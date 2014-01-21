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
	public Package makePackage(Package parent, FModel src,
			String packageName) {
				
		return processor.makePackage(parent, src, packageName);
		
	}

	@Override
	public Element makeInterface(ElementContainer<?> parent, FInterface src) {
		
		Element e = processor.makeInterface(parent, src);
		
		e.SetNotes(getDescription(src));
		e.Update();
		
		return e;
	}

	@Override
	public Method makeSimpleMethod(Element parent, FMethod src) {
				
		Method m = processor.makeSimpleMethod(parent, src);
		
		m.SetNotes(getDescription(src));
		m.Update();
		
		return m;
	}

	@Override
	public Method makeBroadcastMethod(Element parent, FBroadcast src) {

		Method m = processor.makeBroadcastMethod(parent, src);
		
		m.SetNotes(getDescription(src));
		m.Update();
		
		return m;

	}

	@Override
	public Parameter makeParameter(Method parent, EParamType direction,
			FArgument src) {
						
		Parameter p = processor.makeParameter(parent, direction, src);
		
		p.SetNotes(getDescription(src));
		p.Update();
		
		return p;		
		
	}

	@Override
	public Parameter makeParameter(Method parent, EParamType direction,
			FEnumerationType src) {
		
		Parameter p = processor.makeParameter(parent, direction, src);
		
		p.SetNotes(getDescription(src));
		p.Update();
		
		return p;		
	}	
	
	@Override
	public Element makeStructure(ElementContainer<?> parent, FStructType src) {

		Element s = processor.makeStructure(parent, src);
				
		s.SetNotes(getDescription(src));		
		s.SetAuthor(getAuthor(src));
		s.Update();
		
		return s;
	}

	@Override
	public Element makeUnion(ElementContainer<?> parent, FUnionType src) {

		Element s = processor.makeUnion(parent, src);
				
		s.SetNotes(getDescription(src));		
		s.SetAuthor(getAuthor(src));
		s.Update();
		
		return s;
	}
	
	
	@Override
	public Element makeEnumeration(ElementContainer<?> parent, FEnumerationType src) {
		
		Element e = processor.makeEnumeration(parent, src);
		
		e.SetNotes(getDescription(src));
		e.Update();
		
		return e;
	}

	@Override
	public Attribute makeField(Element parent, FField src) {
		
		Attribute f = processor.makeField(parent, src);
		
		f.SetNotes(getDescription(src));
		f.Update();
		
		return f;
	}

	@Override
	public Attribute makeEnumerator(Element parent, FEnumerator src) {				
		
		Attribute e = processor.makeEnumerator(parent, src);
				
		e.SetNotes(getDescription(src));
		e.Update();
		
		return e;
	}

	@Override
	public Element makeTypedef(ElementContainer<?> parent, FTypeDef src) {
		
		Element e = processor.makeTypedef(parent, src);
		
		e.SetNotes(getDescription(src));
		e.Update();
		
		return e;
	}

	@Override
	public Element makeArray(ElementContainer<?> parent, FArrayType src) {
		
		Element e = processor.makeArray(parent, src);
		
		e.SetNotes(getDescription(src));
		e.Update();
		
		return e;
	}

	@Override
	public Element makeMap(ElementContainer<?> parent, FMapType src) {
		
		Element s = processor.makeMap(parent, src);
		
		s.SetNotes(getDescription(src));		
		s.SetAuthor(getAuthor(src));
		s.Update();
		
		return s;
		
	}


	@Override
	public Attribute makeAttribute(Element parent, FAttribute src) {
		
		Attribute attr = processor.makeAttribute(parent, src);
		
		attr.SetNotes(getDescription(src));
		attr.Update();
		
		return attr;
	}


	
	
}
