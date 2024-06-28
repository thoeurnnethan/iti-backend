package com.iti.thesis.helicopter.thesis.core.exception;

public class MNotFoundException extends MException {
	private static final long serialVersionUID = 4590938470775174864L;
	
	public MNotFoundException() {
		super();
	}
	
	public MNotFoundException(String message) {
		super(message);
	}
	
	public MNotFoundException(String code, Throwable cause) {
		super(code, cause);
	}
	
	public MNotFoundException(Throwable cause) {
		super(cause);
	}
	
	public MNotFoundException(String code, String message) {
		super(code, message);
	}
	
	public MNotFoundException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}
}
