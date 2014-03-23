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
package org.franca.importer.ea.utils;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.franca.core.dsl.FrancaIDLHelpers;
import org.franca.core.franca.FModel;
import org.franca.core.franca.FType;
import org.franca.importer.ea.ImportProcessor;
import org.franca.importer.ea.ImporterFacade;
import org.franca.importer.ea.internal.AppearanceCustomizer;
import org.franca.importer.ea.internal.DependencyResolver;
import org.franca.importer.ea.internal.DummyProcessor;
import org.franca.importer.ea.internal.ElementCreator;
import org.franca.importer.ea.internal.Franca2EA;
import org.franca.importer.ea.internal.ImporterFacadeImpl;
import org.franca.importer.ea.internal.NotesUpdater;
import org.franca.importer.ea.internal.utils.EARepositoryAccessor;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Names;

public class EARepositoryAccessorTest {

	private final static String TEST_PROJECT = "src/test/resources/test2.eap";
	protected final static String ROOT_PACKAGE = "Model/Logical View/";
	private static ImporterFacade facade;
	private static FModel fmodel; 
	private static Injector injector;
	
	@BeforeClass
	public static void setUp() throws Exception {

		File eap = new File(TEST_PROJECT);
		
		if(eap.exists()) {
			eap.delete();
		}

		injector = Guice.createInjector(new AbstractModule() {			
			@Override
			protected void configure() {
				bindConstant().annotatedWith(Names.named("create")).to(true);				
				bind(ImporterFacade.class).to(ImporterFacadeImpl.class);
			}
			
			@Provides
			ImportProcessor provideImportProcessor() {
				
				ImportProcessor processor =
									new ElementCreator(
										new DummyProcessor());
				
				return processor;
			}
			
		});
		
		facade = injector.getInstance(ImporterFacade.class);
				
		facade.setup(TEST_PROJECT, ROOT_PACKAGE);
						
		URI root = URI.createURI("classpath:/");
		URI m1 = URI.createFileURI("fidl/test1.fidl");
						
		facade.execute(root, m1);
				
	}

	@AfterClass
	public static void tearDown() throws Exception {

		facade.tearDown();
		
		File eap = new File(TEST_PROJECT);
		
		if(eap.exists()) {
			eap.delete();
		}
				
	}

	@Test
	public void testOpenProject() {		
	}

	@Test
	public void testFindElement() {
		
		URI root = URI.createURI("classpath:/");
		URI m1 = URI.createFileURI("fidl/test1.fidl");
		fmodel = FrancaIDLHelpers.instance().loadModel(m1, root);
		
		for(FType t: fmodel.getInterfaces().get(0).getTypes()) {			
			assertTrue(t.getName().equals(
					EARepositoryAccessor.INSTANCE.findElement(t.getName()).GetName()));			
		}
		
	}

	@Test
	public void testFindRootPackage() {
	}

	@Test
	public void testFindLocalPackageForFrancaModel() {
	}

}
