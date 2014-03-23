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
import org.franca.core.franca.FTypeCollection;
import org.franca.core.franca.FTypeDef;
import org.franca.core.franca.FTypeRef;
import org.franca.core.franca.FUnionType;
import org.franca.importer.ea.ImportProcessor;
import org.franca.importer.ea.internal.utils.ElementContainer;
import org.franca.importer.ea.internal.utils.EAConstants.EParamType;
import org.franca.importer.ea.internal.utils.EAConstants.EStereoType;
import org.sparx.Attribute;
import org.sparx.Collection;
import org.sparx.Element;
import org.sparx.Method;
import org.sparx.Package;
import org.sparx.Parameter;

public class FindByName extends ImportProcessorDecorator {

	private static Logger jlog =  Logger.getLogger(FindByName.class.getName());
	
	@Override
	public Package handleModel(Package parent, FModel src,
			String packageName) {
		
		jlog.log(Level.FINE, "Retrieve Package "+src.getName());		

		return findRootPackage(parent, packageName);
		
	}
	
	
	@Override
	public Package handleTypeCollection(Package parent, FTypeCollection src) {

		jlog.log(Level.FINE, "Retrieve Package "+src.getName());		

		return parent.GetPackages().GetByName(src.getName());
		
//		return super.makePackage(parent, src);
	}


	public FindByName(ImportProcessor provider) {
		super(provider);
	}
	
	@Override
	public Element handleInterface(ElementContainer<?> parent, FInterface src) {

		jlog.log(Level.FINE, "Retrieve Interface "+src.getName());		

		return parent.findElementByName(src.getName(), EStereoType.INTERFACE);
				
	}

	@Override
	public Method handleSimpleMethod(Element parent, FMethod src) {
				
		jlog.log(Level.FINE, "Retrieve Method "+src.getName());		

		return findMethodByName(parent, src.getName());
		
	}

	@Override
	public Method handleBroadcastMethod(Element parent, FBroadcast src) {
		
		jlog.log(Level.FINE, "Retrieve Broadcast "+src.getName());		

		return findMethodByName(parent, src.getName());
		
	}

	@Override
	public Parameter handleParameter(Method parent, EParamType direction,
			FArgument src) {

		jlog.log(Level.FINE, "Retrieve Parameter "+src.getName());		

		return findParameterByName(parent, src.getName(), direction);
	}

	@Override
	public Element handleStructure(ElementContainer<?> parent, FStructType src) {

		jlog.log(Level.FINE, "Retrieve Structure "+src.getName());		

		return parent.findElementByName(src.getName(), EStereoType.STRUCTURE);
		
	}

	@Override
	public Element handleUnion(ElementContainer<?> parent, FUnionType src) {

		jlog.log(Level.FINE, "Retrieve Union "+src.getName());		

		return parent.findElementByName(src.getName(), EStereoType.UNION);
		
	}
	
	
	@Override
	public Element handleEnumeration(ElementContainer<?> parent, FEnumerationType src) {

		jlog.log(Level.FINE, "Retrieve Enumeration "+src.getName());		

		return parent.findElementByName(src.getName(), EStereoType.ENUMERATION);
		
	}

	private Parameter findParameterByName(Method parent, String name, EParamType direction) {
						
		Parameter result = null;
		
		if(parent != null) {
			for(Parameter p: parent.GetParameters()) {
				if(p.GetName().equals(name) && p.GetKind().endsWith(direction.getName())) {
					result = p;
					break;
				}
			}
		}
		
		return result;
		
	}
		
	private Method findMethodByName(Element parent, String name) {

		// UNFORTUNATELY FEATURE IS NOT SUPPORTED BY EA SDK
		// Action is not supported at org.sparx.Collection.comGetByName(Native Method)
		// return parent.GetMethods().GetByName(name);
		Method result = null;
		
		if(parent != null) {
			for(Method m: parent.GetMethods()) {
				if(m.GetName().equals(name)) {
					result = m;
					break;
				}
			}
		}
		
		return result;
		
	}

	private Attribute findAttributeByName(Element parent, String name) {

		// UNFORTUNATELY FEATURE IS NOT SUPPORTED BY EA SDK
		// Action is not supported at org.sparx.Collection.comGetByName(Native Method)
		// return parent.GetAttributes().GetByName(name);

		Attribute result = null;
		
		for(Attribute a: parent.GetAttributes()) {
			if(a.GetName().equals(name)) {
				result = a;
				break;
			}
		}
				
		return result;
				
	}
	
	private Package findRootPackage(Package parent, String name) {
		
		try {
			return parent.GetPackages().GetByName(name);			
		}
		
		catch (Exception e) {
			jlog.log(Level.SEVERE, parent.GetName()+" "+name+ " "+e.toString());		
			return null;
		}
				
	}
	
	
	@Override
	public Attribute handleField(Element parent, FField src) {
		
		jlog.log(Level.FINE, "Retrieve Field "+src.getName());		

		return findAttributeByName(parent, src.getName());
		
	}


	@Override
	public Attribute handleEnumerator(Element parent, FEnumerator src) {

		jlog.log(Level.FINE, "Retrieve Enumerator "+src.getName());		

		return findAttributeByName(parent, src.getName());
		
	}


	@Override
	public Element handleTypedef(ElementContainer<?> parent, FTypeDef src) {
		
		jlog.log(Level.FINE, "Retrieve Typedef "+src.getName());
		
		return parent.findElementByName(src.getName(), EStereoType.ALIAS);
		
	}


	@Override
	public Element handleArray(ElementContainer<?> parent, FArrayType src) {
		
		jlog.log(Level.FINE, "Retrieve Array "+src.getName());
		
		return parent.findElementByName(src.getName(), EStereoType.ARRAY);
		
	}


	@Override
	public Element handleMap(ElementContainer<?> parent, FMapType src) {
		
		jlog.log(Level.FINE, "Retrieve Map "+src.getName());		

		return parent.findElementByName(src.getName(), EStereoType.MAP);
		
	}


	@Override
	public Attribute handleMapKey(Element parent, FTypeRef src) {
		
		jlog.log(Level.FINE, "Retrieve Map Key ");		

		return findAttributeByName(parent, "key");
				
	}


	@Override
	public Attribute handleMapValue(Element parent, FTypeRef src) {
		
		jlog.log(Level.FINE, "Retrieve Map Value ");		

		return findAttributeByName(parent, "value");
				
	}


	@Override
	public Attribute handleAttribute(Element parent, FAttribute src) {

		jlog.log(Level.FINE, "Retrieve Attribute "+src.getName());		

		return findAttributeByName(parent, src.getName());
				
	}


	@Override
	public Parameter handleParameter(Method parent, EParamType direction,
			FEnumerationType src) {
		
		jlog.log(Level.SEVERE, "NOT YET IMPLEMENTED");
		
		return null;
	}


	@Override
	public Parameter handleParameter(Method parent, EParamType direction,
			FEnumerator src) {
		
		jlog.log(Level.SEVERE, "NOT YET IMPLEMENTED");
		
		return null;
	}

	
}
