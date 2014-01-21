package org.franca.importer.ea;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.emf.common.util.URI;
import org.franca.core.franca.FArgument;
import org.franca.core.franca.FArrayType;
import org.franca.core.franca.FAttribute;
import org.franca.core.franca.FBasicTypeId;
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
import org.franca.importer.ea.internal.DummyProcessor;
import org.franca.importer.ea.internal.FindByName;
import org.franca.importer.ea.internal.Franca2EA;
import org.franca.importer.ea.internal.ImportProcessorDecorator;
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

public class FindByNameTest {
	
	private final static String TEST_PROJECT = "src/test/resources/test.eap";
	private final static String ROOT_PACKAGE = "Model/Logical View";
	private final static String TEST_MODEL = "fidl/TypeSortTestModel.fidl";
	private static ImporterFacade facade = null;

	@BeforeClass
	public static void setUp() throws Exception {
		EAImportSuperTest.initialImport(TEST_PROJECT, ROOT_PACKAGE, TEST_MODEL);						
	}

	@AfterClass
	public static void tearDown() throws Exception {
		
		facade.tearDown();
	}
	
	@Test
	public void testSearchByNameRetriever() {
						
		Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() { 
				bindConstant().annotatedWith(Names.named("create")).to(false);				
			}
			
			@Provides
			ImportProcessor provideImportProcessor() {
				
				ImportProcessorDecorator testDecorator = new ImportProcessorDecorator(new FindByName(
						new DummyProcessor())) {
					
					@Override
					public Element makeStructure(ElementContainer<?> parent, FStructType src) {
						
						Element result = processor.makeStructure(parent, src);
						
						assertNotNull(result);
						
						assertEquals(result.GetName(), src.getName());
						
						assertEquals(EStereoType.STRUCTURE.getName(), result.GetStereotype());
						
						return result;
					}

					
					@Override
					public Element makeUnion(ElementContainer<?> parent, FUnionType src) {
						
						Element result = processor.makeUnion(parent, src);
						
						assertNotNull(result);
						
						assertEquals(result.GetName(), src.getName());

						assertEquals(EStereoType.UNION.getName(), result.GetStereotype());

						return result;
					}
					
					
					@Override
					public Method makeSimpleMethod(Element parent, FMethod src) {
						
						Method result = processor.makeSimpleMethod(parent, src);
						
						assertNotNull(result);
						
						assertEquals(result.GetName(), src.getName());
										
						return result;
					}
					
					@Override
					public Parameter makeParameter(Method parent, EParamType direction,
							FArgument src) {
										
						return null;
					}
					
					@Override
					public Package makePackage(Package parent, FModel src, String packageName) {
						
						Package result = processor.makePackage(parent, src, packageName);
						
						assertNotNull(result);
						
						//assertEquals(result.GetName(), src.getName());
										
						return result;
					}
					
					@Override
					public Element makeInterface(ElementContainer<?> parent, FInterface src) {
						
						Element result = processor.makeInterface(parent, src);
						
						assertNotNull(result);
						
						assertEquals(result.GetName(), src.getName());
						
						return result;
					}
					
					@Override
					public Attribute makeField(Element parent, FField src) {
						
						Attribute result = processor.makeField(parent, src);
										
						assertNotNull(result);
						
						assertEquals(result.GetName(), src.getName());
						
						assertEquals(getTypeName(src.getType()), result.GetType());
			
						return result;
					}
					
					@Override
					public Attribute makeEnumerator(Element parent, FEnumerator src) {
						
						Attribute result = processor.makeEnumerator(parent, src);
						
						assertNotNull(result);
						
						assertEquals(result.GetName(), src.getName());
						
						return result;
					}
					
					@Override
					public Element makeEnumeration(ElementContainer<?> parent, FEnumerationType src) {
						
						Element result = processor.makeEnumeration(parent, src);
						
						assertNotNull(result);
						
						assertEquals(result.GetName(), src.getName());
						
						assertEquals(EStereoType.ENUMERATION.getName(), result.GetStereotype());

						return result;
					}
					
					@Override
					public Method makeBroadcastMethod(Element parent, FBroadcast src) {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public Element makeTypedef(ElementContainer<?> parent, FTypeDef src) {
						
						Element result = processor.makeTypedef(parent, src);
						
						assertNotNull(result);
										
						assertEquals(result.GetName(), src.getName());
						
						assertEquals(EStereoType.ALIAS.getName(), result.GetStereotype());
						
						return result;			
					}

					@Override
					public Element makeArray(ElementContainer<?> parent, FArrayType src) {
						
						Element result = processor.makeArray(parent, src);
						
						assertNotNull(result);
						
						assertEquals(result.GetName(), src.getName());
						
						assertEquals(EStereoType.ARRAY.getName(), result.GetStereotype());

						return result;			
					}

					@Override
					public Element makeMap(ElementContainer<?> parent, FMapType src) {
						
						Element result = processor.makeMap(parent, src);
						
						assertNotNull(result);
						
						assertEquals(result.GetName(), src.getName());
						
						assertEquals(EStereoType.MAP.getName(), result.GetStereotype());
						
						return result;
					}

					@Override
					public Attribute makeMapKey(Element parent, FTypeRef src) {
									    				
						return null;
					}

					@Override
					public Attribute makeMapValue(Element parent, FTypeRef src) {
						
						return null;
					}

					@Override
					public Attribute makeAttribute(Element parent, FAttribute src) {
						
						Attribute result = processor.makeAttribute(parent, src);

						assertNotNull(result);
						
						assertEquals(result.GetName(), src.getName());

						return result;

					}
															
				};
					
				return testDecorator;
			}
			
		});
		
		facade = injector.getInstance(ImporterFacade.class);
		
		facade.setup(TEST_PROJECT, ROOT_PACKAGE);

		facade.execute(URI.createURI("classpath:/"), URI.createFileURI(TEST_MODEL));
								
	}
	
	
}
