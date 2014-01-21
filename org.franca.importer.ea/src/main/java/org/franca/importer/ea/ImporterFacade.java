package org.franca.importer.ea;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.URI;
import org.franca.core.dsl.FrancaIDLHelpers;
import org.franca.core.dsl.FrancaPersistenceManager;
import org.franca.core.franca.FModel;
import org.franca.importer.ea.internal.AppearanceCustomizer;
import org.franca.importer.ea.internal.AutoResolver;
import org.franca.importer.ea.internal.DependencyResolver;
import org.franca.importer.ea.internal.DummyProcessor;
import org.franca.importer.ea.internal.ElementCreator;
import org.franca.importer.ea.internal.FindByName;
import org.franca.importer.ea.internal.Franca2EA;
import org.franca.importer.ea.internal.NotesUpdater;
import org.franca.importer.ea.internal.utils.EARepositoryAccessor;
import org.franca.importer.ea.internal.utils.FModelSorter;
import org.franca.importer.ea.internal.utils.FileScanner;
import org.franca.importer.ea.internal.utils.SortException;
import org.sparx.Package;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class ImporterFacade {

	@Inject
	private Logger jlog;

	private Package rootPackage = null;
	
	@Inject
	private Franca2EA importer = null;
	
	@Inject
	@Named("create")
	private boolean createProject;

	@Inject
	@Named("create")
	private boolean createRootPackage;

	//TODO Change from FrancaIDLHelpers to FrancaPersistenceManager with dependency-injection
//	@Inject
//	private FrancaPersistenceManager fidlLoader;

	private List<FModel> models = new ArrayList<FModel>();
		
	public ImporterFacade() {
	}

	/**
	 * Sets the constraints for the import
	 * @param project The EA project path (e.g. "test/ea/myproject.eap")
	 * @param createProject set to true if project should be created if not already existing
	 * @param _rootPackage	The package within the EA project where the imported franca models should be located, e.g "Model/Logical View" 
	 * @param createRootPackage set to true if root package should be created, if not already existing
	 * @return  true if setup was successful, false otherwise
	 */
	public boolean setup(String project, String _rootPackage) {
				
		jlog.log(Level.INFO, project +" " +createProject+ " "+ _rootPackage +" " + createRootPackage);
		
		// Open an existing EA Project
		EARepositoryAccessor.INSTANCE
				.openProject(project, createProject);

		// Find the root package where to import the franca models
		rootPackage = EARepositoryAccessor.INSTANCE
				.findOrCreateRootPackage(_rootPackage, createRootPackage);
		
		
		return rootPackage != null;
	}
	
	/** Adds a franca model to the list of models, that need to be transformed 
	 * @param model The franca model
	 * @param containerName The "name" of the model. Since the filename is not part of the model itself, it must be provided separately 
	 */
	private void addModel(FModel model, String containerName) {				
		models.add(model);
	}

	/**
	 * Starts the actual import of franca models
	 * Invoked by one of the public execute methods
	 * As a prerequisite the franca models must be sorted in the beginning, so that franca elements have been
	 * defined before they are used somewhere else. Otherwise you cannot generate EA elements.  
	 * @return
	 */
	private boolean execute() {
		
		// Sort the franca models
		FModelSorter fModelDependencyResolver = new FModelSorter(models);
		try {
			fModelDependencyResolver.sort();
		} catch (SortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
					
		// Iterate through the models - one-by-one
		for (FModel fModel : fModelDependencyResolver.getSorted()) {
							
			Package p = EARepositoryAccessor.INSTANCE.findLocalPackageForFrancaModel(rootPackage, fModel.getName());
			
			if(p == null) {
				return false;
			}
						
			importer.processRootPackage(p, fModel);
		}

		rootPackage.Update();
		
		return true;
	}
	
	private FModel loadFrancaModel(URI model, URI root) {
		return FrancaIDLHelpers.instance().loadModel(model, root);
		//return fidlLoader.loadModel(model, root);
	}
	
	/**
	 * Starts the importing process of a single franca model, specified by URIs
	 * @param uriRoot The URI root, e.g.: "file:/" or "classpath:/"
	 * @param uriModel The file URI of a franca model (relative or absolute)
	 * @return true if import was successful, false otherwise
	 */
	public boolean execute(URI uriRoot, URI uriModel) {
						
		addModel(loadFrancaModel(uriModel, uriRoot), 
				uriModel.lastSegment());					
				
		return execute();
		
	}	
		
	public boolean execute(String francaModelPath) {
				
		FileScanner fScanner = new FileScanner();
		List<String> francaModels = fScanner.getFiles(francaModelPath);

		// Load all Franca Models and sort them to solve the dependency chain
		for(int i=0; i<francaModels.size(); i++) {
			File francaFile = new File(francaModels.get(i));
			
			URI root = URI.createURI("file:/");
			URI m = URI.createFileURI(francaFile.getAbsolutePath());
			
			addModel(loadFrancaModel(m, root), francaFile.getName().split("\\.")[0]);			
			
		}
		
		return execute();
	}	
	
	/**
	 * Shuts down the connection to the EA repository and cleans the list of franca models
	 */
	public void tearDown() {

		jlog.log(Level.INFO, "Tear down importer. All franca models will be cleared and connection to EA database will be closed");
		
		models.clear();
				
		EARepositoryAccessor.INSTANCE.closeProject();
		
	}

		
}
