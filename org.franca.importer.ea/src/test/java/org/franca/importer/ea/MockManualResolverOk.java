package org.franca.importer.ea;

import java.util.List;

import org.franca.core.franca.FArrayType;
import org.franca.core.franca.FEnumerationType;
import org.franca.core.franca.FStructType;
import org.franca.core.franca.FTypeDef;
import org.franca.core.franca.FUnionType;
import org.franca.importer.ea.internal.utils.EAConstants;
import org.franca.importer.ea.internal.utils.EAConstants.EStereoType;
import org.sparx.Attribute;
import org.sparx.Element;

public class MockManualResolverOk implements ManualResolveCallback {

	@Override
	public Element selectElement(FStructType struct, List<Element> elems) {
		
		Element result = null;
		
		for(Element e: elems) {
			if( (e.GetName()+"_Renamed").equals(struct.getName()) 
					&& e.GetStereotype().equals(EStereoType.STRUCTURE.getName()) ) {
				result = e;
				break;
			}
		}
		
		return result;
	}

	
	
	@Override
	public Element selectElement(FUnionType union, List<Element> elems) {
		
		Element result = null;
		
		for(Element e: elems) {
			if( (e.GetName()+"_Renamed").equals(union.getName()) 
					&& e.GetStereotype().equals(EStereoType.UNION.getName()) ) {
				result = e;
				break;
			}
		}
		
		return result;
	}



	@Override
	public Element selectElement(FEnumerationType enumeration,
			List<Element> elems) {
		
		Element result = null;
		
		for(Element e: elems) {
			if( (e.GetName()+"_Renamed").equals(enumeration.getName()) 
					&& e.GetStereotype().equals(EStereoType.ENUMERATION.getName()) ) {
				result = e;
				break;
			}
		}
		
		return result;
	}

	@Override
	public Element selectElement(FArrayType array, List<Element> elems) {

		Element result = null;
		
		for(Element e: elems) {
			if( (e.GetName()+"_Renamed").equals(array.getName()) 
					&& e.GetStereotype().equals(EStereoType.ARRAY.getName()) ) {
				result = e;
				break;
			}
		}
		
		return result;
		
	}

	@Override
	public Element selectElement(FTypeDef typedef, List<Element> elems) {
		
		Element result = null;
		
		for(Element e: elems) {
			if( (e.GetName()+"_Renamed").equals(typedef.getName()) 
					&& e.GetStereotype().equals(EStereoType.ALIAS.getName()) ) {
				result = e;
				break;
			}
		}
		
		return result;
		
	}

}
