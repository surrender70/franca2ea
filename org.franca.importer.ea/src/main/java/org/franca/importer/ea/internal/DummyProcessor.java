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
	public Package handleModel(Package parent, FModel src, String packageName) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);		
		return null;
	}

	@Override
	public Element handleInterface(ElementContainer<?> parent, FInterface src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Method handleSimpleMethod(Element parent, FMethod src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Method handleBroadcastMethod(Element parent, FBroadcast src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Parameter handleParameter(Method parent, EParamType direction,
			FArgument src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Element handleStructure(ElementContainer<?> parent, FStructType src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Attribute handleField(Element parent, FField src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Element handleEnumeration(ElementContainer<?> parent, FEnumerationType src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Attribute handleEnumerator(Element parent, FEnumerator src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Element handleTypedef(ElementContainer<?> parent, FTypeDef src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Element handleArray(ElementContainer<?> parent, FArrayType src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Element handleUnion(ElementContainer<?> parent, FUnionType src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Element handleMap(ElementContainer<?> parent, FMapType src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Attribute handleMapKey(Element parent, FTypeRef src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Attribute handleMapValue(Element parent, FTypeRef src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Attribute handleAttribute(Element parent, FAttribute src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Package handleTypeCollection(Package parent, FTypeCollection src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Parameter handleParameter(Method parent, EParamType direction,
			FEnumerationType src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

	@Override
	public Parameter handleParameter(Method parent, EParamType direction,
			FEnumerator src) {
		jlog.log(Level.FINE, DUMMY_TRACE_MSG);
		return null;
	}

}
