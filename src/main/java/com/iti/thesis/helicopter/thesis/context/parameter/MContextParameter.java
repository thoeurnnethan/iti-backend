package com.iti.thesis.helicopter.thesis.context.parameter;

import com.iti.thesis.helicopter.thesis.context.MContextHolder;
import com.iti.thesis.helicopter.thesis.core.Json.JsonAdaptorObject;
import com.iti.thesis.helicopter.thesis.core.collection.MData;

public class MContextParameter {
	public static final String	REQUEST_HEADER				= "requestHeader";
	public static final String	REQUEST_CONTEXT_PARAMETER	= "requestContext";
	public static final String	SESSION_PARAMETER			= "sessionContext";
	public static final String	TOKEN_PARAMETER				= "tokenContext";
	
	public static void initMContextParameter() {
		
		if (MContextHolder.getContextObject(REQUEST_CONTEXT_PARAMETER) == null) {
			MContextHolder.setContextObject(REQUEST_CONTEXT_PARAMETER, new JsonAdaptorObject());
		}
		if (MContextHolder.getContextObject(SESSION_PARAMETER) == null) {
			MContextHolder.setContextObject(SESSION_PARAMETER, new MData());
		}
		
		if (MContextHolder.getContextObject(TOKEN_PARAMETER) == null) {
			MContextHolder.setContextObject(TOKEN_PARAMETER, new MData());
		}
	}
	
	public static MData getRequestHeader() {
		return (MData)MContextHolder.getContextObject(REQUEST_HEADER);
	}
	
	public static void setRequestHeader(MData bizMOBHeader) {
		MContextHolder.setContextObject(REQUEST_HEADER, bizMOBHeader);
	}
	
	public static JsonAdaptorObject getContext() {
		return (JsonAdaptorObject)MContextHolder.getContext().get(REQUEST_CONTEXT_PARAMETER);
	}
	
	public static void setContext(JsonAdaptorObject bizContext) {
		MContextHolder.setContextObject(REQUEST_CONTEXT_PARAMETER, bizContext);
	}
	
	public static MData getSessionContext() {
		return MContextHolder.getContext().getMData(SESSION_PARAMETER);
	}
	
	public static void setSessionContext(MData sessionContext) {
		MContextHolder.setContextObject(SESSION_PARAMETER, sessionContext);
	}
	
	public static MData getTokenContext() {
		return MContextHolder.getContext().getMData(TOKEN_PARAMETER);
	}
	
	public static void setTokenContext(MData tokenParameter) {
		MContextHolder.setContextObject(TOKEN_PARAMETER, tokenParameter);
	}
	
}
