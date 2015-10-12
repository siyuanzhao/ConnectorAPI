package org.assistments.connector.exception;

public class ReferenceNotFoundException extends Exception {

	private static final long serialVersionUID = -2642515416131023325L;

	public ReferenceNotFoundException(String msg) {
		super(msg);
	}
	
	public ReferenceNotFoundException(Throwable t) {
		super(t);
	}
}
