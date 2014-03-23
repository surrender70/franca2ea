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

import java.util.HashMap;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.franca.core.dsl.FrancaIDLHelpers;
import org.franca.core.franca.FArrayType;
import org.franca.core.franca.FCompoundType;
import org.franca.core.franca.FEnumerationType;
import org.franca.core.franca.FField;
import org.franca.core.franca.FMapType;
import org.franca.core.franca.FModel;
import org.franca.core.franca.FStructType;
import org.franca.core.franca.FType;
import org.franca.core.franca.FTypeDef;
import org.franca.core.franca.FTypeRef;
import org.franca.core.franca.FUnionType;
import org.franca.importer.ea.internal.utils.FTypeSorter;
import org.franca.importer.ea.internal.utils.SortException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FTypeSorterTest {


	@SuppressWarnings("deprecation")
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testGetSorted() {

		URI root = URI.createURI("classpath:/");
//		URI m1 = URI.createFileURI("src/test/resources/fidl/TypeSortTestModel.fidl");
		URI m1 = URI.createFileURI("fidl/TypeSortTestModel.fidl");
				
		FModel result  = FrancaIDLHelpers.instance().loadModel(m1, root);

		HashMap<String, FType> map =  new HashMap<String, FType>();
		FTypeSorter typeSorter = new FTypeSorter(result.getTypeCollections().get(0).getTypes());		
		try {
			typeSorter.sort();
		} catch (SortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(FType t: typeSorter.getSorted()) {
		
			System.out.println(t.getName());
			
			map.put(t.getName(), t);
			
			if(t instanceof FCompoundType) {							
				for(FField f: ((FCompoundType) t).getElements()) {
					FType refType = f.getType().getDerived();
					if(refType != null) {
						assertTrue(map.containsValue(refType));
					}
				}				
				if(t instanceof FStructType) {
					FStructType base = ((FStructType) t).getBase();
					if(base != null) {
						assertTrue(map.containsValue(base));
					}
				}
				if( t instanceof FUnionType) {
					FUnionType base = ((FUnionType) t).getBase();
					if(base != null) {
						assertTrue(map.containsValue(base));
					}
				}
			}
			
			else if(t instanceof FEnumerationType) {
				FEnumerationType base = ((FEnumerationType) t).getBase();
				if(base != null) {
					assertTrue(map.containsValue(base));
				}
			}
			
			else if(t instanceof FTypeDef) {
				FTypeRef base = ((FTypeDef) t).getActualType();
				if(base.getDerived() != null) {
					assertTrue(map.containsValue(base.getDerived()));
				}
			}
			
			else if(t instanceof FArrayType) {
				FTypeRef base = ((FArrayType) t).getElementType();
				if(base.getDerived() != null) {
					assertTrue(map.containsValue(base.getDerived()));
				}
			}
			
			else if(t instanceof FMapType) {
				FTypeRef key = ((FMapType) t).getKeyType();
				if(key.getDerived() != null) {
					assertTrue(map.containsValue(key.getDerived()));
				}
				FTypeRef value = ((FMapType) t).getKeyType();
				if(value.getDerived() != null) {
					assertTrue(map.containsValue(value.getDerived()));
				}
			}
			
		}
		
		map.clear();
		
	}

}
