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

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.franca.importer.ea.internal.AppearanceCustomizer;
import org.franca.importer.ea.internal.DependencyResolver;
import org.franca.importer.ea.internal.DummyProcessor;
import org.franca.importer.ea.internal.ElementCreator;
import org.franca.importer.ea.internal.ImporterFacadeImpl;
import org.franca.importer.ea.internal.NotesUpdater;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;

/**
 * Dependency injection Module to setup an importer configuration
 * This Module is used to configure an importer for an "initial" import
 * 
 * @author Torsten Mosis
 *
 */
public class CreateOrOverwriteModule extends AbstractModule {

	/**
	 * Since this is the configuration for an initial import we need to set the "create" values to true
	 * (Means a project and its root package will be created if not available)
	 */
	@Override
	protected void configure() {
		bindConstant().annotatedWith(Names.named("create")).to(true);
		bind(ImporterFacade.class).to(ImporterFacadeImpl.class);
	}
	
	/**
	 * The ImportProcessor is implemented with the decorator pattern.
	 * Certain functionality can be added or removed by using decorators
	 * 
	 * This one works as follows:
	 * ElementCreator - Create an UML Element for each franca model element
	 * DependencyResolver - Build UML dependencies/associations between the elements
	 * NotesUpdater - Take-over franca inline comments into UML comments 
	 * AppearanceCustomizer - Customize the look (e.g. colors) of UML model elements
	 * 
	 * @return The import processor for an initial import
	 */
	@Provides
	ImportProcessor provideImportProcessor() {
		
		ImportProcessor processor =
				new AppearanceCustomizer(
					new NotesUpdater(
						new DependencyResolver(
							new ElementCreator(
								new DummyProcessor()))));
		
		return processor;
	}
	
}
