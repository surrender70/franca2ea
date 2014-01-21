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
import org.sparx.Attribute;
import org.sparx.Element;
import org.sparx.Method;
import org.sparx.Package;
import org.sparx.Parameter;

public class DummyProcessor extends ImportProcessor {
	
	private final static String DUMMY_TRACE_MSG = "Dummy function call";
	private static Logger jlog =  Logger.getLogger(DummyProcessor.class.getName());	

	@Override
	public Package makePackage(Package parent, FModel src, String packageName) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);		
		return null;
	}

	@Override
	public Element makeInterface(ElementContainer<?> parent, FInterface src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Method makeSimpleMethod(Element parent, FMethod src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Method makeBroadcastMethod(Element parent, FBroadcast src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Parameter makeParameter(Method parent, EParamType direction,
			FArgument src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Element makeStructure(ElementContainer<?> parent, FStructType src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Attribute makeField(Element parent, FField src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Element makeEnumeration(ElementContainer<?> parent, FEnumerationType src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Attribute makeEnumerator(Element parent, FEnumerator src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Element makeTypedef(ElementContainer<?> parent, FTypeDef src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Element makeArray(ElementContainer<?> parent, FArrayType src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Element makeUnion(ElementContainer<?> parent, FUnionType src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Element makeMap(ElementContainer<?> parent, FMapType src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Attribute makeMapKey(Element parent, FTypeRef src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Attribute makeMapValue(Element parent, FTypeRef src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Attribute makeAttribute(Element parent, FAttribute src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Package makePackage(Package parent, FTypeCollection src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Parameter makeParameter(Method parent, EParamType direction,
			FEnumerationType src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Parameter makeParameter(Method parent, EParamType direction,
			FEnumerator src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

}
