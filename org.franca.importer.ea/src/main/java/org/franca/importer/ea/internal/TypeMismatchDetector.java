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
import org.franca.core.franca.FTypeDef;
import org.franca.core.franca.FTypeRef;
import org.franca.core.franca.FUnionType;
import org.franca.importer.ea.ImportProcessor;
import org.sparx.Attribute;
import org.sparx.Element;
import org.sparx.Method;
import org.sparx.Package;
import org.sparx.Parameter;

public class TypeMismatchDetector extends ImportProcessorDecorator {
	
	private static Logger jlog =  Logger.getLogger("de.wikibooks");
	
	public TypeMismatchDetector(ImportProcessor provider) {
		super(provider);
	}
	

	@Override
	public Attribute makeField(Element parent, FField src) {				
		
		Attribute a = processor.makeField(parent, src);
		
		if(!a.GetType().equals(getTypeName(src.getType()))) {
			
			jlog.log(Level.WARNING, "Type Mismatch for element "+a.GetName());
			
			a.SetType(getTypeName(src.getType()));
			a.Update();
		}
		
		return a;
	}


	@Override
	public Attribute makeMapKey(Element parent, FTypeRef src) {
		
		Attribute a = processor.makeMapKey(parent, src);
		
		if(!a.GetType().equals(getTypeName(src))) {
			
			jlog.log(Level.WARNING, "Type Mismatch for Map Key ");
			
			a.SetType(getTypeName(src));
			a.Update();
		}
		
		return a;
		
	}

	@Override
	public Attribute makeMapValue(Element parent, FTypeRef src) {
		
		Attribute a = processor.makeMapKey(parent, src);
		
		if(!a.GetType().equals(getTypeName(src))) {
			
			jlog.log(Level.WARNING, "Type Mismatch for Map Value ");
			
			a.SetType(getTypeName(src));
			a.Update();
		}
		
		return a;
		
	}

	@Override
	public Attribute makeAttribute(Element parent, FAttribute src) {
		
		Attribute a = processor.makeAttribute(parent, src);
		
		if(!a.GetType().equals(getTypeName(src.getType()))) {
			
			jlog.log(Level.WARNING, "Type Mismatch for element "+a.GetName());
			
			a.SetType(getTypeName(src.getType()));
			a.Update();
		}
		
		return a;
	}
	
	
}