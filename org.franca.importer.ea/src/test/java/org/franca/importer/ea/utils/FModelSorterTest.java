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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.franca.core.dsl.FrancaIDLHelpers;
import org.franca.core.dsl.FrancaPersistenceManager;
import org.franca.core.franca.FModel;
import org.franca.importer.ea.internal.utils.FModelSorter;
import org.franca.importer.ea.internal.utils.SortException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FModelSorterTest {
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetSorted() {
		
		List<FModel> models = new ArrayList<FModel>();
		
		URI root = URI.createURI("classpath:/");
		URI m1 = URI.createFileURI("example/flightservice/flightdata.fidl");
		URI m2 = URI.createFileURI("example/flightservice/common.fidl");

		models.add(FrancaIDLHelpers.instance().loadModel(m1, root));
		models.add(FrancaIDLHelpers.instance().loadModel(m2, root));
				
		FModelSorter modelSorter = new FModelSorter(models);
		
		try {
			modelSorter.sort();
		} catch (SortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<FModel> sortedList = modelSorter.getSorted();
		
		assertNotNull(sortedList);		
		assertEquals(2, sortedList.size());
		
		assertEquals("flightservice.common",sortedList.get(0).getName());
		assertEquals("flightservice.flightdata",sortedList.get(1).getName());
		
	}

}
