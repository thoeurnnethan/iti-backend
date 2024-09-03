package com.iti.thesis.helicopter.thesis.context.util;

import org.apache.logging.log4j.util.Strings;

import com.iti.thesis.helicopter.thesis.context.parameter.MContextParameter;

public class MContextUtil {
	//######################################################################
	//# Common Header
	//######################################################################
	public static String getHeaderResult() {
		return MContextParameter.getRequestHeader().getString("result");
	}
	
	public static String getHeaderInfoText() {
		return MContextParameter.getRequestHeader().getString("info_text");
	}
	
	public static String getHeaderLoginSessionId() {
		return MContextParameter.getRequestHeader().getString("login_session_id");
	}
	
	//######################################################################
	//# IB Session Context
	//######################################################################
	public static String getLoginUserId() {
		return (String)MContextParameter.getSessionContext().getOrDefault("loginUserId", Strings.EMPTY);
	}
	
	public static String getUserRoleId() {
		return (String)MContextParameter.getSessionContext().getOrDefault("userRoleId", Strings.EMPTY);
	}
	public static String getRequestUri() {
		return (String)MContextParameter.getSessionContext().getOrDefault("requestUri", Strings.EMPTY);
	}
	
}
