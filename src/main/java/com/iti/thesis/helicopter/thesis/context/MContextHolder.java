package com.iti.thesis.helicopter.thesis.context;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MContextHolder {
	private MContextThreadLocal contextThreadLocal = new MContextThreadLocal();
	
	public MData getContext() {
		return contextThreadLocal.getMData();
	}
	
	public String getContext(String contextKey) {
		return (String)contextThreadLocal.getMData().get(contextKey);
	}
	
	public Object getContextObject(String contextKey) {
		return contextThreadLocal.getMData().get(contextKey);
	}
	
	public void setContextObject(String contextKey, Object context) throws MException {
		setContext(contextKey, context);
	}
	
	public void setContext(String contextKey, Object context) throws MException {
		
		if (contextThreadLocal == null) {
			throw new MException(CommonErrorCode.NOT_FOUND.getCode(), "Context Error!");
		}
		contextThreadLocal.getMData().set(contextKey, context);
	}
	
	public void setContext(Object caller, String contextKey, MData context) throws MException {
		setContext(contextKey, context);
	}
	
	/**
	 * To remove context data
	 */
	public void remove() {
		contextThreadLocal.remove();
	}
	
	public void remove(String contextKey) {
		contextThreadLocal.getMData().remove(contextKey);
	}
	
	public void clear() {
		
		if (contextThreadLocal != null) {
			contextThreadLocal.remove();
		}
		
	}
	
	public void destroy() {
		clear();
		if (contextThreadLocal != null) {
			contextThreadLocal = null;
		}
		
	}

}
