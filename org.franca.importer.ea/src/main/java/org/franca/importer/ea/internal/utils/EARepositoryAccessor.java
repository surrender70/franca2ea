/*******************************************************************************
 * Copyright (c) 2014 Torsten Mosis.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Torsten Mosis - initial API and implementation
 ******************************************************************************/
package org.franca.importer.ea.internal.utils;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.franca.core.franca.FModel;
import org.franca.importer.ea.internal.utils.EAConstants.EClassType;
import org.franca.importer.ea.internal.utils.EAConstants.EStereoType;
import org.sparx.Attribute;
import org.sparx.Collection;
import org.sparx.CreateModelType;
import org.sparx.Diagram;
import org.sparx.DiagramObject;
import org.sparx.Element;
import org.sparx.Package;
import org.sparx.Repository;

public enum EARepositoryAccessor {

	INSTANCE;

	private static Logger jlog = Logger.getLogger(EARepositoryAccessor.class
			.getName());

	private Repository rep = null;

	private LifecycleHelper lifeCycle = new LifecycleHelper();

	private Package finalPackage;

	public boolean openProject(String file, boolean createIfNotExisting) {

		boolean opened = false;

		jlog.log(Level.INFO, "Opening EA Project " + file + "...");

		lifeCycle.start();

		File projectFile = new File(file);

		if (projectFile.exists()) {
			rep = new Repository();
			opened = rep.OpenFile(projectFile.getAbsolutePath());
			jlog.log(Level.INFO, "Project Opened !");
		} else if (createIfNotExisting) {
			jlog.log(Level.INFO, "Project " + file
					+ " not found ! Trying to create...");
			rep = new Repository();
			opened = rep.CreateModel(CreateModelType.cmEAPFromBase,
					projectFile.getAbsolutePath(), 0);
			
			if(opened == false) {
				jlog.log(Level.SEVERE, "Could not create project "+projectFile.getAbsolutePath());
			}
			else {
				jlog.log(Level.INFO, "Project Created !");
			}

			// FIXME: I need to re-open the project, otherwise I receive a
			// native exception. Needs some more investigation
			if (opened) {
				closeProject();
				return openProject(file, createIfNotExisting);
			}
		}

		return opened;

	}

	public void closeProject() {

		finalPackage = null;

		rep.CloseFile();

		rep.destroy();

		lifeCycle.stop();

		EAConstants.MappingProperties.writeProperties();

		jlog.log(Level.INFO, "Project Closed !");

	}

	public void reloadProject() {

		jlog.log(Level.INFO, "Project Reloaded !");

		rep.GetProjectInterface().ReloadProject();

	}

	public Element findElement(String name) {

		jlog.log(Level.FINE, "Searching for UML Element " + name);

		Collection<Element> structs = rep.GetElementsByQuery("Simple", name);

		for (Element s : structs) {
			// searching for the exact name
			if (s.GetName().equals(name)) {
				if (s.GetStereotype().equals(EStereoType.STRUCTURE.getName())
						|| s.GetStereotype().equals(EStereoType.MAP.getName())
						|| s.GetStereotype()
								.equals(EStereoType.UNION.getName())
						|| s.GetStereotype().equals(
								EStereoType.ENUMERATION.getName())
						|| s.GetStereotype()
								.equals(EStereoType.ALIAS.getName())
						|| s.GetStereotype()
								.equals(EStereoType.ARRAY.getName())
						|| s.GetStereotype().equals(
								EStereoType.INTERFACE.getName())) {
					jlog.log(Level.FINE, "UML Element " + name
							+ " found, with Id " + s.GetElementID());
					return s;
				} else {
					jlog.log(Level.FINE, "UML Element " + name
							+ " found, but no data type.");
					return null;
				}
			}
		}

		jlog.log(Level.FINE, "UML Element " + name + " not found.");

		return null;
	}

	public Package findOrCreateRootPackage(String path, boolean createIfNotExisting) {

		if(rep == null) {
			jlog.log(Level.SEVERE, "Repository is null");
			return null;
		}

		// Collection<Package> modelPackage = rep.GetModels();
		String[] pathElements = path.split("/");
				
		Collection<Package> packages = rep.GetModels();
		finalPackage = null;

		for (String ele : pathElements) {

			boolean found = false;

			for (Package p : packages) {

				if (p.GetName().equals(ele)) {
					packages = p.GetPackages();
					finalPackage = p;
					found = true;
					break;
				}
			}

			if (!found) {
				jlog.log(Level.INFO, "Can not find Package: " + ele
						+ " in path " + path);

				if (createIfNotExisting) {
					jlog.log(Level.INFO, "Trying to create Package: " + ele);
					Package newPackage = finalPackage.GetPackages().AddNew(ele,
							EClassType.PACKAGE.getName());
					newPackage.Update();
					finalPackage.GetPackages().Refresh();
					finalPackage = newPackage;
				} else {
					jlog.log(Level.SEVERE,
							"Import stopped. Package not found and should not be created !!!");
				}

			}

		}

		rep.GetModels().Refresh();

		return finalPackage;
	}

	/**
	 * Finds or creates the EA Package, where the elements of the Franca Model
	 * should be stored
	 * 
	 * e.g.: eaRootPackage = Model - MyLogicalView 
	 *       francaPackage = org.example.mymodel
	 * 
	 * The resulting Package will then be:
	 * Model/MyLogicalView/org/example/mymodel
	 * 
	 * @param eaRootPackage
	 *            The EA Root Package
	 * @param francaPackage
	 *            The package name of the Franca Model
	 * @return
	 */
	public Package findLocalPackageForFrancaModel(Package eaRootPackage,
			String francaPackage) {

		String[] fPackageElems = francaPackage.split("\\.");
		Package packagePointer = eaRootPackage;

		String rootPackage = fPackageElems[0];

		for (String p : fPackageElems) {

			jlog.log(Level.INFO, "packagePointer="+packagePointer.GetName()+", SubPackages="+packagePointer.GetPackages().GetCount());
			Collection<Package> subPackages = packagePointer.GetPackages();

			boolean found = false;
			for (Package eaP1 : subPackages) {
				if (eaP1.GetName().equals(p)) {
					found = true;
					packagePointer = eaP1;
					break;
				}
			}

			if (!found) {
				jlog.log(Level.INFO, "Creating New Package {"+p+"}, SuperPackage {"+packagePointer.GetName()+"}");
				Package res = packagePointer.GetPackages().AddNew(p,
						EClassType.PACKAGE.getName());
				if (p.equals(rootPackage)) {
					res.Update();
					res.GetElement().SetStereotype(
							EAConstants.EStereoType.ROOTPACKAGE.getName());
				}
				res.Update();
				packagePointer.GetPackages().Refresh();
				// newPackages.put(currentPackage, res);
				packagePointer = res;
			}
		}

		return packagePointer;
	}

	public boolean layoutDiagram(Diagram diagram) {

		return rep.GetProjectInterface().LayoutDiagram(
				diagram.GetDiagramGUID(), 0);

	}

	public void createPackageDiagram(Package eaPackage) {

		// Delete the diagram if already exists
		for (short i = 0; i < eaPackage.GetDiagrams().GetCount(); i++) {
			Diagram d = eaPackage.GetDiagrams().GetAt(i);
			if (d.GetName().equals("Type Overview")) {
				eaPackage.GetDiagrams().Delete(i);
			}
		}
		eaPackage.GetDiagrams().Refresh();

		HashSet<Integer> localElements = new HashSet<Integer>();

		Diagram diagram = eaPackage.GetDiagrams().AddNew("Type Overview",
				"Logical");
		diagram.Update();

		// Collect all local elements
		for (Element e : eaPackage.GetElements()) {
			localElements.add(e.GetElementID());
			DiagramObject dObj = diagram.GetDiagramObjects().AddNew("", "");
			dObj.SetElementID(e.GetElementID());
			dObj.SetDiagramID(diagram.GetDiagramID());
			dObj.Update();
		}

		// Collect all referenced elements
		if (EAConstants.DIAGRAM_SHOW_EXTERNAL_DEPENDENCIES.equals("true")) {
			for (Element e : eaPackage.GetElements()) {
				for (Attribute a : e.GetAttributes()) {
					Element refElem = EARepositoryAccessor.INSTANCE
							.findElement(a.GetType());
					if (refElem != null) {
						if (!localElements.contains(refElem.GetElementID())) {
							localElements.add(refElem.GetElementID());
							DiagramObject dObj = diagram.GetDiagramObjects()
									.AddNew("", "");
							dObj.SetElementID(refElem.GetElementID());
							dObj.SetDiagramID(diagram.GetDiagramID());
							int bColor = Integer
									.parseInt(
											EAConstants.DIAGRAM_EXTERNAL_DEPENDENCIES_BACKGROUND,
											16);
							dObj.SetStyle("BCol=" + bColor);
							dObj.Update();
						}
					}
				}
			}
		}

		localElements.clear();

		diagram.SetOrientation("P");
		diagram.Update();

		layoutDiagram(diagram);

		eaPackage.GetDiagrams().Refresh();

	}

}
