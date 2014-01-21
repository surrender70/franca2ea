package org.franca.importer.ea.internal.utils;

import org.franca.importer.ea.internal.utils.EAConstants.EClassType;
import org.franca.importer.ea.internal.utils.EAConstants.EStereoType;
import org.sparx.Collection;
import org.sparx.Element;

public class CElement extends ElementContainer<Element> {


	@Override
	public Collection<Element> getAllElements() {
		
		return container.GetElements();
	}
	
	@Override
	protected Element getElementByName(String name) {
		return container.GetElements().GetByName(name);
	}	

	@Override
	protected Element addElement(String name, EClassType type) {
		return container.GetElements().AddNew(name, type.getName());
	}

	@Override
	public void refresh() {
		container.GetElements().Refresh();
	}

	@Override
	public void update() {
		container.Update();
	}	
}
