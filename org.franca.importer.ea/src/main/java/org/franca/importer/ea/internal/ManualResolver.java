package org.franca.importer.ea.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.franca.core.franca.FArrayType;
import org.franca.core.franca.FEnumerationType;
import org.franca.core.franca.FStructType;
import org.franca.core.franca.FTypeDef;
import org.franca.core.franca.FUnionType;
import org.franca.importer.ea.IUserFeedbackCallback;
import org.franca.importer.ea.ImportProcessor;
import org.franca.importer.ea.internal.utils.EAConstants;
import org.franca.importer.ea.internal.utils.ElementContainer;
import org.sparx.Element;

import com.google.inject.Inject;

public class ManualResolver extends ImportProcessorDecorator {
	
	private static Logger jlog =  Logger.getLogger(ManualResolver.class.getName());

	@Inject
	private IUserFeedbackCallback callback = null;
	
	public ManualResolver(ImportProcessor lProcessor) {
		super(lProcessor);
	}

	
	@Override
	public Element makeUnion(ElementContainer<?> parent, FUnionType src) {
		
		Element u = processor.makeUnion(parent, src);
		List<Element> availableElements = new ArrayList<Element>();		
		
		if(u == null) {
			for(Element e: parent.getAllElements()) {
				if(e.GetStereotype().equals(EAConstants.EStereoType.UNION.getName())) {
					availableElements.add(e);
				}
			}
			if(availableElements.size() > 0) {
				
				u = callback.selectElement(src, availableElements);
				
				if(u !=  null) {
					jlog.log(Level.WARNING, "Renamed Element " + u.GetName()
							+ " into " + src.getName() + " by user interaction!");
					u.SetName(src.getName());
					u.Update();				
				}			
			}			
		}
				
		return u;
	}


	@Override
	public Element makeStructure(ElementContainer<?> parent, FStructType src) {
		
		Element s = processor.makeStructure(parent, src);
		List<Element> availableElements = new ArrayList<Element>();		
		
		if(s == null) {
			for(Element e: parent.getAllElements()) {
				if(e.GetStereotype().equals(EAConstants.EStereoType.STRUCTURE.getName())) {
					availableElements.add(e);
				}
			}
			if(availableElements.size() > 0) {
				
				s = callback.selectElement(src, availableElements);
				
				if(s !=  null) {
					jlog.log(Level.WARNING, "Renamed Element " + s.GetName()
							+ " into " + src.getName() + " by user interaction!");
					s.SetName(src.getName());
					s.Update();				
				}			
			}			
		}
				
		return s;
	}

	@Override
	public Element makeEnumeration(ElementContainer<?> parent, FEnumerationType src) {
		
		Element enumeration = processor.makeEnumeration(parent, src);		
		List<Element> availableElements = new ArrayList<Element>();		
		
		if(enumeration == null) {
			for(Element e: parent.getAllElements()) {
				if(e.GetStereotype().equals(EAConstants.EStereoType.ENUMERATION.getName())) {
					availableElements.add(e);
				}
			}
			if(availableElements.size() > 0) {
				
				enumeration = callback.selectElement(src, availableElements);
				
				if(enumeration !=  null) {
					jlog.log(Level.WARNING, "Renamed Element " + enumeration.GetName()
							+ " into " + src.getName() + " by user interaction!");
					enumeration.SetName(src.getName());
					enumeration.Update();				
				}			
			}			
		}
				
		return enumeration;

	}

	@Override
	public Element makeTypedef(ElementContainer<?> parent, FTypeDef src) {
		
		Element t = processor.makeTypedef(parent, src);
		List<Element> availableElements = new ArrayList<Element>();		
		
		if(t == null) {
			for(Element e: parent.getAllElements()) {
				if(e.GetStereotype().equals(EAConstants.EStereoType.ALIAS.getName())) {
					availableElements.add(e);
				}
			}
			if(availableElements.size() > 0) {
				
				t = callback.selectElement(src, availableElements);
				
				if(t !=  null) {
					jlog.log(Level.WARNING, "Renamed Element " + t.GetName()
							+ " into " + src.getName() + " by user interaction!");
					t.SetName(src.getName());
					t.Update();				
				}			
			}			
		}
				
		return t;
		
	}

	@Override
	public Element makeArray(ElementContainer<?> parent, FArrayType src) {
		
		Element a = processor.makeArray(parent, src);
		List<Element> availableElements = new ArrayList<Element>();		
		
		if(a == null) {
			for(Element e: parent.getAllElements()) {
				if(e.GetStereotype().equals(EAConstants.EStereoType.ARRAY.getName())) {
					availableElements.add(e);
				}
			}
			if(availableElements.size() > 0) {
				
				a = callback.selectElement(src, availableElements);
				
				if(a !=  null) {
					jlog.log(Level.WARNING, "Renamed Element " + a.GetName()
							+ " into " + src.getName() + " by user interaction!");
					a.SetName(src.getName());
					a.Update();				
				}			
			}			
		}
				
		return a;
	}
		

}
