package com.iti.thesis.helicopter.thesis.context.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iti.thesis.helicopter.thesis.context.parameter.MContextParameter;
import com.iti.thesis.helicopter.thesis.core.Json.JsonAdaptorObject;
import com.iti.thesis.helicopter.thesis.core.Json.JsonUtil;
import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.util.MStringUtil;

@Component
public class MHttpRequestUtil {
	
	private static ObjectMapper		objectMapper;
	public MHttpRequestUtil(ObjectMapper objectMapper) {
		MHttpRequestUtil.objectMapper = objectMapper;
	}
	
	public static HttpServletRequest getCurrentRequest() {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (!MStringUtil.isNull(attrs)) {
			return attrs.getRequest();
		}
		return null;
	}
	
	public static JsonAdaptorObject createSession(MData param) {
		try {
			HttpServletRequest	request		= MHttpRequestUtil.getCurrentRequest();
			HttpSession			session		= request.getSession(true);
			MData				defaultMeta	= new MData();
			JsonAdaptorObject	obj			= new JsonAdaptorObject();
			defaultMeta.setString("id", MStringUtil.EMPTY);
			defaultMeta.setString("created_datetime", MStringUtil.EMPTY);
			obj.put(JsonAdaptorObject.TYPE.META, convertMDataToJsonNode(defaultMeta));
			
			String				sessionID	= session.getId();
			Long				date		= session.getCreationTime();
			JsonNode			jsonNode	= (ObjectNode)obj.get(JsonAdaptorObject.TYPE.META);
			JsonUtil.putValue((ObjectNode) jsonNode, "id", sessionID);
			JsonUtil.putValue((ObjectNode) jsonNode, "created_datetime", date.toString());
			obj.put(JsonAdaptorObject.TYPE.META, jsonNode);
			obj.put(JsonAdaptorObject.TYPE.REQUEST, convertMDataToJsonNode(param));
			MContextParameter.setSessionContext(convertJsonNodeToMData(jsonNode));
			return obj;
		} catch (Exception e) {
			throw new MException("00017", "Error creating session");
		}
	}
	
	private static JsonNode convertMDataToJsonNode(MData data) {
		return objectMapper.convertValue(data, JsonNode.class);
	}
	private static MData convertJsonNodeToMData(JsonNode data) {
		return objectMapper.convertValue(data, MData.class);
	}
	
}
