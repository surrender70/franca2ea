package org.franca.importer.ea.internal;

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

public class NotesUpdater extends ImportProcessorDecorator {

	public NotesUpdater(ImportProcessor provider) {
		super(provider);
	}

	@Override
	public Package handleModel(Package parent, FModel src,
			String packageName) {
				
		return processor.handleModel(parent, src, packageName);
		
	}

	@Override
	public Element handleInterface(ElementContainer<?> parent, FInterface src) {
		
		Element e = processor.handleInterface(parent, src);
		
		e.SetNotes(getDescription(src));
		e.Update();
		
		return e;
	}

	@Override
	public Method handleSimpleMethod(Element parent, FMethod src) {
				
		Method m = processor.handleSimpleMethod(parent, src);
		
		m.SetNotes(getDescription(src));
		m.Update();
		
		return m;
	}

	@Override
	public Method handleBroadcastMethod(Element parent, FBroadcast src) {

		Method m = processor.handleBroadcastMethod(parent, src);
		
		m.SetNotes(getDescription(src));
		m.Update();
		
		return m;

	}

	@Override
	public Parameter handleParameter(Method parent, EParamType direction,
			FArgument src) {
						
		Parameter p = processor.handleParameter(parent, direction, src);
		
		p.SetNotes(getDescription(src));
		p.Update();
		
		return p;		
		
	}

	@Override
	public Parameter handleParameter(Method parent, EParamType direction,
			FEnumerationType src) {
		
		Parameter p = processor.handleParameter(parent, direction, src);
		
		p.SetNotes(getDescription(src));
		p.Update();
		
		return p;		
	}	
	
	@Override
	public Element handleStructure(ElementContainer<?> parent, FStructType src) {

		Element s = processor.handleStructure(parent, src);
				
		s.SetNotes(getDescription(src));		
		s.SetAuthor(getAuthor(src));
		s.Update();
		
		return s;
	}

	@Override
	public Element handleUnion(ElementContainer<?> parent, FUnionType src) {

		Element s = processor.handleUnion(parent, src);
				
		s.SetNotes(getDescription(src));		
		s.SetAuthor(getAuthor(src));
		s.Update();
		
		return s;
	}
	
	
	@Override
	public Element handleEnumeration(ElementContainer<?> parent, FEnumerationType src) {
		
		Element e = processor.handleEnumeration(parent, src);
		
		e.SetNotes(getDescription(src));
		e.Update();
		
		return e;
	}

	@Override
	public Attribute handleField(Element parent, FField src) {
		
		Attribute f = processor.handleField(parent, src);
		
		f.SetNotes(getDescription(src));
		f.Update();
		
		return f;
	}

	@Override
	public Attribute handleEnumerator(Element parent, FEnumerator src) {				
		
		Attribute e = processor.handleEnumerator(parent, src);
				
		e.SetNotes(getDescription(src));
		e.Update();
		
		return e;
	}

	@Override
	public Element handleTypedef(ElementContainer<?> parent, FTypeDef src) {
		
		Element e = processor.handleTypedef(parent, src);
		
		e.SetNotes(getDescription(src));
		e.Update();
		
		return e;
	}

	@Override
	public Element handleArray(ElementContainer<?> parent, FArrayType src) {
		
		Element e = processor.handleArray(parent, src);
		
		e.SetNotes(getDescription(src));
		e.Update();
		
		return e;
	}

	@Override
	public Element handleMap(ElementContainer<?> parent, FMapType src) {
		
		Element s = processor.handleMap(parent, src);
		
		s.SetNotes(getDescription(src));		
		s.SetAuthor(getAuthor(src));
		s.Update();
		
		return s;
		
	}


	@Override
	public Attribute handleAttribute(Element parent, FAttribute src) {
		
		Attribute attr = processor.handleAttribute(parent, src);
		
		attr.SetNotes(getDescription(src));
		attr.Update();
		
		return attr;
	}


	
	
}
