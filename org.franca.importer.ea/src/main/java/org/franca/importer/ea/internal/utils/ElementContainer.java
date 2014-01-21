package org.franca.importer.ea.internal.utils;

import org.franca.importer.ea.internal.utils.EAConstants.EClassType;
import org.franca.importer.ea.internal.utils.EAConstants.EStereoType;
import org.sparx.Collection;
import org.sparx.Element;

public abstract class ElementContainer<T> {

	protected T container = null;
	
	public final Element makeElement(String name, EStereoType stereoType, 
			EClassType type, String genlinks) {
		
		Element e = addElement(name, type);
		e.SetStereotype(stereoType.getName());
		e.SetGentype(EAConstants.LANGUAGE_TYPE);
		e.SetGenlinks(genlinks);
		
		e.SetTreePos(getAllElements().GetCount());
		
		e.Update();
		refresh();
		update();
		return e;		
		
	}

	public final Element findElementByName(String name, EStereoType stereoType) {
		
		try {
			Element e = getElementByName(name);
			if(e != null && e.GetStereotype().equals(stereoType.getName())) {
				return e;
			}
			else {
				return null;
			}
		}
		catch(Exception e) {
			return null;
		}		
	}
	
	public abstract Collection<Element> getAllElements();
	
	public void setContainer(T _container) {		
		container = _container;
	}
	
	protected abstract Element getElementByName(String name);
	
	protected abstract Element addElement(String name, EClassType type);
	
	public abstract void refresh();
	
	public abstract void update();
}
