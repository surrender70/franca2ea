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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LifecycleHelper {

	private static Logger jlog =  Logger.getLogger(LifecycleHelper.class.getName());	

	private List<String> listBefore = new ArrayList<String>();
	private List<String> listAfter = new ArrayList<String>();
	
	public void start() {
		listBefore.clear();
		listAfter.clear();

		eaProcessList(listBefore);
	}
	
	public void stop() {
		eaProcessList(listAfter);
		
		if(listAfter.size() > listBefore.size()) {
			
			String task = listAfter.get(listAfter.size()-1);
			
			StringTokenizer tokenizer = new StringTokenizer(task);
			
			// processName
			tokenizer.nextToken();
			
			// processId
			String pid = tokenizer.nextToken();

			Runtime runtime = Runtime.getRuntime();

			try {
				
				jlog.log(Level.WARNING, "Obviously EA.exe was not terminated correctly after API call. Therefore it must be killed by OS Call");				
				jlog.log(Level.WARNING, "Killing EA Process: "+pid);

				runtime.exec("taskkill /F /PID "+pid);
				
				Thread.sleep(1000);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			
		}		
	}
	
	private void eaProcessList(List<String> list) {
		
		Runtime runtime = Runtime.getRuntime();
		
		try {
			Process p = runtime.exec("tasklist /FI \"IMAGENAME eq EA.exe\" /NH");			
			InputStream inputStream = p.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			
			String line = bufferedReader.readLine();
			
			while(line != null) {
								
				if(line.startsWith("EA")) {
					jlog.log(Level.FINE, "Found Running EA Process: "+line);
					list.add(line);					
				}
				
				line = bufferedReader.readLine();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
