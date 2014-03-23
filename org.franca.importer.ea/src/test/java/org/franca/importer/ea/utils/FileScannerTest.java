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

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.franca.importer.ea.internal.utils.FileScanner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FileScannerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetFiles() {
		
		FileScanner scanner = new FileScanner();
				
		List<String> files = scanner.getFiles("src/test/resources/example/flightservice");
		
		assertNotNull(files);
		assertEquals(2, files.size());
		
		for(String f: files) {
			assertTrue(f.endsWith(".fidl"));
		}
		
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetFilesWithInvalidPath() {
		
		FileScanner scanner = new FileScanner();
		
		List<String> files = scanner.getFiles("./some/invalid/path");
		
	}
	
}
