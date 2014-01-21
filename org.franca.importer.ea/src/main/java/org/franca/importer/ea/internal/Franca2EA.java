package org.franca.importer.ea.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.util.EcoreUtil;
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
import org.franca.core.franca.FType;
import org.franca.core.franca.FTypeCollection;
import org.franca.core.franca.FTypeDef;
import org.franca.core.franca.FUnionType;
import org.franca.core.franca.FrancaFactory;
import org.franca.importer.ea.ImportProcessor;
import org.franca.importer.ea.internal.utils.CElement;
import org.franca.importer.ea.internal.utils.CPackage;
import org.franca.importer.ea.internal.utils.EAConstants;
import org.franca.importer.ea.internal.utils.EARepositoryAccessor;
import org.franca.importer.ea.internal.utils.ElementContainer;
import org.franca.importer.ea.internal.utils.FTypeSorter;
import org.franca.importer.ea.internal.utils.SortException;
import org.franca.importer.ea.internal.utils.EAConstants.EParamType;
import org.franca.importer.ea.internal.utils.EAConstants.EStereoType;
import org.sparx.Attribute;
import org.sparx.Element;
import org.sparx.Method;
import org.sparx.Package;
import org.sparx.Parameter;

import com.google.inject.Inject;

public class Franca2EA {

	final private ImportProcessor processor;
	
	private CElement parentIsElement = new CElement();
	
	private CPackage parentIsPackage = new CPackage();
	
	@Inject
	private Logger jlog;

	@Inject
	public Franca2EA(ImportProcessor processor) {
		this.processor = processor;
	}

	public Package processRootPackage(Package parent, FModel src) {
		
		String containerName = src.eResource().getURI().lastSegment().split("\\.")[0];
		
		Package p = processor
				.makePackage(parent, src, containerName);
		parentIsPackage.setContainer(p);
		
		// TODO
		// Check if the stereotype could be set implicitly, while calling malePackage  
		p.GetElement().SetStereotype(EStereoType.FIDLFILE.getName());
		p.Update();
		
		// Check if the broadcast interfaces are configured to be own interfaces
		if(EAConstants.GENERATE_CLIENTINTEFACE.equals("true")) {
			
			List<FInterface> clientInterfaces = new ArrayList<FInterface>();
			FInterface clientInterface = null;
			
			if(EAConstants.GENERATE_BROADCAST.equals("true")) {
				for (FInterface fi : src.getInterfaces()) {
					if(fi.getBroadcasts().size() > 0) {
						clientInterface = FrancaFactory.eINSTANCE.createFInterface();
						for(FBroadcast b: fi.getBroadcasts()) {
							FBroadcast copy = EcoreUtil.copy(b);
							clientInterface.getBroadcasts().add(copy);
						}
						fi.getBroadcasts().clear();
						clientInterface.setName(fi.getName()+EAConstants.CLIENTINTERFACE_SUFFIX);
						clientInterface.setVersion(EcoreUtil.copy(fi.getVersion()));
//						clientInterfaces.add(clientInterface);
					}
				}							
			}

			if(EAConstants.GENERATE_REQUEST_RESPONSE_PAIR.equals("true")) {
				for (FInterface fi : src.getInterfaces()) {
					for(FMethod fm : fi.getMethods()) {
						if(fm.getOutArgs().size() > 0) {
							FMethod clientMethod = EcoreUtil.copy(fm);
							clientMethod.getInArgs().clear();
							fm.getOutArgs().clear();
							if(clientInterface == null) {
								clientInterface = FrancaFactory.eINSTANCE.createFInterface();							
								clientInterface.setName(fi.getName()+EAConstants.CLIENTINTERFACE_SUFFIX);
								clientInterface.setVersion(EcoreUtil.copy(fi.getVersion()));
							}
							clientInterface.getMethods().add(clientMethod);
						}
					}					
				}					
			}
			
			if(EAConstants.GENERATE_ATTRIBUTE_METHOD.equals("true")) {
				for (FInterface fi : src.getInterfaces()) {
					if(fi.getAttributes().size() > 0) {
						if(clientInterface == null) {
							clientInterface = FrancaFactory.eINSTANCE.createFInterface();							
							clientInterface.setName(fi.getName()+EAConstants.CLIENTINTERFACE_SUFFIX);
							clientInterface.setVersion(EcoreUtil.copy(fi.getVersion()));
						}
						for(FAttribute fa: fi.getAttributes()) {
							FMethod attrMethod = FrancaFactory.eINSTANCE.createFMethod();
							FArgument arg = FrancaFactory.eINSTANCE.createFArgument();
							arg.setName("value");
							arg.setType(EcoreUtil.copy(fa.getType()));
							attrMethod.setName("on"+fa.getName()+"Update");
							attrMethod.getOutArgs().add(arg);
							clientInterface.getMethods().add(attrMethod);
						}
					}
				}				
			}
			
			if(clientInterface != null) {
				clientInterfaces.add(clientInterface);
			}

//			
//			if(clientInterface != null) {
//				src.getInterfaces().add(clientInterface);
//			}	
			
			if(clientInterfaces.size() > 0) {
				for(FInterface c: clientInterfaces) {
					src.getInterfaces().add(c);
				}
				clientInterfaces.clear();
			}
			
			
		}
		// ************
		
		for (FInterface fi : src.getInterfaces()) {					
			transformInterface(parentIsPackage, fi);
		}
		
		for(FTypeCollection typeCollection: src.getTypeCollections()) {
						
			Package typeCollectionPackage = processor.makePackage(p, typeCollection);
			
			parentIsPackage.setContainer(typeCollectionPackage);
			
			FTypeSorter typeDependencyResolver = new FTypeSorter(typeCollection.getTypes());
			try {
				typeDependencyResolver.sort();
			} catch (SortException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			for (FType ft : typeDependencyResolver.getSorted()) {
				Element e = transformType(parentIsPackage, ft);
				jlog.log(Level.INFO, "Transformed Element: "+e.GetName()+" "+e.GetElementID());
			}
			
			//TODO
			// Create Type Diagram for sub-package
			EARepositoryAccessor.INSTANCE.createPackageDiagram(typeCollectionPackage);
		}
		EARepositoryAccessor.INSTANCE.createPackageDiagram(p);
		
		return p;
	}

	private Element transformType(ElementContainer<?> parent, FType type) {

		Element e = null;
		
		if (type instanceof FEnumerationType) {
			e = transformEnumeration(parent, (FEnumerationType) type);
		} else if (type instanceof FStructType) {
			e = transformStructure(parent, (FStructType) type);
		} else if (type instanceof FUnionType) {
			e = transformUnion(parent, (FUnionType) type);
		} else if (type instanceof FTypeDef) {
			e = transformTypedef(parent, (FTypeDef) type);
		} else if(type instanceof FArrayType) {
			e = transformArray(parent, (FArrayType) type);
		} else if(type instanceof FMapType) {
			e = transformMap(parent, (FMapType) type);
		} else {
			jlog.log(Level.SEVERE, "Unknown Datatype: "+type.toString());
		}
				
		return e;
	}

	private Element transformMap(ElementContainer<?> parent, FMapType type) {

		Element map = processor.makeMap(parent, type);
		
		processor.makeMapKey(map, type.getKeyType());
		
		processor.makeMapValue(map, type.getValueType());
		
		return map;		
	}

	private Element transformInterface(ElementContainer<?> parent, FInterface src) {

		Element p = processor.makeInterface(parent, src);
		parentIsElement.setContainer(p);
		
		if(src.getTypes().size() > 0) {
			
			FTypeSorter typeDependencyResolver = new FTypeSorter(src.getTypes());
			try {
				typeDependencyResolver.sort();
			} catch (SortException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
			
			for (FType ft : typeDependencyResolver.getSorted()) {
				Element e = transformType(parentIsElement, ft);
				jlog.log(Level.INFO, "Transformed Element: "+e.GetName()+" "+e.GetElementID());
			}
			
		}
		for(FAttribute a: src.getAttributes()) {
			transformAttribute(p, a);
		}
		
		for (FMethod m : src.getMethods()) {
			transformMethod(p, m);
		}

		for (FBroadcast b : src.getBroadcasts()) {
			transformMethod(p, b);
		}
				
		p.Refresh();

		return p;
	}

	private Attribute transformAttribute(Element parent, FAttribute src) {
		
		Attribute a = processor.makeAttribute(parent, src);
		
		return a;
	}
	
	private Method transformMethod(Element parent, FMethod src) {

		Method m = processor.makeSimpleMethod(parent, src);
		
		for (FArgument in : src.getInArgs()) {
			transformParameter(m, EParamType.IN, in);
		}

		for (FArgument out : src.getOutArgs()) {
			transformParameter(m, EParamType.OUT, out);
		}
				
		if(src.getErrorEnum() != null) {
			processor.makeParameter(m, EParamType.OUT, src.getErrorEnum());			
		}
		
		else if(src.getErrors() != null) {
			processor.makeParameter(m, EParamType.OUT, src.getErrors());			
			for(FEnumerator e: src.getErrors().getEnumerators()) {
				processor.makeParameter(m, EParamType.OUT, e);
			}			
		}
				
		return m;
	}

	private Method transformMethod(Element parent, FBroadcast src) {

		Method m = processor.makeBroadcastMethod(parent, src);

		for (FArgument out : src.getOutArgs()) {
			transformParameter(m, EParamType.OUT, out);
		}

		return m;

	}

	private Parameter transformParameter(Method parent, EParamType direction,
			FArgument src) {

		Parameter p = processor.makeParameter(parent, direction, src);

		return p;

	}

	private Element transformTypedef(ElementContainer<?> parent, FTypeDef src) {
		
		return processor.makeTypedef(parent, src);		
	}

	private Element transformArray(ElementContainer<?> parent, FArrayType src) {
		
		return processor.makeArray(parent, src);		
	}

	private Element transformStructure(ElementContainer<?> parent, FStructType src) {

		Element e = processor.makeStructure(parent, src);

		for (FField field : src.getElements()) {
			transformField(e, field);
		}
				
		return e;
	}

	private Element transformUnion(ElementContainer<?> parent, FUnionType src) {

		Element e = processor.makeUnion(parent, src);

		for (FField field : src.getElements()) {
			transformField(e, field);
		}
		
		return e;
	}	
	
	private Attribute transformField(Element parent, FField src) {
		
		if(src.getType().getDerived() != null) {
			Element result = EARepositoryAccessor.INSTANCE.findElement(src.getType().getDerived().getName());
			if(result == null) {
				if(src.getType().getDerived().eResource() != null) {
					jlog.log(Level.WARNING, "Unknown element: "+src.getType().getDerived().getName()+" "+
							src.getType().getDerived().eResource().toString());					
				}
				else {
					jlog.log(Level.WARNING, "Unknown eResource for: "+src.getType().getDerived().getName());										
				}								
			}
		}
		
		return processor.makeField(parent, src);

	}

	private Element transformEnumeration(ElementContainer<?> parent, FEnumerationType src) {

		Element e = processor.makeEnumeration(parent, src);

		for (FEnumerator eLiteral : src.getEnumerators()) {
			transformEnumerator(e, eLiteral);
		}

		return e;
	}

	private Attribute transformEnumerator(Element parent, FEnumerator src) {

		return processor.makeEnumerator(parent, src);

	}


}
