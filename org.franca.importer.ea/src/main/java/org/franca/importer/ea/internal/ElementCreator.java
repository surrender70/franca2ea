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
import org.franca.core.franca.FType;
import org.franca.core.franca.FTypeCollection;
import org.franca.core.franca.FTypeDef;
import org.franca.core.franca.FTypeRef;
import org.franca.core.franca.FUnionType;
import org.franca.importer.ea.ImportProcessor;
import org.franca.importer.ea.internal.utils.EAConstants;
import org.franca.importer.ea.internal.utils.EARepositoryAccessor;
import org.franca.importer.ea.internal.utils.ElementContainer;
import org.franca.importer.ea.internal.utils.EAConstants.EClassType;
import org.franca.importer.ea.internal.utils.EAConstants.EParamType;
import org.franca.importer.ea.internal.utils.EAConstants.EStereoType;
import org.franca.importer.ea.internal.utils.EAConstants.EVisibility;
import org.sparx.Attribute;
import org.sparx.Diagram;
import org.sparx.Element;
import org.sparx.Method;
import org.sparx.Package;
import org.sparx.Parameter;

public class ElementCreator extends ImportProcessorDecorator {

	private boolean keepOriginalOrder = false;
	
	public ElementCreator(ImportProcessor lProcessor) {
		super(lProcessor);
		if(EAConstants.KEEP_ORIGINAL_ORDER.equals("true")) {
			keepOriginalOrder = true;
		}
	}

	private static Logger jlog =  Logger.getLogger(ElementCreator.class.getName());
		
	private Method makeMethod(String name, Element parent, String stereoType,
				EVisibility visibility) {
		
		Method m = parent.GetMethods().AddNew(name, "");
		m.SetStereotype(stereoType);
		m.SetVisibility(visibility.getName());
		m.Update();		
		
		parent.Refresh();
		
		return m;
	}
	
	@Override
	public Package handleModel(Package parent, FModel src,
			String packageName) {
		
		Package p = processor.handleModel(parent, src, packageName);
		
		if(p == null) {		
			jlog.log(Level.INFO, "Create Package for Franca Model "+src.getName());			
			p = parent.GetPackages().AddNew(packageName, EClassType.PACKAGE.getName());					
			p.Update();
		}
		
		return p;
	}

	@Override
	public Package handleTypeCollection(Package parent, FTypeCollection src) {
		
		Package p = processor.handleTypeCollection(parent, src);
		
		if(p == null) {		
			jlog.log(Level.INFO, "Create Package for TypeCollection "+src.getName());			
			p = parent.GetPackages().AddNew(src.getName(), EClassType.PACKAGE.getName());					
			if(src.getVersion() != null) {
				p.SetVersion(src.getVersion().getMajor()+"."+src.getVersion().getMinor());
			}
			else {
				jlog.log(Level.WARNING, "Version not set in "+src.getName());
			}
			p.Update();
			parent.Update();
		}
		
		return p;
	}
	
	@Override
	public Element handleInterface(ElementContainer<?> parent, FInterface src) {

		Element e = processor.handleInterface(parent, src);
		
		if(e == null) {		
			jlog.log(Level.INFO, "Create Interface "+src.getName());			
			e = parent.makeElement(src.getName(), EStereoType.INTERFACE, EClassType.INTERFACE, "");
			if(src.getVersion() != null) {
				e.SetVersion(src.getVersion().getMajor()+"."+src.getVersion().getMinor());
			}
			else {
				e.SetVersion("");
				jlog.log(Level.WARNING, "Version not set in "+src.getName());
			}
		}
		
		return e;
	}

	@Override
	public Method handleSimpleMethod(Element parent, FMethod src) {

		Method m = processor.handleSimpleMethod(parent, src);
		
		if(m == null) {		
			jlog.log(Level.INFO, "Create Method "+src.getName());
			String stereotype = EStereoType.METHOD.getName();
			if(src.getFireAndForget() != null) {
				stereotype += " "+EStereoType.METHOD_FIREANDFORGET.getName();
			}
			
			m = makeMethod(src.getName(), parent, stereotype, EVisibility.PUBLIC);
		}
		
		return m;
	}

	@Override
	public Method handleBroadcastMethod(Element parent, FBroadcast src) {

		Method m = processor.handleBroadcastMethod(parent, src);
		
		if(m == null) {
			jlog.log(Level.INFO, "Create Broadcast "+src.getName());
			String stereotype = EStereoType.BROADCAST.getName();
			
			if(src.getSelective() == null) {
				stereotype += " "+EStereoType.BROADCAST_SELECTIVE.getName();
			}
			m = makeMethod(src.getName(), parent, stereotype, EVisibility.PUBLIC);								
			
		}		
		return m;
	}

	@Override
	public Parameter handleParameter(Method parent, EParamType direction,
			FArgument src) {

		Parameter p = processor.handleParameter(parent, direction, src);
		
		if(p == null) {
			jlog.log(Level.INFO, "Create Parameter "+src.getName());
									
			p = parent.GetParameters().AddNew(src.getName(), EClassType.PARAMETER.getName());
			p.SetKind(direction.getName());		
			p.SetType(getTypeName(src.getType()));
			if(keepOriginalOrder) {
				p.SetPosition(parent.GetParameters().GetCount());				
			}
			updateClassifier(p, getTypeName(src.getType()));
			p.Update();
						
			parent.GetParameters().Refresh();
			parent.Update();
		}
		
		return p;
	}

	@Override
	public Parameter handleParameter(Method parent, EParamType direction,
			FEnumerationType src) {
		
		Parameter p = processor.handleParameter(parent, direction, src);
		
		if(p == null) {
			jlog.log(Level.INFO, "Create Error Parameter "+src.getName());
						
			p = parent.GetParameters().AddNew("error", EClassType.PARAMETER.getName());
			p.SetKind(direction.getName());
			
			// check if error is a referenced enum
			if(src.getName() != null) {
				p.SetType(src.getName());
				updateClassifier(p, src.getName());				
			}
			// if not, then enum is extended or defined anonymous
			else {
				// see if extended
				if(src.getBase() != null) {
					p.SetType(src.getBase().getName());
					updateClassifier(p, src.getBase().getName());				
				}// or not
				else {
					p.SetType("");
				}
			}
			
			p.SetStereotype(EAConstants.EStereoType.ERROR_PARAMETER.getName());
			if(keepOriginalOrder) {
				p.SetPosition(parent.GetParameters().GetCount());							
			}
			
			p.Update();		
			parent.GetParameters().Refresh();
			parent.Update();
		}
		
		return p;
	}	
	
	@Override
	public Parameter handleParameter(Method parent, EParamType direction, FEnumerator src) {
		
		Parameter p = processor.handleParameter(parent, direction, src);
		
		if(p == null) {
			jlog.log(Level.INFO, "Create Error Parameter "+src.getName());
			p = parent.GetParameters().AddNew(src.getName(), EClassType.PARAMETER.getName());
			p.SetKind(direction.getName());
			p.SetType("");
			if(keepOriginalOrder) {
				p.SetPosition(parent.GetParameters().GetCount());							
			}
			if(src.getValue() != null) {
				p.SetDefault(src.getValue());
			}
			p.SetStereotype(EAConstants.EStereoType.ERROR_PARAMETER.getName());			
			p.Update();			
			parent.GetParameters().Refresh();
			parent.Update();
		}		
		
		return p;
	}	
	
	@Override
	public Element handleStructure(ElementContainer<?> parent, FStructType src) {
		
		Element structure = processor.handleStructure(parent, src);
		
		if(structure == null) {
			structure = parent.makeElement(src.getName(), EStereoType.STRUCTURE, EClassType.CLASS, "");
			jlog.log(Level.INFO, "Create Structure "+src.getName() + " with element Id "+structure.GetElementID());			
		}		
						
		return structure;
	}

	@Override
	public Element handleUnion(ElementContainer<?> parent, FUnionType src) {
		
		Element union = processor.handleUnion(parent, src);
		
		if(union == null) {
			union = parent.makeElement(src.getName(), EStereoType.UNION, EClassType.CLASS, "");
			jlog.log(Level.INFO, "Create Union "+src.getName() + " with element Id "+union.GetElementID());			
		}		
										
		return union;
	}
	
	@Override
	public Element handleEnumeration(ElementContainer<?> parent, FEnumerationType src) {
		
		Element e = processor.handleEnumeration(parent, src);
		
		if(e == null) {
			jlog.log(Level.INFO, "Create Enumeration "+src.getName());				
			e = parent.makeElement(src.getName(), EStereoType.ENUMERATION, EClassType.ENUMERATION, "");
		}
		
		return e;
	}

	@Override
	public Attribute handleField(Element parent, FField src) {
		
		Attribute attr = processor.handleField(parent, src);
		
		if(attr == null) {
			jlog.log(Level.INFO, "Create Field "+src.getName());		
			
			attr = parent.GetAttributes().AddNew(src.getName(), "");
			attr.SetVisibility(EVisibility.PUBLIC.getName());
			attr.SetType(getTypeName(src.getType()));			
			updateClassifier(attr, src.getType());
			
			if(src.getArray() != null) {
				attr.SetLowerBound("0");
				attr.SetUpperBound("*");
				attr.SetIsOrdered(true);
				attr.SetIsCollection(true);
			}
			
			if(keepOriginalOrder) {
				attr.SetPos(parent.GetAttributes().GetCount());
			}
			
			attr.Update();
			parent.GetAttributes().Refresh();
			parent.Update();
		}		
		
		return attr;
	}

	@Override
	public Attribute handleEnumerator(Element parent, FEnumerator src) {

		Attribute attr = processor.handleEnumerator(parent, src);
		
		if(attr == null) {
			jlog.log(Level.INFO, "Create Enumerator "+src.getName());
			
			attr = parent.GetAttributes().AddNew(src.getName(), "");
			attr.SetDefault(src.getValue());
						
			if(keepOriginalOrder) {
				attr.SetPos(parent.GetAttributes().GetCount());
			}
									
			attr.Update();			
			parent.GetAttributes().Refresh();			
			parent.Update();
		}
				
		return attr;
	}

	@Override
	public Element handleTypedef(ElementContainer<?> parent, FTypeDef src) {
		
		Element e = processor.handleTypedef(parent, src);
		
		if(e == null) {
			jlog.log(Level.INFO, "Create Typedef "+src.getName());
						
			e = parent.makeElement(src.getName(), EStereoType.ALIAS, EClassType.CLASS,
					"Parent="+getTypeName(src.getActualType())+";");			
		}
		
		return e;
	}

	@Override
	public Element handleArray(ElementContainer<?> parent, FArrayType src) {

		Element e = processor.handleArray(parent, src);
		
		if(e == null) {
			
			e = parent.makeElement(src.getName(), EStereoType.ARRAY, EClassType.CLASS,
					"Parent="+getTypeName(src.getElementType())+";");
			
			
			jlog.log(Level.INFO, "Create Array "+src.getName() + " with element Id "+e.GetElementID());			
			
		}
		
		return e;		
		
	}

	@Override
	public Element handleMap(ElementContainer<?> parent, FMapType src) {
		
		Element map = processor.handleMap(parent, src);
		
		if(map == null) {
			map = parent.makeElement(src.getName(), EStereoType.MAP, EClassType.CLASS, "");
			jlog.log(Level.INFO, "Create Map "+src.getName() + " with element Id "+map.GetElementID());			
		}		
						
		return map;		
	}

	@Override
	public Attribute handleMapKey(Element parent, FTypeRef src) {

		Attribute attr = processor.handleMapKey(parent, src);

		if(attr == null) {
			jlog.log(Level.INFO, "Create Map Key "+getTypeName(src));		
			
			attr = parent.GetAttributes().AddNew("key", "");
			attr.SetVisibility(EVisibility.PUBLIC.getName());
			attr.SetType(getTypeName(src));
			updateClassifier(attr, src);
						
			attr.Update();
			parent.GetAttributes().Refresh();
		}		
				
		return attr;
		
	}

	@Override
	public Attribute handleMapValue(Element parent, FTypeRef src) {

		Attribute attr = processor.handleMapValue(parent, src);

		if(attr == null) {
			jlog.log(Level.INFO, "Create Map Value "+getTypeName(src));		
			
			attr = parent.GetAttributes().AddNew("value", "");
			attr.SetVisibility(EVisibility.PUBLIC.getName());
			attr.SetType(getTypeName(src));
			updateClassifier(attr, src);
						
			attr.Update();
			parent.GetAttributes().Refresh();
		}		
				
		return attr;
		
	}

	@Override
	public Attribute handleAttribute(Element parent, FAttribute src) {

		Attribute attr = processor.handleAttribute(parent, src);
		
		if(attr == null) {
			
			jlog.log(Level.INFO, "Create Attribute "+src.getName());		
			String stereoType = EStereoType.ATTRIBUTE.getName();
						
//			if(src.getReadonly() != null) {
//				stereoType += " "+EStereoType.ATTRIBUTE_READONLY.getName();
//			}
//			if(src.getNoSubscriptions() != null) {
//				stereoType += " "+EStereoType.ATTRIBUTE_NOSUBSCRIPTIONS.getName();
//			}
			
			attr = parent.GetAttributes().AddNew(src.getName(), "");
			attr.SetVisibility(EVisibility.PUBLIC.getName());
			attr.SetType(getTypeName(src.getType()));
			updateClassifier(attr, src.getType());
			attr.SetStereotype(stereoType);
						
			attr.Update();
			parent.GetAttributes().Refresh();
			
		}
		
		return attr;
	}


	private void updateClassifier(Attribute attr, FTypeRef type) {
		
		Element e = EARepositoryAccessor.INSTANCE.findElement(getTypeName(type));
		
		if(e != null) {
			attr.SetClassifierID(e.GetElementID());
		}
		
	}
		
	private void updateClassifier(Parameter param, String type) {
		
		Element e = EARepositoryAccessor.INSTANCE.findElement(type);
		
		if(e != null) {
			param.SetClassifierID(""+e.GetElementID());
		}
		
	}
			
	
}
