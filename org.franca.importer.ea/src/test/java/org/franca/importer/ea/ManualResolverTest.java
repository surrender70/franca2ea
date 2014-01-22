package org.franca.importer.ea;

import static org.junit.Assert.assertNotNull;

import org.eclipse.emf.common.util.URI;
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
import org.franca.core.franca.FTypeDef;
import org.franca.core.franca.FTypeRef;
import org.franca.core.franca.FUnionType;
import org.franca.importer.ea.internal.DummyProcessor;
import org.franca.importer.ea.internal.ElementCreator;
import org.franca.importer.ea.internal.FindByName;
import org.franca.importer.ea.internal.Franca2EA;
import org.franca.importer.ea.internal.ImportProcessorDecorator;
import org.franca.importer.ea.internal.ImporterFacadeImpl;
import org.franca.importer.ea.internal.ManualResolver;
import org.franca.importer.ea.internal.utils.EARepositoryAccessor;
import org.franca.importer.ea.internal.utils.ElementContainer;
import org.franca.importer.ea.internal.utils.EAConstants.EParamType;
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

public class ManualResolverTest {

	
	private final static String TEST_PROJECT = "src/test/resources/test.eap";
	private final static String ROOT_PACKAGE = "Model/Logical View";
	private final static String TEST_MODEL = "fidl/TypeSortTestModel.fidl";
	private static ImporterFacade facade = null;

	private String rename(String name) {
		return name + "_Renamed";
	}

	@BeforeClass
	public static void setUp() throws Exception {
		
		EAImportSuperTest.initialImport(TEST_PROJECT, ROOT_PACKAGE, TEST_MODEL);
		
	}

	@AfterClass
	public static void tearDown() throws Exception {
		facade.tearDown();
	}

	@Test
	public void testFindRefactorings() {

		Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() { 
				bind(ManualResolveCallback.class).to(MockManualResolverOk.class);
				bindConstant().annotatedWith(Names.named("create")).to(true);
				bind(ImporterFacade.class).to(ImporterFacadeImpl.class);
			}
			
			@Provides
			ImportProcessor provideImportProcessor() {
				
				ImportProcessor testDecorator = 
					new ElementCreator(
						new ImportProcessorDecorator(
							new ManualResolver(
								new FindByName(
									new DummyProcessor()))) {

							@Override
							public Element handleStructure(ElementContainer<?> parent, FStructType src) {

								Element result = processor.handleStructure(parent, src);
								
								assertNotNull(result);
								
								return result;
								
							}

							@Override
							public Element handleUnion(ElementContainer<?> parent, FUnionType src) {

								Element result = processor.handleUnion(parent, src);
								
								assertNotNull(result);
								
								return result;
								
							}
							
							
							@Override
							public Method handleSimpleMethod(Element parent, FMethod src) {

								return processor.handleSimpleMethod(parent, src);
							}

							@Override
							public Parameter handleParameter(Method parent,
									EParamType direction, FArgument src) {
								// TODO Auto-generated method stub
								return null;
							}

							@Override
							public Package handleModel(Package parent, FModel src,
									String packageName) {
								// TODO Auto-generated method stub
								return processor.handleModel(parent, src, packageName);
							}

							@Override
							public Element handleInterface(ElementContainer<?> parent, FInterface src) {

								return processor.handleInterface(parent, src);
							}

							@Override
							public Attribute handleField(Element parent, FField src) {
								// TODO Auto-generated method stub
								return processor.handleField(parent, src);
							}

							@Override
							public Attribute handleEnumerator(Element parent,
									FEnumerator src) {
								// TODO Auto-generated method stub
								return processor.handleEnumerator(parent, src);
							}

							@Override
							public Element handleEnumeration(ElementContainer<?> parent,
									FEnumerationType src) {

								return processor.handleEnumeration(parent, src);
							}

							@Override
							public Method handleBroadcastMethod(Element parent,
									FBroadcast src) {
								// TODO Auto-generated method stub
								return null;
							}

							@Override
							public Element handleTypedef(ElementContainer<?> parent, FTypeDef src) {

								return processor.handleTypedef(parent, src);
							}

							@Override
							public Element handleArray(ElementContainer<?> parent, FArrayType src) {

								return processor.handleArray(parent, src);
							}

							@Override
							public Element handleMap(ElementContainer<?> parent, FMapType src) {
								
								return processor.handleMap(parent, src);
							}

							@Override
							public Attribute handleMapKey(Element parent, FTypeRef src) {
								
								return processor.handleMapKey(parent, src);
							}

							@Override
							public Attribute handleMapValue(Element parent, FTypeRef src) {
								
								return processor.handleMapValue(parent, src);
							}

							@Override
							public Attribute handleAttribute(Element parent,
									FAttribute src) {
								return processor.handleAttribute(parent, src);
							}
							
							
							
						});
				
				return testDecorator;
			}
						
		} );
		
		//FIXME
//		if(fmodel.getTypeCollections().size() > 0) {
//			for (FType f : fmodel.getTypeCollections().get(0).getTypes()) {
//				f.setName(rename(f.getName()));
//			}
//		}

		facade = injector.getInstance(ImporterFacade.class);
		
		facade.setup(TEST_PROJECT, ROOT_PACKAGE);

		facade.execute(URI.createURI("classpath:/"), URI.createFileURI(TEST_MODEL));

	}

}
