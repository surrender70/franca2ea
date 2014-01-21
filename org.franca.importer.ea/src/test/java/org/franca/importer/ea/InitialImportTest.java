package org.franca.importer.ea;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.franca.core.dsl.FrancaIDLHelpers;
import org.franca.core.franca.FArgument;
import org.franca.core.franca.FArrayType;
import org.franca.core.franca.FAttribute;
import org.franca.core.franca.FBroadcast;
import org.franca.core.franca.FCompoundType;
import org.franca.core.franca.FEnumerationType;
import org.franca.core.franca.FEnumerator;
import org.franca.core.franca.FField;
import org.franca.core.franca.FInterface;
import org.franca.core.franca.FMapType;
import org.franca.core.franca.FMethod;
import org.franca.core.franca.FModel;
import org.franca.core.franca.FStructType;
import org.franca.core.franca.FType;
import org.franca.core.franca.FTypeDef;
import org.franca.core.franca.FTypeRef;
import org.franca.core.franca.FUnionType;
import org.franca.importer.ea.internal.AppearanceCustomizer;
import org.franca.importer.ea.internal.DummyProcessor;
import org.franca.importer.ea.internal.ElementCreator;
import org.franca.importer.ea.internal.Franca2EA;
import org.franca.importer.ea.internal.ImportProcessorDecorator;
import org.franca.importer.ea.internal.NotesUpdater;
import org.franca.importer.ea.internal.utils.EAConstants;
import org.franca.importer.ea.internal.utils.EARepositoryAccessor;
import org.franca.importer.ea.internal.utils.ElementContainer;
import org.franca.importer.ea.internal.utils.EAConstants.EParamType;
import org.franca.importer.ea.internal.utils.EAConstants.EStereoType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sparx.Attribute;
import org.sparx.Element;
import org.sparx.Method;
import org.sparx.Package;
import org.sparx.Parameter;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Names;

public class InitialImportTest {

	private static ImporterFacade facade = null;
	private final static String TEST_PROJECT = "src/test/resources/test.eap";
	private final static String ROOT_PACKAGE = "Model/Logical View";
	private final static String FRANCA_MODEL_PATH = "src/test/resources/fidl";


	@BeforeClass
	public static void setUp() throws Exception {
		
		//Delete project if already exists
		File projectFile = new File(TEST_PROJECT);
		
		if(projectFile.exists()) {
			projectFile.delete();
		}				
		
	}

	@AfterClass
	public static void tearDown() throws Exception {
		facade.tearDown();
	}

	@Test
	public void testElementCreator() {

		Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() { 
				bindConstant().annotatedWith(Names.named("create")).to(true);
			}
			
			@Provides
			ImportProcessor provideImportProcessor() {
				
				ImportProcessor testDecorator = new ImportProcessorDecorator(
						new AppearanceCustomizer(
							new NotesUpdater(	
								new ElementCreator(
									new DummyProcessor())))) {
					
					@Override
					public Element makeUnion(ElementContainer<?> parent, FUnionType src) {
						
						short cntBefore = parent.getAllElements().GetCount();
						
						for(Element e: parent.getAllElements()) {
							assertFalse(e.GetName().equals(src.getName()));
						}
						
						Element result = processor.makeUnion(parent, src);
						
						assertNotNull(result);
										
						assertEquals(src.getName(), result.GetName());
										
						assertTrue(cntBefore+1 == parent.getAllElements().GetCount());
						
						assertEquals(EStereoType.UNION.getName(), result.GetStereotype()); 
						
						return result;
					}
					@Override
					public Element makeStructure(ElementContainer<?> parent, FStructType src) {
						
						short cntBefore = parent.getAllElements().GetCount();
						
						for(Element e: parent.getAllElements()) {
							assertFalse(e.GetName().equals(src.getName()));
						}
						
						Element result = processor.makeStructure(parent, src);
						
						assertNotNull(result);
										
						assertEquals(src.getName(), result.GetName());
										
						assertTrue(cntBefore+1 == parent.getAllElements().GetCount());

						assertEquals(EStereoType.STRUCTURE.getName(), result.GetStereotype()); 
						
						return result;
					}
					
					@Override
					public Method makeSimpleMethod(Element parent, FMethod src) {
						
						for(Method m: parent.GetMethods()) {
							assertFalse(m.GetName().equals(src.getName()));
						}

						Method result = processor.makeSimpleMethod(parent, src);
						
						assertNotNull(result);
						assertEquals(src.getName(), result.GetName());

						assertEquals(EStereoType.METHOD.getName(), result.GetStereotype()); 
						
						return result;
						
					}
					
					@Override
					public Parameter makeParameter(Method parent, EParamType direction,
							FArgument src) {

						for(Parameter p: parent.GetParameters()) {
							assertFalse(p.GetName().equals(src.getName()));
						}

						Parameter result = processor.makeParameter(parent, direction, src);
						
						assertNotNull(result);
						assertEquals(src.getName(), result.GetName());
						
						assertEquals(getTypeName(src.getType()), result.GetType());
										
						assertEquals(direction.getName(), result.GetKind());
						
						return result;
						
					}
					
					@Override
					public Package makePackage(Package parent, FModel src, String packageName) {
										
						Package result = processor.makePackage(parent, src, packageName);
										
						assertNotNull(result);
						
						return result;
					}
					
					@Override
					public Element makeInterface(ElementContainer<?> parent, FInterface src) {

						for(Element e: parent.getAllElements()) {
							assertFalse(e.GetName().equals(src.getName()));
						}

						Element result = processor.makeInterface(parent, src);
						
						assertNotNull(result);
						assertEquals(src.getName(), result.GetName());
						
						if(src.getVersion() != null) {
							assertEquals( 
									src.getVersion().getMajor()+"."+src.getVersion().getMinor(),
									result.GetVersion());
						}
						else {
							assertEquals("", result.GetVersion());
						}
						
						assertEquals(EStereoType.INTERFACE.getName(), result.GetStereotype()); 
										
						return result;				
					}
					
					@Override
					public Attribute makeField(Element parent, FField src) {

						for(Element e: parent.GetElements()) {
							assertFalse(e.GetName().equals(src.getName()));
						}

						Attribute result = processor.makeField(parent, src);
						
						if(src.getType().getDerived() != null) {
							assertFalse(0 == result.GetClassifierID());
						}
						else if(src.getType().getPredefined() != null) {
							assertTrue(0 == result.GetClassifierID());
						}
						
						assertNotNull(result);
						assertEquals(src.getName(), result.GetName());
						assertEquals(getTypeName(src.getType()), result.GetType());
										
						if(EAConstants.KEEP_ORIGINAL_ORDER.equals("true")) {
							int fieldIndex = result.GetPos();
							assertEquals(result.GetName(), ((FCompoundType)src.eContainer()).getElements().get(fieldIndex).getName());
						}
						
						return result;				
					}
					
					@Override
					public Attribute makeEnumerator(Element parent, FEnumerator src) {

						for(Element e: parent.GetElements()) {
							assertFalse(e.GetName().equals(src.getName()));
						}

						Attribute result = processor.makeEnumerator(parent, src);

						assertNotNull(result);
						assertEquals(src.getName(), result.GetName());
						
						if(src.getValue() == null) {
							assertEquals("", result.GetDefault());					
						}
						else {
							assertEquals(src.getValue(), result.GetDefault());
						}
						
						if(EAConstants.KEEP_ORIGINAL_ORDER.equals("true")) {
							int enumerationIndex = result.GetPos();
							assertEquals(result.GetName(), ((FEnumerationType)src.eContainer()).getEnumerators().get(enumerationIndex).getName());
						}
						
						return result;
					}
					
					@Override
					public Element makeEnumeration(ElementContainer<?> parent, FEnumerationType src) {

						for(Element e: parent.getAllElements()) {
							assertFalse(e.GetName().equals(src.getName()));
						}

						Element result = processor.makeEnumeration(parent, src);
										
						assertNotNull(result);
						assertEquals(src.getName(), result.GetName());

						assertEquals(EStereoType.ENUMERATION.getName(), result.GetStereotype()); 
						
						return result;
					}
					
					@Override
					public Method makeBroadcastMethod(Element parent, FBroadcast src) {
						
						for(Method m: parent.GetMethods()) {
							assertFalse(m.GetName().equals(src.getName()));
						}
						
						Method result = processor.makeBroadcastMethod(parent, src);
										
						assertNotNull(result);
						assertEquals(src.getName(), result.GetName());
						
						assertEquals(EStereoType.BROADCAST.getName(), result.GetStereotype()); 

						return result;
						
					}

					@Override
					public Element makeTypedef(ElementContainer<?> parent, FTypeDef src) {
						
						for(Element e: parent.getAllElements()) {
							assertFalse(e.GetName().equals(src.getName()));
						}
						
						Element result = processor.makeTypedef(parent, src);
						
						assertNotNull(result);

						assertTrue(result.GetGenlinks().contains(getTypeName(src.getActualType())));								
						assertEquals(src.getName(), result.GetName());
										
						assertEquals(EStereoType.ALIAS.getName(), result.GetStereotype()); 				
						
						return result;			
					}

					@Override
					public Element makeArray(ElementContainer<?> parent, FArrayType src) {
						
						for(Element e: parent.getAllElements()) {
							assertFalse(e.GetName().equals(src.getName()));
						}
						
						Element result = processor.makeArray(parent, src);
						
						assertNotNull(result);
						
						assertTrue(result.GetGenlinks().contains(getTypeName(src.getElementType())));				
						assertEquals(src.getName(), result.GetName());
										
						assertEquals(EStereoType.ARRAY.getName(), result.GetStereotype()); 
						
						return result;			
					}
					
					@Override
					public Element makeMap(ElementContainer<?> parent, FMapType src) {
						
						short cntBefore = parent.getAllElements().GetCount();
						
						for(Element e: parent.getAllElements()) {
							assertFalse(e.GetName().equals(src.getName()));
						}
						
						Element result = processor.makeMap(parent, src);
						
						assertNotNull(result);
										
						assertEquals(src.getName(), result.GetName());
										
						assertTrue(cntBefore+1 == parent.getAllElements().GetCount());
						
						assertEquals(EStereoType.MAP.getName(), result.GetStereotype()); 
						
						return result;
					}
					
					@Override
					public Attribute makeMapKey(Element parent, FTypeRef src) {
						
						Attribute result = processor.makeMapKey(parent, src);
						
						assertNotNull(result);
						assertEquals(getTypeName(src), result.GetType());
						
						return result;
					}
					
					@Override
					public Attribute makeMapValue(Element parent, FTypeRef src) {
						
						Attribute result = processor.makeMapValue(parent, src);
						
						assertNotNull(result);
						assertEquals(getTypeName(src), result.GetType());				
						
						return result;
					}
					
					@Override
					public Attribute makeAttribute(Element parent, FAttribute src) {
						
						for(Attribute a: parent.GetAttributes()) {
							assertFalse(a.GetName().equals(src.getName()));
						}

						short cntBefore = parent.GetAttributes().GetCount();
						
						Attribute result = processor.makeAttribute(parent, src);
						
						assertNotNull(result);
						assertEquals(src.getName(), result.GetName());
						assertEquals(getTypeName(src.getType()), result.GetType());
						
						assertEquals(EStereoType.ATTRIBUTE.getName(), result.GetStereotype()); 
						
						assertTrue(cntBefore + 1 == parent.GetAttributes().GetCount());
						
						return result;				

					}
					
								
				};		
				
				return testDecorator;
			}
			
		});		
		
		facade = injector.getInstance(ImporterFacade.class);
		
		facade.setup(TEST_PROJECT, ROOT_PACKAGE);
				
		facade.execute(FRANCA_MODEL_PATH);
				
	}

}
