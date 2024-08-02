package com.iti.thesis.helicopter.thesis.context.util;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iti.thesis.helicopter.thesis.common.SessionUtil;
import com.iti.thesis.helicopter.thesis.common.ErrorCode.ErrorCode;
import com.iti.thesis.helicopter.thesis.constant.UserRoleCode;
import com.iti.thesis.helicopter.thesis.context.constant.AdminUriAccessConst;
import com.iti.thesis.helicopter.thesis.context.constant.DepManagerUriAccessConst;
import com.iti.thesis.helicopter.thesis.context.constant.StudentUriAccessConst;
import com.iti.thesis.helicopter.thesis.context.constant.TeacherUriAccessConst;
import com.iti.thesis.helicopter.thesis.context.parameter.MContextParameter;
import com.iti.thesis.helicopter.thesis.core.Json.JsonAdaptorObject;
import com.iti.thesis.helicopter.thesis.core.Json.JsonUtil;
import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.util.MStringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
			HttpServletRequest	request			= MHttpRequestUtil.getCurrentRequest();
			HttpSession			session			= request.getSession(true); 
			
//			MHttpRequestUtil.checkAuthentication(request, session, param);
			
			JsonAdaptorObject	obj				= new JsonAdaptorObject();
			MHttpRequestUtil.initMetaData(obj);
			String				sessionID		= session.getId();
			Long				date			= session.getCreationTime();
			String				requestUri		= request.getRequestURI();
			boolean				isLoginRequest	= requestUri.contains("/login");
			JsonNode			jsonNode	= (ObjectNode)obj.get(JsonAdaptorObject.TYPE.META);
			JsonUtil.putValue((ObjectNode) jsonNode, "id", sessionID);
			JsonUtil.putValue((ObjectNode) jsonNode, "created_datetime", date.toString());
			JsonUtil.putValue((ObjectNode) jsonNode, "requestUri", requestUri);
			obj.put(JsonAdaptorObject.TYPE.META, jsonNode);
			obj.put(JsonAdaptorObject.TYPE.REQUEST, convertMDataToJsonNode(param));
			if(!isLoginRequest) {
				MData sessionData = MHttpRequestUtil.getSessionAttribute(sessionID);
				JsonUtil.putValue((ObjectNode) jsonNode, "loginUserId", sessionData.getString("loginUserId"));
				JsonUtil.putValue((ObjectNode) jsonNode, "userRoleId", sessionData.getString("userRoleId"));
				obj.put(JsonAdaptorObject.TYPE.META, jsonNode);
				MContextParameter.setContext(obj);
				SessionUtil.updateSession(convertJsonNodeToMData(jsonNode));
			}
			return obj;
		} catch (MException e) {
			throw new MException(e.getMCode(), e.getMMessage());
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			throw new MException("00000", "There are problem while working with session");
		}
	}
	
	private static void checkAuthentication(HttpServletRequest request, HttpSession session, MData param) {
		String	requestUri		= request.getRequestURI();
		String	sessionID		= session.getId();
		String	userSessionID	= param.getMData("header").getString("login_session_id");
		boolean	isLoginRequest	= requestUri.contains("/login") ? true : false;
		if(!isLoginRequest) {
			if(!MStringUtil.isEmpty(userSessionID) && !MStringUtil.isEmpty(sessionID)) {
				if(!userSessionID.equals(sessionID)) {
					throw new MException("401", HttpStatus.UNAUTHORIZED.getReasonPhrase());
				}
			}
		}
	}
	
	private static void initMetaData(JsonAdaptorObject obj) {
		MData		defaultMeta	= new MData();
		defaultMeta.setString("id", MStringUtil.EMPTY);
		defaultMeta.setString("created_datetime", MStringUtil.EMPTY);
		defaultMeta.setString("loginUserId", MStringUtil.EMPTY);
		defaultMeta.setString("userRoleId", MStringUtil.EMPTY);
		defaultMeta.setString("requestUri", MStringUtil.EMPTY);
		obj.put(JsonAdaptorObject.TYPE.META, convertMDataToJsonNode(defaultMeta));
	}
	
	public static void setSessionAttribute(MData sessionAtrrData) {
		HttpServletRequest	request			= MHttpRequestUtil.getCurrentRequest();
		HttpSession			session			= request.getSession(false);
		session.setAttribute("prefix_" + session.getId(), sessionAtrrData);
	}
	
	public static MData getSessionAttribute(String sessionID) {
		HttpServletRequest	request			= MHttpRequestUtil.getCurrentRequest();
		HttpSession			session			= request.getSession(false);
		return (MData) session.getAttribute("prefix_" + sessionID);
	}
	
	private static JsonNode convertMDataToJsonNode(MData data) {
		return objectMapper.convertValue(data, JsonNode.class);
	}
	
	private static MData convertJsonNodeToMData(JsonNode data) {
		return objectMapper.convertValue(data, MData.class);
	}

	public static void checkAuthorization(MData input) {
		try {
			if (MStringUtil.isEmpty(MContextUtil.getLoginUserId())) {
				throw new MException(ErrorCode.INVALID_ACCESS.getValue(), ErrorCode.INVALID_ACCESS.getDescription());
			}
			
			String loginUserRoleID	= MContextUtil.getUserRoleId();
			if(UserRoleCode.ADMIN.getValue().equals(loginUserRoleID)) {
				if(!AdminAccessUri()) throw new MException(ErrorCode.NEED_PERMISSION.getValue(), ErrorCode.NEED_PERMISSION.getDescription());
			}else if(UserRoleCode.DEP_MANAGER.getValue().equals(loginUserRoleID)) {
				if(!DepManagerAccessUri()) throw new MException(ErrorCode.NEED_PERMISSION.getValue(), ErrorCode.NEED_PERMISSION.getDescription());
			}else if(UserRoleCode.TEACHER.getValue().equals(loginUserRoleID)) {
				if(!TeacherAccessUri()) throw new MException(ErrorCode.NEED_PERMISSION.getValue(), ErrorCode.NEED_PERMISSION.getDescription());
			}else if(UserRoleCode.STUDENT.getValue().equals(loginUserRoleID)) {
				if(!StudentAccessUri()) throw new MException(ErrorCode.NEED_PERMISSION.getValue(), ErrorCode.NEED_PERMISSION.getDescription());
			}else {
				throw new MException(ErrorCode.INVALID_ROLE.getValue(), ErrorCode.INVALID_ROLE.getDescription());
			}
		} catch (MException e) {
			throw e;
		}
	}
	
	private static boolean AdminAccessUri() {
		return Arrays.asList(
				AdminUriAccessConst.USER_LOGIN
				).contains( MContextUtil.getRequestUri() ); 
	}
	
	private static boolean DepManagerAccessUri() {
		return Arrays.asList(
				DepManagerUriAccessConst.USER_LOGIN
				).contains( MContextUtil.getRequestUri() ); 
	}
	
	private static boolean TeacherAccessUri() {
		return Arrays.asList(
				TeacherUriAccessConst.USER_LOGIN
				).contains( MContextUtil.getRequestUri() ); 
	}
	
	private static boolean StudentAccessUri() {
		return Arrays.asList(
				StudentUriAccessConst.USER_LOGIN,
				StudentUriAccessConst.USER_LIST
				).contains( MContextUtil.getRequestUri() ); 
	}
	
}
