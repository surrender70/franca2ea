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
