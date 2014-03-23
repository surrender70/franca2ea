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
package org.franca.importer.ea.internal.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface EAConstants {

	final static String EA_MISC_BASE_TYPE = "baseType";
	
	final static String CLIENTINTERFACE_SUFFIX = 
			MappingProperties.getProperty("clientinterface.suffix", "Client");
	
	final static String GENERATE_CLIENTINTEFACE =			
			MappingProperties.getProperty("clientinterface.generate", "true");

	final static String GENERATE_BROADCAST =			
			MappingProperties.getProperty("clientinterface-generate.broadcast", "true");

	final static String GENERATE_REQUEST_RESPONSE_PAIR =			
			MappingProperties.getProperty("clientinterface-generate.request-response-pair", "false");

	final static String GENERATE_ATTRIBUTE_METHOD = 
			MappingProperties.getProperty("clientinterface-generate.attribute-method", "false");
	
	final static String DIAGRAM_SHOW_EXTERNAL_DEPENDENCIES =
			MappingProperties.getProperty("diagram-depencencies.show-external-types", "false");			

	final static String DIAGRAM_EXTERNAL_DEPENDENCIES_BACKGROUND =
			MappingProperties.getProperty("diagram-depencencies.show-external-color", "c0c0c0");			

	final static String LANGUAGE_TYPE =
			MappingProperties.getProperty("language.gentype", "FrancaIDL");
	
	final static String KEEP_ORIGINAL_ORDER = 
			MappingProperties.getProperty("keep-original-order", "true");
	
	public enum EParamType {
		
		IN ("in"), OUT ("out");
		
		private String name = null;

		EParamType(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}		
	}
	
	public enum ECardinality {
		
		ZERO_TO_INFINITE ("0..*");
		
		private String name = null;

		ECardinality(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
						
	}
	
	public enum EVisibility {
		
		PUBLIC("public"),
		PRIVATE("private");
		
		private String name = null;

		EVisibility(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
		
	}
	
	public enum EClassType {
				
		PACKAGE (MappingProperties.getProperty("classtype.package", "Package")),
		CLASS (MappingProperties.getProperty("classtype.class", "Class")),
		ENUMERATION (MappingProperties.getProperty("classtype.enumeration", "Enumeration")),
		PARAMETER (MappingProperties.getProperty("classtype.parameter", "Parameter")),
		INTERFACE (MappingProperties.getProperty("classtype.interface", "Interface"));
		
		private String name = null;

		EClassType(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
		
	}
	
	public enum EStereoType {
		
		INTERFACE(MappingProperties.getProperty("stereotype.interface","interface")),
		STRUCTURE(MappingProperties.getProperty("stereotype.structure", "struct")),
		UNION(MappingProperties.getProperty("stereotype.union", "union")),
		MAP(MappingProperties.getProperty("stereotype.map", "map")),
		ENUMERATION(MappingProperties.getProperty("stereotype.enumeration", "enumeration")),
		ERROR_PARAMETER(MappingProperties.getProperty("stereotype.error-param", "error")),
		METHOD(MappingProperties.getProperty("stereotype.method", "method")),
		METHOD_FIREANDFORGET(MappingProperties.getProperty("stereotype.method-fireandforget", "fireAndForget")),
		BROADCAST(MappingProperties.getProperty("stereotype.broadcast", "broadcast")),
		BROADCAST_SELECTIVE(MappingProperties.getProperty("stereotype.broadcast-selective", "selective")),
		BROADCAST_DEPENDENCY(MappingProperties.getProperty("stereotype.broadcast-dependency", "FrancaInterface")),
		ATTRIBUTE(MappingProperties.getProperty("stereotype.attribute", "attribute")),
		ATTRIBUTE_READONLY(MappingProperties.getProperty("stereotype.attribute-readonly", "readonly")),
		ATTRIBUTE_NOSUBSCRIPTIONS(MappingProperties.getProperty("stereotype.attribute-nosubscriptions", "nosubscriptions")),
		ALIAS(MappingProperties.getProperty("stereotype.alias", "typedef")),
		ARRAY(MappingProperties.getProperty("stereotype.array", "array")),
		INHERITANCE(MappingProperties.getProperty("stereotype.inheritance", "extends")),
		ARRAY_OF(MappingProperties.getProperty("stereotype.array-of", "arrayOf")),
		ROOTPACKAGE(MappingProperties.getProperty("stereotype.rootpackage", "root")),
		FIDLFILE(MappingProperties.getProperty("stereotype.fidl-file", "fidl"));

		private String name = null;
		
		EStereoType(String name) {					
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}

	public enum EConnectionType {

		STRUCTURE(MappingProperties.getProperty("connector.structure", "Aggregation")),
		UNION(MappingProperties.getProperty("connector.union", "Aggregation")),
		ARRAY(MappingProperties.getProperty("connector.array", "Dependency")),
		ALIAS(MappingProperties.getProperty("connector.alias", "Dependency")),
		BROADCAST(MappingProperties.getProperty("connector.broadcast-interface", "Association")),
		MAP(MappingProperties.getProperty("connector.map", "Composition")),
		INHERITANCE(MappingProperties.getProperty("connector.inheritance", "Generalization"));

		private String name = null;

		EConnectionType(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

	}

	public enum EConnectorDirection {

		SOURCE_TO_DEST("Source -> Destination"), 
		DEST_TO_SOURCE("Destination -> Source"),
		UNSPECIFIED("Unspecified");

		private String name = null;

		EConnectorDirection(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

	}
	
	public enum EBackgroundColor {
				
		STRUCTURE(MappingProperties.getProperty("diagram-background.structure", "62d0b5")),
		UNION(MappingProperties.getProperty("diagram-background.union", "e2c5bf")),
		MAP(MappingProperties.getProperty("diagram-background.map", "b4f5fe")),
		ENUMERATION(MappingProperties.getProperty("diagram-background.enumeration", "a5dcfb")),
		ARRAY(MappingProperties.getProperty("diagram-background.array", "efcede")),
		TYPEDEF(MappingProperties.getProperty("diagram-background.typedef", "e0f3cc"));
				
		private String name = null;

		EBackgroundColor(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}		
	}
	
	public class MappingProperties {
		
		private static Logger jlog =  Logger.getLogger(MappingProperties.class.getName());
		
		static private boolean loaded = false;		
		static private Properties props = null;		
		static private boolean updateRequired = false;
		private final static String DEFAULT_PROPERTY_FILE = "conf/mapping.properties";
		
		static public String getProperty(String key, String defaultValue) {
			
			String propertyFile = null;
			
			if(loaded) {
				if(props != null) {
					if(props.containsKey(key)) {
						return (String) props.get(key);
					}
					else {
						props.put(key, defaultValue);
						updateRequired = true;
						return defaultValue;
					}
				}
			}
			else {
				props = new Properties() {
					  /**
					   * Overrides, called by the store method.
					   */
					  @SuppressWarnings("unchecked")
					  public synchronized Enumeration keys() {
					     Enumeration keysEnum = super.keys();
					     Vector keyList = new Vector();
					     while(keysEnum.hasMoreElements()){
					       keyList.add(keysEnum.nextElement());
					     }
					     Collections.sort(keyList);
					     return keyList.elements();
					  }					
				};
				
				loaded = true;
				try {
					propertyFile = System.getenv("franca2ea.conf");
					if(propertyFile == null || propertyFile.equals("")) {
						propertyFile = DEFAULT_PROPERTY_FILE;
					}
					props.load(new FileReader(propertyFile));
					return getProperty(key, defaultValue);
				} catch (FileNotFoundException e) {
					jlog.log(Level.WARNING, "Property file "+propertyFile+ " unknown, default values are used instead");
					updateRequired = false;
					props.put(key, defaultValue);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return defaultValue;
			
		}
		
		static public void writeProperties() {

			String propertyFile = System.getenv("franca2ea.conf");
			if(propertyFile == null || propertyFile.equals("")) {
				propertyFile = DEFAULT_PROPERTY_FILE;
			}
			
			if(updateRequired && props != null) {
				jlog.log(Level.INFO, "Updated property file "+propertyFile);
				try {
					
					// create file not already exist
					File file = new File(propertyFile.split("/")[0]);
					if(!file.exists()) {
						file.mkdir();
					}
					
					props.store(new FileWriter(propertyFile), "Updated");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
	}

}
