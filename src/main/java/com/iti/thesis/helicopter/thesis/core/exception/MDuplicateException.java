package com.iti.thesis.helicopter.thesis.core.exception;

public class MDuplicateException extends MException {
	private static final long serialVersionUID = -4668068470041155355L;
	
	public MDuplicateException() {
		super();
	}
	
	public MDuplicateException(String message) {
		super(message);
	}
	
	public MDuplicateException(String code, String message) {
		super(code, message);
	}
	
	public MDuplicateException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public MDuplicateException(Throwable cause) {
		super(cause);
	}
	
	public MDuplicateException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}
}
