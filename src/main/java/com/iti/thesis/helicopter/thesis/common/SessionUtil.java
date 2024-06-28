package com.iti.thesis.helicopter.thesis.common;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iti.thesis.helicopter.thesis.context.constant.SessionDataConst;
import com.iti.thesis.helicopter.thesis.context.parameter.MContextParameter;
import com.iti.thesis.helicopter.thesis.core.Json.JsonAdaptorObject;
import com.iti.thesis.helicopter.thesis.core.Json.JsonUtil;
import com.iti.thesis.helicopter.thesis.core.collection.MData;

public class SessionUtil {
	public static void updateSession(MData input) {
		JsonAdaptorObject	obj			= MContextParameter.getContext();
		ObjectNode			sessionNode	= (ObjectNode)obj.get(JsonAdaptorObject.TYPE.META);
		for (String key : SessionDataConst.META_DATA_FIELD) {
			if (input.containsKey(key)) {
				JsonUtil.putValue(sessionNode, key, input.get(key));
			}
		}
		obj.put(JsonAdaptorObject.TYPE.META, sessionNode);
	}
	
	public static String getSessionId() {
		JsonAdaptorObject	obj			= MContextParameter.getContext();
		String				sessionId	= "";
		try {
			ObjectNode sessionNode = (ObjectNode)obj.get(JsonAdaptorObject.TYPE.META);
			if (sessionNode != null) {
				sessionId = sessionNode.get("id").textValue();
			}
		} catch (Exception e) {
			//
		}
		return sessionId;
	}
	
	public static boolean isLoggedin() {
		JsonAdaptorObject	obj			= MContextParameter.getContext();
		boolean				loginStatus	= false;
		
		try {
			ObjectNode sessionNode = (ObjectNode)obj.get(JsonAdaptorObject.TYPE.META);
			if (sessionNode != null) {
				String loginId = sessionNode.get("loginUserId").textValue();
				if (StringUtils.isNotEmpty(loginId)) {
					loginStatus = true;
				}
			}
		} catch (Exception e) {
			//
		}
		return loginStatus;
	}
}
