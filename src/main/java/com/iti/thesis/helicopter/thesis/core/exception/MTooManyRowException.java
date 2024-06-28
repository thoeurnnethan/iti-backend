package com.iti.thesis.helicopter.thesis.core.exception;

public class MTooManyRowException extends MException {
	private static final long serialVersionUID = -215197930161389239L;
	
	public MTooManyRowException() {
		super();
	}
	
	public MTooManyRowException(String message) {
		super(message);
	}
	
	public MTooManyRowException(String code, Throwable cause) {
		super(code, cause);
	}
	
	public MTooManyRowException(Throwable cause) {
		super(cause.getMessage(), cause);
	}
	
	public MTooManyRowException(String code, String message) {
		super(code, message);
	}
	
	public MTooManyRowException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}
}
