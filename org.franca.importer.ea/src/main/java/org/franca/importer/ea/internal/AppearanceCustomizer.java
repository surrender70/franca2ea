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

import org.franca.core.franca.FArrayType;
import org.franca.core.franca.FEnumerationType;
import org.franca.core.franca.FMapType;
import org.franca.core.franca.FStructType;
import org.franca.core.franca.FTypeDef;
import org.franca.core.franca.FUnionType;
import org.franca.importer.ea.ImportProcessor;
import org.franca.importer.ea.internal.utils.EAConstants;
import org.franca.importer.ea.internal.utils.ElementContainer;
import org.sparx.Element;


public class AppearanceCustomizer extends ImportProcessorDecorator {
	
	private int colorStruct;
	private int colorEnum;
	private int colorArray;
	private int colorTypdef;
	private int colorUnion;
	private int colorMap;
	
	public AppearanceCustomizer(ImportProcessor lProcessor) {
		super(lProcessor);
		
		colorStruct = Integer.parseInt(EAConstants.EBackgroundColor.STRUCTURE.getName(), 16);
		colorUnion = Integer.parseInt(EAConstants.EBackgroundColor.UNION.getName(), 16);
		colorMap = Integer.parseInt(EAConstants.EBackgroundColor.MAP.getName(), 16);
		colorEnum = Integer.parseInt(EAConstants.EBackgroundColor.ENUMERATION.getName(), 16);
		colorTypdef = Integer.parseInt(EAConstants.EBackgroundColor.TYPEDEF.getName(), 16);
		colorArray = Integer.parseInt(EAConstants.EBackgroundColor.ARRAY.getName(), 16);

	}
	
	@Override
	public Element handleStructure(ElementContainer<?> parent, FStructType src) {
		
		Element e = processor.handleStructure(parent, src);
		
		if(e != null) {
			e.SetAppearance(1, 0, colorStruct);
			e.Update();
		}
		
		return e;
	}

	@Override
	public Element handleUnion(ElementContainer<?> parent, FUnionType src) {
		
		Element e = processor.handleUnion(parent, src);
		
		if(e != null) {
			e.SetAppearance(1, 0, colorUnion);
			e.Update();
		}
		
		return e;
	}
	
	
	@Override
	public Element handleEnumeration(ElementContainer<?> parent, FEnumerationType src) {
		
		Element e = processor.handleEnumeration(parent, src);
		
		if(e != null) {
			e.SetAppearance(1, 0, colorEnum);
			e.Update();
		}
		
		return e;
	}

	@Override
	public Element handleTypedef(ElementContainer<?> parent, FTypeDef src) {
		
		Element e = processor.handleTypedef(parent, src);
		
		if(e != null) {
			e.SetAppearance(1, 0, colorTypdef);
			e.Update();
		}
		
		return e;
	}

	@Override
	public Element handleArray(ElementContainer<?> parent, FArrayType src) {
		
		Element e = processor.handleArray(parent, src);
		
		if(e != null) {
			e.SetAppearance(1, 0, colorArray);
			e.Update();
		}
		
		return e;
	}

	@Override
	public Element handleMap(ElementContainer<?> parent, FMapType src) {
		
		Element e = processor.handleMap(parent, src);
		
		if(e != null) {
			e.SetAppearance(1, 0, colorMap);
			e.Update();
		}
		
		return e;
	}
	
	
}
