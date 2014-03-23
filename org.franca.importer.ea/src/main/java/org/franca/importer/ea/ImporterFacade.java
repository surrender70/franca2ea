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

import org.eclipse.emf.common.util.URI;

/**
 * 
 * @author Torsten Mosis
 *
 * Use dependeny-injection to retrieve an instance of an ImporterFacade
 * There is one implementation available with org.franca.importer.ea.CreateOrOverwriteModule
 * 
 */
public interface ImporterFacade {

	/**
	 * Setting the relevant parameter for importing franca models into an Enterprise Architect project
	 * Depending on the import type (initial import or merge/update) the project will be created/overwritten or not
	 * The configuration takes place with dependency-injection (see e.g. CreateOrOverwriteModule)
	 *  
	 * @param project The path (absolute or relative) where the EA project is located (e.g. "your/path/test.eap")
	 * @param _rootPackage Defines the EA UML package, where within the EA project the imported franca model should be placed at
	 * 		  (e.g. "Model/Logical View")
	 * @return true if setup was successful, false otherwise
	 */
	public boolean setup(String project, String _rootPackage);
	
	/**
	 * Invokes the import of a single franca model 
	 * @param uriRoot The root uri (s.th. like "classpath:/" or "file:/"
	 * @param uriModel The path where model is located
	 * @return true if import was successful, false otherwise
	 */
	public boolean execute(URI uriRoot, URI uriModel);
	
	
	/**
	 * Invokes the import of multiple franca models located under a given directory
	 * The import cannot be interrupted by an API-call
	 * 
	 * @param francaModelPath The path (absolute or relative) where the franca models are located 
	 * @return true if import was successful, false otherwise
	 */
	public boolean execute(String francaModelPath);
	
	
	/**
	 * Tears down the facade. All resources are released
	 */
	public void tearDown();
	
}
