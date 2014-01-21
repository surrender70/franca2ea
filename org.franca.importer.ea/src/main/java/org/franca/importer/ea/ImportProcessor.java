package org.franca.importer.ea;

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
import org.franca.core.franca.FTypeCollection;
import org.franca.core.franca.FTypeDef;
import org.franca.core.franca.FTypeRef;
import org.franca.core.franca.FUnionType;
import org.franca.importer.ea.internal.utils.ElementContainer;
import org.franca.importer.ea.internal.utils.EAConstants.EParamType;
import org.sparx.Attribute;
import org.sparx.Element;
import org.sparx.Method;
import org.sparx.Package;
import org.sparx.Parameter;

public abstract class ImportProcessor {

	public abstract Package makePackage(Package parent, FModel src, String packageName) ;
	
	public abstract Package makePackage(Package parent, FTypeCollection src);

	public abstract Element makeInterface(ElementContainer<?> parent, FInterface src) ;
	
	public abstract Method makeSimpleMethod(Element parent, FMethod src) ;
	
	public abstract Method makeBroadcastMethod(Element parent, FBroadcast src) ;
	
	public abstract Attribute makeAttribute(Element parent, FAttribute src);
	
	public abstract Parameter makeParameter(Method parent, EParamType direction, FArgument src) ;
	
	public abstract Parameter makeParameter(Method parent, EParamType direction, FEnumerationType src);

	public abstract Parameter makeParameter(Method parent, EParamType direction, FEnumerator src);

	public abstract Element makeStructure(ElementContainer<?> parent, FStructType src) ;
	
	public abstract Element makeUnion(ElementContainer<?> parent, FUnionType src);
	
	public abstract Attribute makeField(Element parent, FField src);
	
	public abstract Element makeEnumeration(ElementContainer<?> parent, FEnumerationType src) ;
	
	public abstract Attribute makeEnumerator(Element parent, FEnumerator src) ;
	
	public abstract Element makeTypedef(ElementContainer<?> parent, FTypeDef src);
	
	public abstract Element makeArray(ElementContainer<?> parent, FArrayType src);
	
	public abstract Element makeMap(ElementContainer<?> parent, FMapType src);
	
	public abstract Attribute makeMapKey(Element parent, FTypeRef src);

	public abstract Attribute makeMapValue(Element parent, FTypeRef src);
	
	protected String getTypeName(FTypeRef t) {
		
		if(t.getDerived() != null) {
			return t.getDerived().getName();
		}
		else {
			return t.getPredefined().getName();
		}
		
	}	
	
	
	protected String getDescription(FModelElement src) {

		return getDescriptionFromAnnotation(src, FAnnotationType.DESCRIPTION);

	}
	
	protected String getAuthor(FModelElement src) {
		
		return getDescriptionFromAnnotation(src, FAnnotationType.AUTHOR);		
	}
	
	private String getDescriptionFromAnnotation(FModelElement src, FAnnotationType annotationType) {
		
		if(src.getComment() != null) {
			for(FAnnotation fa: src.getComment().getElements()) {
				if(fa.getType().equals(annotationType)) {
					return fa.getComment().trim();
				}
			}
			return "";
		}
		
		else {
			return "";
		}
	}	
		
}
