package org.franca.importer.ea;

import java.util.List;

import org.franca.core.franca.FArrayType;
import org.franca.core.franca.FEnumerationType;
import org.franca.core.franca.FStructType;
import org.franca.core.franca.FTypeDef;
import org.franca.core.franca.FUnionType;
import org.sparx.Attribute;
import org.sparx.Element;

/**
 * 
 * This interface should be used in case of the importer will not create or overwrite and exisiting project,
 * but tries to merge/update an existing UML model with changed/adapted franca models
 * These are callbacks which are used in the special import processor ManualResolver
 * 
 * This is not yet complete, but a basic idea to resolve merge conflicts with user interaction
 * 
 * @author TMosis
 *
 */
public interface ManualResolveCallback {
	
	/**
	 * A callback method to select a certain Element by a given list of selection possibilities
	 * @param struct The franca structure that cannot be merged automatically with an already existing EA element
	 * @param elems Possible proposals of EA elements that could be selected
	 * @return The selected Element if reasonable, null otherwise
	 */
	public Element selectElement(FStructType struct, List<Element> elems);
	
	/**
	 * A callback method to select a certain Element by a given list of selection possibilities
	 * @param union The franca union that cannot be merged automatically with an already existing EA element
	 * @param elems Possible proposals of EA elements that could be selected
	 * @return The selected Element if reasonable, null otherwise
	 */
	public Element selectElement(FUnionType union, List<Element> elems);

	/**
	 * A callback method to select a certain Element by a given list of selection possibilities
	 * @param enumeration The franca enumeration that cannot be merged automatically with an already existing EA element
	 * @param elems Possible proposals of EA elements that could be selected
	 * @return The selected Element if reasonable, null otherwise
	 */
	public Element selectElement(FEnumerationType enumeration, List<Element> elems);

	/**
	 * A callback method to select a certain Element by a given list of selection possibilities
	 * @param array The franca array that cannot be merged automatically with an already existing EA element
	 * @param elems Possible proposals of EA elements that could be selected
	 * @return The selected Element if reasonable, null otherwise
	 */
	public Element selectElement(FArrayType array, List<Element> elems);

	/**
	 * A callback method to select a certain Element by a given list of selection possibilities
	 * @param typedef The franca typedef that cannot be merged automatically with an already existing EA element
	 * @param elems Possible proposals of EA elements that could be selected
	 * @return The selected Element if reasonable, null otherwise
	 */
	public Element selectElement(FTypeDef typedef, List<Element> elems);
		
}
