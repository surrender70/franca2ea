package org.franca.importer.ea;

import java.util.List;

import org.franca.core.franca.FArrayType;
import org.franca.core.franca.FEnumerationType;
import org.franca.core.franca.FStructType;
import org.franca.core.franca.FTypeDef;
import org.franca.core.franca.FUnionType;
import org.sparx.Attribute;
import org.sparx.Element;

public interface IUserFeedbackCallback {
	
	public Element selectElement(FStructType struct, List<Element> elems);
	
	public Element selectElement(FUnionType union, List<Element> elems);

	public Element selectElement(FEnumerationType enumeration, List<Element> elems);

	public Element selectElement(FArrayType array, List<Element> elems);

	public Element selectElement(FTypeDef typedef, List<Element> elems);

	
	public int selectAttribute(String title, List<Attribute> attrs);
	
}
