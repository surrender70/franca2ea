package org.franca.importer.ea.internal;

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
import org.sparx.Attribute;
import org.sparx.Element;
import org.sparx.Method;
import org.sparx.Package;
import org.sparx.Parameter;

public abstract class ImportProcessorDecorator extends ImportProcessor {

	protected final ImportProcessor processor;
	
	public ImportProcessorDecorator(ImportProcessor lProcessor) {
		processor = lProcessor;
	}
	
	@Override	
	public Package handleModel(Package parent, FModel src, String packageName) {
		return processor.handleModel(parent, src, packageName);
	}

	@Override	
	public Package handleTypeCollection(Package parent, FTypeCollection src) {
		return processor.handleTypeCollection(parent, src);
	}
	
	@Override
	public Element handleInterface(ElementContainer<?> parent, FInterface src) {
		return processor.handleInterface(parent, src);
	}

	@Override
	public Method handleSimpleMethod(Element parent, FMethod src) {
		return processor.handleSimpleMethod(parent, src);
	}

	@Override
	public Method handleBroadcastMethod(Element parent, FBroadcast src) {
		return processor.handleBroadcastMethod(parent, src);
	}

	@Override
	public Parameter handleParameter(Method parent, EParamType direction, FArgument src) {
		return processor.handleParameter(parent, direction, src);
	}

	@Override
	public Element handleStructure(ElementContainer<?> parent, FStructType src) {
		return processor.handleStructure(parent, src);
	}

	@Override
	public Element handleEnumeration(ElementContainer<?> parent, FEnumerationType src) {
		return processor.handleEnumeration(parent, src);
	}
	
	@Override
	public Attribute handleField(Element parent, FField src) {
		return processor.handleField(parent, src);
	}
	
	@Override
	public Attribute handleEnumerator(Element parent, FEnumerator src) {
		return processor.handleEnumerator(parent, src);
	}

	@Override
	public Element handleTypedef(ElementContainer<?> parent, FTypeDef src) {
		return processor.handleTypedef(parent, src);
	}
	
	@Override
	public Element handleArray(ElementContainer<?> parent, FArrayType src) {
		return processor.handleArray(parent, src);
	}

	@Override
	public Element handleUnion(ElementContainer<?> parent, FUnionType src) {
		return processor.handleUnion(parent, src);
	}

	@Override
	public Element handleMap(ElementContainer<?> parent, FMapType src) {
		return processor.handleMap(parent, src);
	}

	@Override
	public Attribute handleMapKey(Element parent, FTypeRef src) {
		return processor.handleMapKey(parent, src);
	}

	@Override
	public Attribute handleMapValue(Element parent, FTypeRef src) {
		return processor.handleMapValue(parent, src);
	}

	@Override
	public Attribute handleAttribute(Element parent, FAttribute src) {
		return processor.handleAttribute(parent, src);
	}

	@Override
	public Parameter handleParameter(Method parent, EParamType direction,
			FEnumerationType src) {
		return processor.handleParameter(parent, direction, src);
	}

	@Override
	public Parameter handleParameter(Method parent, EParamType direction,
			FEnumerator src) {
		return processor.handleParameter(parent, direction, src);
	}
	

}
