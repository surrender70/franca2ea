package org.franca.importer.ea;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.franca.importer.ea.internal.AppearanceCustomizer;
import org.franca.importer.ea.internal.DependencyResolver;
import org.franca.importer.ea.internal.DummyProcessor;
import org.franca.importer.ea.internal.ElementCreator;
import org.franca.importer.ea.internal.NotesUpdater;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;

public class CreateOrOverwriteModule extends AbstractModule {

	@Override
	protected void configure() {
		bindConstant().annotatedWith(Names.named("create")).to(true);
	}
	
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
