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

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.franca.core.dsl.FrancaIDLHelpers;
import org.franca.core.franca.FModel;
import org.franca.importer.ea.internal.DummyProcessor;
import org.franca.importer.ea.internal.ElementCreator;
import org.franca.importer.ea.internal.Franca2EA;
import org.franca.importer.ea.internal.ImporterFacadeImpl;
import org.franca.importer.ea.internal.utils.EARepositoryAccessor;
import org.junit.Test;
import org.sparx.Package;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Names;

public class EAImportSuperTest {

	public static void initialImport(String eaProject, String rootPackage, String testModel) {

//		protected final static String TEST_PROJECT = "src/test/resources/test.eap";
//		protected final static String ROOT_PACKAGE = "Model/Logical View/";
		
		//Delete project if already exists
		File projectFile = new File(eaProject);
		
		if(projectFile.exists()) {
			projectFile.delete();
		}

		Injector injector = Guice.createInjector(new AbstractModule() {			
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
		
		ImporterFacade facade = injector.getInstance(ImporterFacade.class);		
		
		facade.setup(eaProject, rootPackage);
				
		// configure the test model
//		root = URI.createURI("classpath:/");
//		model = URI.createFileURI("fidl/TypeSortTestModel.fidl");
		
		facade.execute(URI.createURI("classpath:/"), URI.createFileURI(testModel));
		facade.tearDown();
	}
	

}
