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

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class CommandLineHelper {

	private final static String CLI_EA_PROJECT = "eap";
	
	private final static String CLI_EA_ROOT_PACKAGE = "rp";
	
	private final static String CLI_FRANCA_MODEL_PATH = "fp";
	
	private final static String CLI_FRANCA_MODELS = "fm";
		
	// create Options object
	Options options = new Options();
	private String eaProject = null;
	
	public String getEaProject() {
		return eaProject;
	}

	public String getRootPackage() {
		return rootPackage;
	}

	public String getFrancaPath() {
		return francaPath;
	}

	public String getFrancaModels() {
		return francaModels;
	}

	private String rootPackage = null;
	
	private String francaPath = null;
	
	private String francaModels = null;

	public void createOptions() {
		
		// add t option
		options.addOption(CLI_EA_PROJECT, true, "path of enterprise architect project (.eap)");
		
		options.addOption(CLI_EA_ROOT_PACKAGE, true, "root package within enterprise architect uml model to store imported franca models");
		
		options.addOption(CLI_FRANCA_MODEL_PATH, true, "main path to find franca models");
		
		options.addOption(CLI_FRANCA_MODELS, true, "list of franca models to load, separated by ','");
				
	}
	
	public void parseOptions(String[] args) {
		
	    CommandLineParser parser = new PosixParser();
	    try {
	        // parse the command line arguments
	        CommandLine line = parser.parse( options, args );
	        
	        if( line.hasOption( CLI_EA_PROJECT ) ) {
	            eaProject = line.getOptionValue( CLI_EA_PROJECT );	            
	        }
	        
	        if( line.hasOption( CLI_EA_ROOT_PACKAGE ) ) {
	            rootPackage = line.getOptionValue( CLI_EA_ROOT_PACKAGE );
	        }
	        
	        if( line.hasOption( CLI_FRANCA_MODEL_PATH)) {
	        	francaPath = line.getOptionValue( CLI_FRANCA_MODEL_PATH );
	        }
	        
	        if( line.hasOption( CLI_FRANCA_MODELS)) {
	        	francaModels = line.getOptionValue( CLI_FRANCA_MODELS);
	        }
	        	        
	    }
	    catch( ParseException exp ) {
	        // oops, something went wrong
	        System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
	    }		
	}
	
}
