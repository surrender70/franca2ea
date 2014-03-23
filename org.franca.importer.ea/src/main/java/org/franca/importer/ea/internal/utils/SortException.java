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

public class SortException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SortException() {
		super("Could not sort elements");
	}

	public SortException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public SortException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public SortException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
