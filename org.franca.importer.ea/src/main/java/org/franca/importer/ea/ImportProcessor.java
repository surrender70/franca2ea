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
package org.franca.importer.ea;

import org.franca.core.franca.FAnnotation;
import org.franca.core.franca.FAnnotationType;
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
import org.franca.core.franca.FModelElement;
import org.franca.core.franca.FStructType;
import org.franca.core.franca.FTypeCollection;
import org.franca.core.franca.FTypeDef;
import org.franca.core.franca.FTypeRef;
import org.franca.core.franca.FUnionType;
import org.franca.importer.ea.internal.utils.ElementContainer;
import org.franca.importer.ea.internal.utils.EAConstants.EParamType;
import org.sparx.Attribute;
import org.sparx.Element;
import org.sparx.Method;
import org.sparx.Package;
import org.sparx.Parameter;

public abstract class ImportProcessor {

	/**
	 * Handles the franca model
	 * @param parent The parent package where the resulting package should be placed
	 * @param src The franca model (root element)
	 * @param packageName An optional package name 
	 * @return The corresponding EA package for the given franca model
	 */
	public abstract Package handleModel(Package parent, FModel src, String packageName) ;
	
	/**
	 * Handles a franca type collection
	 * @param parent The parent package where the resulting package should be placed
	 * @param src The franca type collection
	 * @return The corresponding EA package for the given franca type collection
	 */
	public abstract Package handleTypeCollection(Package parent, FTypeCollection src);

	/**
	 * Handles a franca interface
	 * @param parent The parent container where resulting element should be placed (can be a Package or an Element)
	 * @param src The franca interface
	 * @return The corresponding EA element for the given franca interface
	 */
	public abstract Element handleInterface(ElementContainer<?> parent, FInterface src) ;
	
	/**
	 * Handles a franca method
	 * @param parent The element where the resulting method should be placed
	 * @param src The franca method
	 * @return The corresponding EA method for the given franca method
	 */
	public abstract Method handleSimpleMethod(Element parent, FMethod src) ;
	
	/**
	 * Handles a franca broadcast
	 * @param parent The element where the resulting method should be placed
	 * @param src The franca broadcast
	 * @return The corresponding EA method for the given franca broadcast
	 */
	public abstract Method handleBroadcastMethod(Element parent, FBroadcast src) ;
	
	/**
	 * Handles a franca attribute
	 * @param parent The element where the resulting attribute should be placed
	 * @param src The franca attribute
	 * @return The corresponding EA attribute for the given franca attribute
	 */	
	public abstract Attribute handleAttribute(Element parent, FAttribute src);
	
	/**
	 * Handles a franca parameter
	 * @param parent The method where the resulting parameter should be placed
	 * @param direction Describes the parameter direction (in/out)
	 * @param src The franca parameter
	 * @return The corresponding EA method for the given franca argument
	 */	
	public abstract Parameter handleParameter(Method parent, EParamType direction, FArgument src) ;
	
	/**
	 * Handles a franca error parameter 
	 * @param parent The method where the resulting parameter should be placed
	 * @param direction Describes the parameter direction (in/out)
	 * @param src The franca error parameter
	 * @return The corresponding EA method for the given franca error parameter
	 */	
	public abstract Parameter handleParameter(Method parent, EParamType direction, FEnumerationType src);

	/**
	 * Handles a franca anonymous error parameter 
	 * @param parent The method where the resulting parameter should be placed
	 * @param direction Describes the parameter direction (in/out)
	 * @param src The franca anonymous error parameter
	 * @return The corresponding EA method for the given franca anonymous error parameter
	 */	
	public abstract Parameter handleParameter(Method parent, EParamType direction, FEnumerator src);

	/**
	 * Handles a franca structure
	 * @param parent The parent container where resulting element should be placed (can be a Package or an Element)
	 * @param src The franca structure
	 * @return The corresponding EA element for the given franca structure
	 */
	public abstract Element handleStructure(ElementContainer<?> parent, FStructType src) ;
	
	/**
	 * Handles a franca union
	 * @param parent The parent container where resulting element should be placed (can be a Package or an Element)
	 * @param src The franca union
	 * @return The corresponding EA element for the given franca union
	 */
	public abstract Element handleUnion(ElementContainer<?> parent, FUnionType src);
	
	/**
	 * Handles a franca field
	 * @param parent The element where the resulting attribute should be placed
	 * @param src The franca field
	 * @return The corresponding EA attribute for the given franca field
	 */		
	public abstract Attribute handleField(Element parent, FField src);
	
	/**
	 * Handles a franca enumeration
	 * @param parent The parent container where resulting element should be placed (can be a Package or an Element)
	 * @param src The franca enumeration
	 * @return The corresponding EA element for the given franca enumeration
	 */
	public abstract Element handleEnumeration(ElementContainer<?> parent, FEnumerationType src) ;
	
	/**
	 * Handles a franca enumerator
	 * @param parent The element where the resulting attribute should be placed
	 * @param src The franca enumerator
	 * @return The corresponding EA attribute for the given franca enumerator
	 */		
	public abstract Attribute handleEnumerator(Element parent, FEnumerator src) ;
	
	/**
	 * Handles a franca typedef
	 * @param parent The parent container where resulting element should be placed (can be a Package or an Element)
	 * @param src The franca typedef
	 * @return The corresponding EA element for the given franca typedef
	 */
	public abstract Element handleTypedef(ElementContainer<?> parent, FTypeDef src);
	
	/**
	 * Handles a franca array
	 * @param parent The parent container where resulting element should be placed (can be a Package or an Element)
	 * @param src The franca array
	 * @return The corresponding EA element for the given franca array
	 */
	public abstract Element handleArray(ElementContainer<?> parent, FArrayType src);
	
	/**
	 * Handles a franca map
	 * @param parent The parent container where resulting element should be placed (can be a Package or an Element)
	 * @param src The franca map
	 * @return The corresponding EA element for the given franca map
	 */
	public abstract Element handleMap(ElementContainer<?> parent, FMapType src);
	
	/**
	 * Handles a franca map key
	 * @param parent The element where the resulting attribute should be placed
	 * @param src The franca map key
	 * @return The corresponding EA attribute for the given franca map key
	 */		
	public abstract Attribute handleMapKey(Element parent, FTypeRef src);

	/**
	 * Handles a franca map value
	 * @param parent The element where the resulting attribute should be placed
	 * @param src The franca map value
	 * @return The corresponding EA attribute for the given franca map value
	 */		
	public abstract Attribute handleMapValue(Element parent, FTypeRef src);
	
	/**
	 * Retrieves the typename (String) of a given franca type (Object)
	 * @param type The given franca type
	 * @return The resulting type name
	 */
	protected String getTypeName(FTypeRef type) {
		
		// The type is either derived or predefined
		if(type.getDerived() != null) {
			return type.getDerived().getName();
		}
		else {
			return type.getPredefined().getName();
		}
		
	}	
	
	/**
	 * Retrieves the description of a franca element annotated with "@description"
	 * @param src A given model element
	 * @return The description of the given model element
	 */
	protected String getDescription(FModelElement src) {

		return getDescriptionFromAnnotation(src, FAnnotationType.DESCRIPTION);

	}
	
	/**
	 * Retrieves the author name of a franca element annotated with "@author"
	 * @param src A given model element
	 * @return The author name of the given model element
	 */
	protected String getAuthor(FModelElement src) {
		
		return getDescriptionFromAnnotation(src, FAnnotationType.AUTHOR);		
	}
	
	private String getDescriptionFromAnnotation(FModelElement src, FAnnotationType annotationType) {
		
		if(src.getComment() != null) {
			for(FAnnotation fa: src.getComment().getElements()) {
				if(fa.getType().equals(annotationType)) {
					return fa.getComment().trim();
				}
			}
			return "";
		}
		
		else {
			return "";
		}
	}	
		
}
