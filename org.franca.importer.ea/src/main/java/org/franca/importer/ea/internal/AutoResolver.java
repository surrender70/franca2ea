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
package org.franca.importer.ea.internal;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.franca.core.franca.FArgument;
import org.franca.core.franca.FArrayType;
import org.franca.core.franca.FAttribute;
import org.franca.core.franca.FBroadcast;
import org.franca.core.franca.FEnumerationType;
import org.franca.core.franca.FEnumerator;
import org.franca.core.franca.FField;
import org.franca.core.franca.FInterface;
import org.franca.core.franca.FMapType;
import org.franca.core.franca.FMethod;
import org.franca.core.franca.FModel;
import org.franca.core.franca.FStructType;
import org.franca.core.franca.FTypeDef;
import org.franca.core.franca.FTypeRef;
import org.franca.core.franca.FUnionType;
import org.franca.importer.ea.ImportProcessor;
import org.franca.importer.ea.internal.utils.EAConstants;
import org.franca.importer.ea.internal.utils.ElementContainer;
import org.sparx.Attribute;
import org.sparx.Element;
import org.sparx.Method;
import org.sparx.Package;
import org.sparx.Parameter;

public class AutoResolver extends ImportProcessorDecorator {


	private static Logger jlog = Logger
			.getLogger(AutoResolver.class.getName());

	public final static int MIN_ELEMENTS_TO_COMPARE = 2;
	
	public AutoResolver(ImportProcessor provider) {
		super(provider);
	}

	
	@Override
	public Element handleStructure(ElementContainer<?> parent, FStructType src) {

		Element s = processor.handleStructure(parent, src);
		
		if(s != null) {
			return s;
		}
		
		// See if there is a structure defined, that has the same description as the given Franca structure.
		// This could be a hint, that the structure was renamed		
		
		for (Element e : parent.getAllElements()) {
			if (e.GetStereotype().equals(EAConstants.EStereoType.STRUCTURE.getName())
					&& e.GetNotes().equals(getDescription(src)) 
					&& e.GetNotes().length() > 5) {
				jlog.log(Level.WARNING, "Found Element " + e.GetName()
						+ " with same description as " + src.getName());
				e.SetName(src.getName());
				e.Update();
				return e;
			}
			
			if(e.GetStereotype().equals(EAConstants.EStereoType.STRUCTURE.getName()) 
					&& (e.GetAttributes().GetCount() == src.getElements().size())
					&& src.getElements().size()> MIN_ELEMENTS_TO_COMPARE
					) {
				
				int findingCnt = 0;
				
				for(Attribute a: e.GetAttributes()) {
					for(FField f: src.getElements()) {
						if(a.GetName().equals(f.getName())) {
							findingCnt++;
						}
					}
				}
				
				if(findingCnt == src.getElements().size()) {
					jlog.log(Level.WARNING, "Found Element " + e.GetName()
							+ " with same attributes as " + src.getName());
					e.SetName(src.getName());
					e.Update();
					return e;
				}
				
			}
			
		}
		
		

		return null;
	}

	@Override
	public Element handleEnumeration(ElementContainer<?> parent, FEnumerationType src) {

		Element enumeration = processor.handleEnumeration(parent, src);
		
		if(enumeration != null) {
			return enumeration;
		}
		
		for (Element e : parent.getAllElements()) {
			if (e.GetStereotype().equals(EAConstants.EStereoType.ENUMERATION.getName())
					&& e.GetNotes().equals(getDescription(src)) 
					&& e.GetNotes().length() > 5) {
				jlog.log(Level.WARNING, "Found Element " + e.GetName()
						+ " with same description as " + src.getName());
				e.SetName(src.getName());
				e.Update();
				return e;
			}
			
			if(e.GetStereotype().equals(EAConstants.EStereoType.ENUMERATION.getName()) 
					&& (e.GetAttributes().GetCount() == src.getEnumerators().size())
					&& src.getEnumerators().size()> MIN_ELEMENTS_TO_COMPARE
					) {
				
				int findingCnt = 0;
				
				for(Attribute a: e.GetAttributes()) {
					for(FEnumerator en: src.getEnumerators()) {
						if(a.GetName().equals(en.getName())) {
							findingCnt++;
						}
					}
				}
				
				if(findingCnt == src.getEnumerators().size()) {
					jlog.log(Level.WARNING, "Found Element " + e.GetName()
							+ " with same attributes as " + src.getName());
					e.SetName(src.getName());
					e.Update();
					return e;
				}
				
			}
			
		}		
		
		return null;
	}

	@Override
	public Attribute handleField(Element parent, FField src) {
		
		Attribute a = processor.handleField(parent, src);

		if(a != null) {
			if(!a.GetType().equals(getTypeName(src.getType()))) {
				
				jlog.log(Level.WARNING, "Type Mismatch for element "+a.GetName());
				
				a.SetType(getTypeName(src.getType()));
				a.Update();
			}			
		}
						
		return a;
	}
	
}
