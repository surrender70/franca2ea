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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public abstract class FrancaSorter<T> {

	protected List<T> mSorted;
	protected List<T> mRemaining;
	
	public FrancaSorter(List<T> toSort) {

		mRemaining = new CopyOnWriteArrayList<T>();
		for(int i=0; i<toSort.size(); i++) {
			mRemaining.add(toSort.get(i));
		}		
		mSorted = new CopyOnWriteArrayList<T>();
	}
	
	public void sort() throws SortException {
		
		int beforeRemaining = mRemaining.size();
		int beforeSorted = mSorted.size();
		
		for(T r: mRemaining) {
			if(canSwitch(r)) {
				switchElement(r);
			}
		}
				
		if(beforeRemaining == mRemaining.size()) {
			throw new SortException("Sorting cannot be finished. Stopped before endup in an endless loop");
		}
		
		if(beforeRemaining+beforeSorted != mRemaining.size()+mSorted.size()) {
			throw new SortException("Sorting algorithm lost one or more models to be sorted during recursion. Check algorithm");
		}
		
		if(mRemaining.size() > 0) {
			sort();
		}		
	}
	
	abstract public boolean canSwitch(T element) throws SortException; 

	public List<T> getSorted() {
		return mSorted;
	}
	
	protected void switchElement(T r) {
		mSorted.add(r);
		mRemaining.remove(r);
	}
	
	protected boolean isReferenceResolved(T type) {
		if(type == null) {
			return true;
		}
		else {
			return mSorted.contains(type) || !mRemaining.contains(type);
		}
	}


}
