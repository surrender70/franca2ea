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
	public Package makePackage(Package parent, FModel src, String packageName) {
		return processor.makePackage(parent, src, packageName);
	}

	@Override	
	public Package makePackage(Package parent, FTypeCollection src) {
		return processor.makePackage(parent, src);
	}
	
	@Override
	public Element makeInterface(ElementContainer<?> parent, FInterface src) {
		return processor.makeInterface(parent, src);
	}

	@Override
	public Method makeSimpleMethod(Element parent, FMethod src) {
		return processor.makeSimpleMethod(parent, src);
	}

	@Override
	public Method makeBroadcastMethod(Element parent, FBroadcast src) {
		return processor.makeBroadcastMethod(parent, src);
	}

	@Override
	public Parameter makeParameter(Method parent, EParamType direction, FArgument src) {
		return processor.makeParameter(parent, direction, src);
	}

	@Override
	public Element makeStructure(ElementContainer<?> parent, FStructType src) {
		return processor.makeStructure(parent, src);
	}

	@Override
	public Element makeEnumeration(ElementContainer<?> parent, FEnumerationType src) {
		return processor.makeEnumeration(parent, src);
	}
	
	@Override
	public Attribute makeField(Element parent, FField src) {
		return processor.makeField(parent, src);
	}
	
	@Override
	public Attribute makeEnumerator(Element parent, FEnumerator src) {
		return processor.makeEnumerator(parent, src);
	}

	@Override
	public Element makeTypedef(ElementContainer<?> parent, FTypeDef src) {
		return processor.makeTypedef(parent, src);
	}
	
	@Override
	public Element makeArray(ElementContainer<?> parent, FArrayType src) {
		return processor.makeArray(parent, src);
	}

	@Override
	public Element makeUnion(ElementContainer<?> parent, FUnionType src) {
		return processor.makeUnion(parent, src);
	}

	@Override
	public Element makeMap(ElementContainer<?> parent, FMapType src) {
		return processor.makeMap(parent, src);
	}

	@Override
	public Attribute makeMapKey(Element parent, FTypeRef src) {
		return processor.makeMapKey(parent, src);
	}

	@Override
	public Attribute makeMapValue(Element parent, FTypeRef src) {
		return processor.makeMapValue(parent, src);
	}

	@Override
	public Attribute makeAttribute(Element parent, FAttribute src) {
		return processor.makeAttribute(parent, src);
	}

	@Override
	public Parameter makeParameter(Method parent, EParamType direction,
			FEnumerationType src) {
		return processor.makeParameter(parent, direction, src);
	}

	@Override
	public Parameter makeParameter(Method parent, EParamType direction,
			FEnumerator src) {
		return processor.makeParameter(parent, direction, src);
	}
	

}
