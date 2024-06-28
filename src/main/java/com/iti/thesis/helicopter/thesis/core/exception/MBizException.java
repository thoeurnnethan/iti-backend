package com.iti.thesis.helicopter.thesis.core.exception;

public class MBizException extends MException {
	private static final long	serialVersionUID	= -4454580843735287729L;
	
	public MBizException() {
		super();
	}
	
	public MBizException(String message) {
		super(message);
	}
	
	public MBizException(String code, String message) {
		super(code, message);
	}
	
	public MBizException(Throwable cause) {
		super(cause);
	}
	
	public MBizException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public MBizException(String code, String message, Throwable cause) {
		super(code, message, cause);
	}
}
