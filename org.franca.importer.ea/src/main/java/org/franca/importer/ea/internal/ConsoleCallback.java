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
package org.franca.importer.ea.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.franca.core.franca.FArrayType;
import org.franca.core.franca.FEnumerationType;
import org.franca.core.franca.FEnumerator;
import org.franca.core.franca.FField;
import org.franca.core.franca.FStructType;
import org.franca.core.franca.FTypeDef;
import org.franca.core.franca.FUnionType;
import org.franca.importer.ea.ManualResolveCallback;
import org.sparx.Attribute;
import org.sparx.Element;

public class ConsoleCallback implements ManualResolveCallback {

	@Override
	public Element selectElement(FStructType src, List<Element> elems) {
				
		System.out.println("Please select, if structure "+src.getName()+" should be created or assigned to an already exisintg element");
		
		System.out.println("Fidl Structure:  "+src.getName());
		for(FField f: src.getElements()) {
			System.out.println("     "+f.getName()+" "+f.getType().toString());
		}		
				
		return selectElement(elems);
	}
	
	
	@Override
	public Element selectElement(FUnionType union, List<Element> elems) {
		
		System.out.println("Please select, if union "+union.getName()+" should be created or assigned to an already exisintg element");
		
		System.out.println("Fidl Structure:  "+union.getName());
		for(FField f: union.getElements()) {
			System.out.println("     "+f.getName()+" "+f.getType().toString());
		}		
				
		return selectElement(elems);
	}


	
	@Override
	public Element selectElement(FEnumerationType enumeration, List<Element> elems) {
		System.out.println("Please select, if enumeration "+enumeration.getName()+" should be created or assigned to an already exisintg element");
		
		System.out.println("Fidl Enumeration:  "+enumeration.getName());
		for(FEnumerator e: enumeration.getEnumerators()) {
			System.out.println("     "+e.getName());
		}		
		
		return selectElement(elems);
	}

	@Override
	public Element selectElement(FArrayType array, List<Element> elems) {
		
		System.out.println("Please select, if array "+array.getName()+" should be created or assigned to an already exisintg element");
		
		System.out.println("Fidl Array:  "+array.getName());
		
		return selectElement(elems);
	}

	@Override
	public Element selectElement(FTypeDef typedef, List<Element> elems) {
		
		System.out.println("Please select, if typedef "+typedef.getName()+" should be created or assigned to an already exisintg element");
		
		System.out.println("Fidl Typedef:  "+typedef.getName());
		
		return selectElement(elems);
	}

	private Element selectElement(List<Element> elems) {

		System.out.println( "0 : Create a new element");
		for(int i=0; i<elems.size(); i++) {
			Element e = elems.get(i);
			System.out.println( (i+1)+" : "+e.GetName());
			for(Attribute a: e.GetAttributes()) {
				System.out.println("     "+a.GetName()+" "+a.GetType());
			}
		}
		
	    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	    String strInput = null;
		try {
			strInput = input.readLine();
		    System.out.println("Selection: "+strInput);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		int result = Integer.parseInt(strInput);
		
		if(result <= 0 || result > elems.size()) {
			return null;
		}
		
		return elems.get(result-1);
		
	}
	
}
