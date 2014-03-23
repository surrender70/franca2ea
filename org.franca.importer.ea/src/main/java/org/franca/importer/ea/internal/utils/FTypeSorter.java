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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.activation.FileTypeMap;

import org.franca.core.franca.FArrayType;
import org.franca.core.franca.FCompoundType;
import org.franca.core.franca.FEnumerationType;
import org.franca.core.franca.FField;
import org.franca.core.franca.FMapType;
import org.franca.core.franca.FStructType;
import org.franca.core.franca.FType;
import org.franca.core.franca.FTypeDef;
import org.franca.core.franca.FTypeRef;
import org.franca.core.franca.FUnionType;

public class FTypeSorter extends FrancaSorter<FType> {

	public FTypeSorter(List<FType> toSort) {
		super(toSort);
	}

	@Override
	public boolean canSwitch(FType r) {

		if (r instanceof FEnumerationType) {
			return isReferenceResolved(((FEnumerationType) r).getBase());
		} else if (r instanceof FTypeDef) {
			return isReferenceResolved(((FTypeDef) r).getActualType().getDerived());		
		} else if (r instanceof FArrayType) {
			return isReferenceResolved(((FArrayType) r).getElementType().getDerived());
		} else if (r instanceof FCompoundType) {
			boolean canSwitch = true;
			for (FField f : ((FCompoundType) r).getElements()) {
				if (!isReferenceResolved(f.getType().getDerived())) {
					canSwitch = false;
					
					break;
				}
			}
			if(canSwitch) {
				if(r instanceof FStructType) {
					canSwitch = isReferenceResolved(((FStructType) r).getBase());
				}
				else if(r instanceof FUnionType) {
					canSwitch = isReferenceResolved(((FUnionType) r).getBase());
				}
			}			
			return canSwitch; 
		} else if (r instanceof FMapType) {
			if( isReferenceResolved(((FMapType) r).getKeyType().getDerived()) && 
				isReferenceResolved(((FMapType) r).getValueType().getDerived()) )
			return true;
		}

		return false;

	}

}
