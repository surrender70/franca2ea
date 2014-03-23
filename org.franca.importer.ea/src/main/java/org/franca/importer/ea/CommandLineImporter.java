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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.franca.importer.ea.utils.CommandLineHelper;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * 
 * This class provides the possibility to use the Franca-2-EA importer via command-line
 * It can also be used to see how the API is working:
 * 
 * (1) Configure the tool with dependency-injection
 * (2) Get an instance of the ImporterFacade
 * (3) Setup the facade with the import configuration
 * (4) Invoke the import
 * (5) tear everything down
 * 
 * @author Torsten Mosis
 *
 */
public class CommandLineImporter {

	private static Logger jlog = Logger.getLogger(CommandLineImporter.class.getName());

	public int doImport(String[] args) {

		// (1) Configure the tool with dependency injection
		// (2) Get an instance of the ImporterFacade
		Injector injector = Guice.createInjector(new CreateOrOverwriteModule());
		ImporterFacade facade = injector.getInstance(ImporterFacade.class);

		// Read the runtime configuration parameter
		CommandLineHelper cliOptions = new CommandLineHelper();
		cliOptions.createOptions();
		cliOptions.parseOptions(args);

		if (cliOptions.getEaProject() != null
				&& cliOptions.getRootPackage() != null
				&& cliOptions.getFrancaPath() != null) {

			// (3) Setup the facade with the import configuration
			if (!facade.setup(cliOptions.getEaProject(), cliOptions.getRootPackage())) {
				facade.tearDown();
				return -1;
			}
			
			// (4) Invoke the import
			facade.execute(cliOptions.getFrancaPath());

			// (5) tear everything down
			facade.tearDown();

			return 0;
		}

		else {
			return -2;
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		CommandLineImporter importer = new CommandLineImporter();

		int result = importer.doImport(args);

		switch (result) {
		case 0:
			jlog.log(Level.INFO, "Import successfully finished");
			break;
		case -1:
			jlog.log(Level.SEVERE, "Failed to open or create project");
			break;
		case -2:
			jlog.log(Level.SEVERE, "Please specify import parameter");
			break;
		default:
			break;
		}

		System.exit(result);

	}

}
