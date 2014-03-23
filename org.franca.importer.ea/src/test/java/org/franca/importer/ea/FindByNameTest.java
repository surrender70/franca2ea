/*******************************************************************************
 * Copyright (c) 2014 Torsten Mosis.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Torsten Mosis - initial API and implementation
 ******************************************************************************/
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
import org.franca.importer.ea.internal.ImporterFacadeImpl;
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
				bind(ImporterFacade.class).to(ImporterFacadeImpl.class);
			}
			
			@Provides
			ImportProcessor provideImportProcessor() {
				
				ImportProcessorDecorator testDecorator = new ImportProcessorDecorator(new FindByName(
						new DummyProcessor())) {
					
					@Override
					public Element handleStructure(ElementContainer<?> parent, FStructType src) {
						
						Element result = processor.handleStructure(parent, src);
						
						assertNotNull(result);
						
						assertEquals(result.GetName(), src.getName());
						
						assertEquals(EStereoType.STRUCTURE.getName(), result.GetStereotype());
						
						return result;
					}

					
					@Override
					public Element handleUnion(ElementContainer<?> parent, FUnionType src) {
						
						Element result = processor.handleUnion(parent, src);
						
						assertNotNull(result);
						
						assertEquals(result.GetName(), src.getName());

						assertEquals(EStereoType.UNION.getName(), result.GetStereotype());

						return result;
					}
					
					
					@Override
					public Method handleSimpleMethod(Element parent, FMethod src) {
						
						Method result = processor.handleSimpleMethod(parent, src);
						
						assertNotNull(result);
						
						assertEquals(result.GetName(), src.getName());
										
						return result;
					}
					
					@Override
					public Parameter handleParameter(Method parent, EParamType direction,
							FArgument src) {
										
						return null;
					}
					
					@Override
					public Package handleModel(Package parent, FModel src, String packageName) {
						
						Package result = processor.handleModel(parent, src, packageName);
						
						assertNotNull(result);
						
						//assertEquals(result.GetName(), src.getName());
										
						return result;
					}
					
					@Override
					public Element handleInterface(ElementContainer<?> parent, FInterface src) {
						
						Element result = processor.handleInterface(parent, src);
						
						assertNotNull(result);
						
						assertEquals(result.GetName(), src.getName());
						
						return result;
					}
					
					@Override
					public Attribute handleField(Element parent, FField src) {
						
						Attribute result = processor.handleField(parent, src);
										
						assertNotNull(result);
						
						assertEquals(result.GetName(), src.getName());
						
						assertEquals(getTypeName(src.getType()), result.GetType());
			
						return result;
					}
					
					@Override
					public Attribute handleEnumerator(Element parent, FEnumerator src) {
						
						Attribute result = processor.handleEnumerator(parent, src);
						
						assertNotNull(result);
						
						assertEquals(result.GetName(), src.getName());
						
						return result;
					}
					
					@Override
					public Element handleEnumeration(ElementContainer<?> parent, FEnumerationType src) {
						
						Element result = processor.handleEnumeration(parent, src);
						
						assertNotNull(result);
						
						assertEquals(result.GetName(), src.getName());
						
						assertEquals(EStereoType.ENUMERATION.getName(), result.GetStereotype());

						return result;
					}
					
					@Override
					public Method handleBroadcastMethod(Element parent, FBroadcast src) {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public Element handleTypedef(ElementContainer<?> parent, FTypeDef src) {
						
						Element result = processor.handleTypedef(parent, src);
						
						assertNotNull(result);
										
						assertEquals(result.GetName(), src.getName());
						
						assertEquals(EStereoType.ALIAS.getName(), result.GetStereotype());
						
						return result;			
					}

					@Override
					public Element handleArray(ElementContainer<?> parent, FArrayType src) {
						
						Element result = processor.handleArray(parent, src);
						
						assertNotNull(result);
						
						assertEquals(result.GetName(), src.getName());
						
						assertEquals(EStereoType.ARRAY.getName(), result.GetStereotype());

						return result;			
					}

					@Override
					public Element handleMap(ElementContainer<?> parent, FMapType src) {
						
						Element result = processor.handleMap(parent, src);
						
						assertNotNull(result);
						
						assertEquals(result.GetName(), src.getName());
						
						assertEquals(EStereoType.MAP.getName(), result.GetStereotype());
						
						return result;
					}

					@Override
					public Attribute handleMapKey(Element parent, FTypeRef src) {
									    				
						return null;
					}

					@Override
					public Attribute handleMapValue(Element parent, FTypeRef src) {
						
						return null;
					}

					@Override
					public Attribute handleAttribute(Element parent, FAttribute src) {
						
						Attribute result = processor.handleAttribute(parent, src);

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
