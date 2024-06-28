package com.iti.thesis.helicopter.thesis.core.exception;

public class MNotAffectedException extends MException {
	private static final long serialVersionUID = -6167372328797543749L;
	
	public MNotAffectedException() {
		super();
	}
	
	public MNotAffectedException(String message) {
		super(message);
	}
	
	public MNotAffectedException(String code, Throwable cause) {
		super(code, cause);
	}
	
	public MNotAffectedException(Throwable cause) {
		super(cause);
	}
	
	public MNotAffectedException(String code, String message) {
		super(code, message);
	}
	
	public MNotAffectedException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}
}
