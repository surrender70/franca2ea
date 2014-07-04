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

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.franca.core.franca.FModel;
import org.franca.core.franca.Import;

public class FModelSorter extends FrancaSorter<FModel> {

	private static Logger jlog =  Logger.getLogger(FModelSorter.class.getName());
	
//	private HashMap<FModel, String> containerMap = new HashMap<FModel, String>(); 

	public FModelSorter(List<FModel> toSort) {
		super(toSort);
//		containerMap = _container;
	}

	@Override
	public boolean canSwitch(FModel m) throws SortException {

		if (m.getImports().size() == 0) {
			return true;
		} else {
			boolean canSwitch = true;
			for (Import i : m.getImports()) {			
				FModel parent = getFModelFromImport(i);
				if (!isReferenceResolved(parent)) {
					canSwitch = false;
				}
			}
			return canSwitch;
		}
	}
	
	/**
	 * Try to retrieve the FModel, where the given Import refers to
	 * It must be contained in one of the lists
	 * @param i The given Import
	 * @return The FModel, that is referred from the given Import
	 * @throws SortException 
	 */
	private FModel getFModelFromImport(Import i) throws SortException {		
		
		for(FModel m: mRemaining) {
			if(modelNameEqualsImportName(m, i)) {
				return m;
			}
		}

		for(FModel m: mSorted) {
			if(modelNameEqualsImportName(m, i)) {
				return m;
			}
		}

		// If the import is not found in any of the parent models, there is obviously s.th. wrong
		throw new SortException("Can not find parent model for corresponding import "+i.getImportURI());
	}

	private boolean modelNameEqualsImportName(FModel m, Import i) throws SortException {
		
		// Check if model was loaded via fidl file reader
		if(m.eResource() != null) {
			//FIXME What if a model with the same file name exists more than once, but in different paths
			//This is a potential bug
			String[] pathElements = i.getImportURI().split("/");
			
			return m.eResource().getURI().lastSegment().equals(pathElements[pathElements.length-1]);
		}
		// No, then the franca model was created differently, probably via M2M transformation
		else {
			throw new SortException("Obviously franca model was not loaded via Resource, but probably via M2M transformation");
		}
	}
	
}
