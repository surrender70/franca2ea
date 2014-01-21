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
	public Element makeStructure(ElementContainer<?> parent, FStructType src) {
		
		Element e = processor.makeStructure(parent, src);
		
		if(e != null) {
			e.SetAppearance(1, 0, colorStruct);
			e.Update();
		}
		
		return e;
	}

	@Override
	public Element makeUnion(ElementContainer<?> parent, FUnionType src) {
		
		Element e = processor.makeUnion(parent, src);
		
		if(e != null) {
			e.SetAppearance(1, 0, colorUnion);
			e.Update();
		}
		
		return e;
	}
	
	
	@Override
	public Element makeEnumeration(ElementContainer<?> parent, FEnumerationType src) {
		
		Element e = processor.makeEnumeration(parent, src);
		
		if(e != null) {
			e.SetAppearance(1, 0, colorEnum);
			e.Update();
		}
		
		return e;
	}

	@Override
	public Element makeTypedef(ElementContainer<?> parent, FTypeDef src) {
		
		Element e = processor.makeTypedef(parent, src);
		
		if(e != null) {
			e.SetAppearance(1, 0, colorTypdef);
			e.Update();
		}
		
		return e;
	}

	@Override
	public Element makeArray(ElementContainer<?> parent, FArrayType src) {
		
		Element e = processor.makeArray(parent, src);
		
		if(e != null) {
			e.SetAppearance(1, 0, colorArray);
			e.Update();
		}
		
		return e;
	}

	@Override
	public Element makeMap(ElementContainer<?> parent, FMapType src) {
		
		Element e = processor.makeMap(parent, src);
		
		if(e != null) {
			e.SetAppearance(1, 0, colorMap);
			e.Update();
		}
		
		return e;
	}
	
	
}