package org.franca.importer.ea;

import static org.junit.Assert.*;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.franca.core.dsl.FrancaIDLHelpers;
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
import org.franca.core.franca.FTypeRef;
import org.franca.core.franca.FUnionType;
import org.franca.importer.ea.internal.AppearanceCustomizer;
import org.franca.importer.ea.internal.DependencyResolver;
import org.franca.importer.ea.internal.DummyProcessor;
import org.franca.importer.ea.internal.ElementCreator;
import org.franca.importer.ea.internal.Franca2EA;
import org.franca.importer.ea.internal.ImportProcessorDecorator;
import org.franca.importer.ea.internal.NotesUpdater;
import org.franca.importer.ea.internal.utils.EARepositoryAccessor;
import org.franca.importer.ea.internal.utils.ElementContainer;
import org.franca.importer.ea.internal.utils.EAConstants.EParamType;
import org.franca.importer.ea.internal.utils.EAConstants.EStereoType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sparx.Attribute;
import org.sparx.Connector;
import org.sparx.Element;
import org.sparx.Method;
import org.sparx.Package;
import org.sparx.Parameter;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Names;

public class DependencyResolverTest {

	private static ImporterFacade facade = null;
	private final static String TEST_PROJECT = "src/test/resources/test.eap";
	private final static String ROOT_PACKAGE = "Model/Logical View";

	@BeforeClass
	public static void setUp() throws Exception {

		// Delete project if already exists
		File projectFile = new File("src/test/resources/test.eap");

		if (projectFile.exists()) {
			projectFile.delete();
		}

	}

	@AfterClass
	public static void tearDown() throws Exception {
		facade.tearDown();
	}

	@Test
	public void testDependencyResolver() {

		Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bindConstant().annotatedWith(Names.named("create")).to(true);
			}

			@Provides
			ImportProcessor provideImportProcessor() {
				
				ImportProcessor testDecorator = new ImportProcessorDecorator(
						new DependencyResolver(new ElementCreator(
								new DummyProcessor()))) {

					@Override
					public Package makePackage(Package parent, FModel src,
							String packageName) {
						// TODO Auto-generated method stub
						return super.makePackage(parent, src, packageName);
					}

					@Override
					public Package makePackage(Package parent,
							FTypeCollection src) {
						// TODO Auto-generated method stub
						return super.makePackage(parent, src);
					}

					@Override
					public Element makeInterface(ElementContainer<?> parent,
							FInterface src) {
						// TODO Auto-generated method stub
						return super.makeInterface(parent, src);
					}

					@Override
					public Method makeSimpleMethod(Element parent, FMethod src) {
						// TODO Auto-generated method stub
						return super.makeSimpleMethod(parent, src);
					}

					@Override
					public Method makeBroadcastMethod(Element parent,
							FBroadcast src) {
						// TODO Auto-generated method stub
						return super.makeBroadcastMethod(parent, src);
					}

					@Override
					public Parameter makeParameter(Method parent,
							EParamType direction, FArgument src) {
						// TODO Auto-generated method stub
						return super.makeParameter(parent, direction, src);
					}

					@Override
					public Element makeStructure(ElementContainer<?> parent,
							FStructType src) {

						if (src.getBase() != null) {

							Element subElement = processor.makeStructure(
									parent, src);
							Element superElement = EARepositoryAccessor.INSTANCE
									.findElement(src.getBase().getName());

							assertEquals(EStereoType.STRUCTURE.getName(),
									superElement.GetStereotype());

							checkInheritance(subElement, superElement);

						}

						return super.makeStructure(parent, src);
					}

					@Override
					public Element makeEnumeration(ElementContainer<?> parent,
							FEnumerationType src) {

						if (src.getBase() != null) {
							Element subElement = processor.makeEnumeration(
									parent, src);
							Element superElement = EARepositoryAccessor.INSTANCE
									.findElement(src.getBase().getName());

							assertEquals(EStereoType.ENUMERATION.getName(),
									superElement.GetStereotype());

							checkInheritance(subElement, superElement);
						}
						return super.makeEnumeration(parent, src);
					}

					@Override
					public Attribute makeField(Element parent, FField src) {

						if (src.getType().getDerived() != null) {

							Attribute attr = processor.makeField(parent, src);

							Element type = EARepositoryAccessor.INSTANCE
									.findElement(attr.GetType());

							FType francaType = src.getType().getDerived();

							if (francaType instanceof FStructType) {
								assertEquals(EStereoType.STRUCTURE.getName(),
										type.GetStereotype());
							} else if (francaType instanceof FUnionType) {
								assertEquals(EStereoType.UNION.getName(),
										type.GetStereotype());
							} else if (francaType instanceof FEnumerationType) {
								assertEquals(EStereoType.ENUMERATION.getName(),
										type.GetStereotype());
							} else if (francaType instanceof FMapType) {
								assertEquals(EStereoType.MAP.getName(),
										type.GetStereotype());
							} else if (francaType instanceof FTypeDef) {
								assertEquals(EStereoType.ALIAS.getName(),
										type.GetStereotype());
							} else if (francaType instanceof FArrayType) {
								assertEquals(EStereoType.ARRAY.getName(),
										type.GetStereotype());
							}

							short foundConnections = 0;

							for (Connector c : type.GetConnectors()) {
								if (c.GetName().equals(src.getName())) {
									foundConnections++;
									assertEquals(type.GetElementID(),
											c.GetClientID());
									assertEquals(parent.GetElementID(),
											c.GetSupplierID());
								}
							}

							assertTrue(1 == foundConnections);

						}

						return super.makeField(parent, src);
					}

					@Override
					public Attribute makeEnumerator(Element parent,
							FEnumerator src) {

						return super.makeEnumerator(parent, src);
					}

					@Override
					public Element makeTypedef(ElementContainer<?> parent,
							FTypeDef src) {
						return super.makeTypedef(parent, src);
					}

					@Override
					public Element makeArray(ElementContainer<?> parent,
							FArrayType src) {

						if (src.getElementType().getDerived() != null) {

							Element array = processor.makeArray(parent, src);
							Element arrayElementType = EARepositoryAccessor.INSTANCE
									.findElement(src.getElementType()
											.getDerived().getName());

							short foundConnections = 0;

							for (Connector c : array.GetConnectors()) {
								if (c.GetStereotype().equals(
										EStereoType.ARRAY_OF.getName())) {
									foundConnections++;
									assertEquals(
											arrayElementType.GetElementID(),
											c.GetClientID());
									assertEquals(array.GetElementID(),
											c.GetSupplierID());
								}
							}

							assertTrue(1 == foundConnections);

						}

						return super.makeArray(parent, src);
					}

					@Override
					public Element makeUnion(ElementContainer<?> parent,
							FUnionType src) {

						if (src.getBase() != null) {
							Element subElement = processor.makeUnion(parent,
									src);
							Element superElement = EARepositoryAccessor.INSTANCE
									.findElement(src.getBase().getName());

							assertEquals(EStereoType.UNION.getName(),
									superElement.GetStereotype());

							checkInheritance(subElement, superElement);
						}
						return super.makeUnion(parent, src);
					}

					@Override
					public Element makeMap(ElementContainer<?> parent,
							FMapType src) {
						// TODO Auto-generated method stub
						return super.makeMap(parent, src);
					}

					@Override
					public Attribute makeMapKey(Element parent, FTypeRef src) {
						// TODO Auto-generated method stub
						return super.makeMapKey(parent, src);
					}

					@Override
					public Attribute makeMapValue(Element parent, FTypeRef src) {
						// TODO Auto-generated method stub
						return super.makeMapValue(parent, src);
					}

					@Override
					public Attribute makeAttribute(Element parent,
							FAttribute src) {
						// TODO Auto-generated method stub
						return super.makeAttribute(parent, src);
					}

					@Override
					public Parameter makeParameter(Method parent,
							EParamType direction, FEnumerationType src) {
						// TODO Auto-generated method stub
						return super.makeParameter(parent, direction, src);
					}

					@Override
					public Parameter makeParameter(Method parent,
							EParamType direction, FEnumerator src) {
						// TODO Auto-generated method stub
						return super.makeParameter(parent, direction, src);
					}

					private void checkInheritance(Element subElement,
							Element superElement) {

						short foundInheritances = 0;

						for (Connector c : subElement.GetConnectors()) {
							if (c.GetStereotype().equals(
									EStereoType.INHERITANCE.getName())) {
								foundInheritances++;
								assertEquals(subElement.GetElementID(),
										c.GetClientID());
								assertEquals(superElement.GetElementID(),
										c.GetSupplierID());
							}
						}

						assertTrue(1 == foundInheritances);
					}

				};

				return testDecorator;
			}
		});

		facade = injector.getInstance(ImporterFacade.class);
		
		facade.setup(TEST_PROJECT, ROOT_PACKAGE);

		facade.execute(URI.createURI("classpath:/"), URI.createFileURI("fidl/TypeSortTestModel.fidl"));

	}

}
