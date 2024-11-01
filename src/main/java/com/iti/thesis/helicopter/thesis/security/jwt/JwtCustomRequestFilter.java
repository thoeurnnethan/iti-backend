package com.iti.thesis.helicopter.thesis.security.jwt;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iti.thesis.helicopter.thesis.common.ErrorCode.ErrorCode;
import com.iti.thesis.helicopter.thesis.constant.UserRoleCode;
import com.iti.thesis.helicopter.thesis.context.constant.UriConst;
import com.iti.thesis.helicopter.thesis.context.parameter.MContextParameter;
import com.iti.thesis.helicopter.thesis.context.util.MContextUtil;
import com.iti.thesis.helicopter.thesis.core.Json.JsonAdaptorObject;
import com.iti.thesis.helicopter.thesis.core.Json.JsonUtil;
import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.util.MStringUtil;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtCustomRequestFilter {
	
	private static JwtUtil			jwtUtil;
	private static ObjectMapper		objectMapper;
	
	public JwtCustomRequestFilter(ObjectMapper objectMapper, JwtUtil jwtUtil) {
		JwtCustomRequestFilter.objectMapper = objectMapper;
		JwtCustomRequestFilter.jwtUtil = jwtUtil;
	}
	
	@SuppressWarnings("null")
	public static HttpServletRequest getCurrentRequest() {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (!MStringUtil.isNull(attrs)) {
			return attrs.getRequest();
		}
		return null;
	}
	
	public static JsonAdaptorObject validateRequest(MData param) {
		try {
			HttpServletRequest	request			= JwtCustomRequestFilter.getCurrentRequest();
			String				requestUri		= request.getRequestURI();
			
			MData sessionContext = new MData();
			sessionContext.setString("requestUri", requestUri);
			MContextParameter.setSessionContext(sessionContext);
			
			JsonAdaptorObject	obj				= new JsonAdaptorObject();
			JwtCustomRequestFilter.initMetaData(obj);
			
			MData userToken = new MData();
			JwtCustomRequestFilter.checkAuthentication(param, userToken);
			
			String				date			= param.getMData("header").getString("created_datetime");
			JsonNode			jsonNode	= (ObjectNode)obj.get(JsonAdaptorObject.TYPE.META);
			JsonUtil.putValue((ObjectNode) jsonNode, "id", request.getSession().getId());
			JsonUtil.putValue((ObjectNode) jsonNode, "created_datetime", date);
			JsonUtil.putValue((ObjectNode) jsonNode, "loginUserId", MStringUtil.isEmpty(userToken.getString("userID")) ? MStringUtil.EMPTY : userToken.getString("userID"));
			JsonUtil.putValue((ObjectNode) jsonNode, "userRoleId", MStringUtil.isEmpty(userToken.getString("userRoleID")) ? MStringUtil.EMPTY : userToken.getString("userRoleID"));
			JsonUtil.putValue((ObjectNode) jsonNode, "requestUri", requestUri);
			obj.put(JsonAdaptorObject.TYPE.META, jsonNode);
			obj.put(JsonAdaptorObject.TYPE.REQUEST, convertMDataToJsonNode(param));
			return obj;
		} catch (MException e) {
			throw new MException(e.getMCode(), e.getMMessage());
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			throw new MException(HttpStatus.UNAUTHORIZED.value() + "", "There are problem while working with session");
		}
	}
	
	private static JsonNode convertMDataToJsonNode(MData data) {
		return objectMapper.convertValue(data, JsonNode.class);
	}
	
	private static void initMetaData(JsonAdaptorObject obj) {
		MData defaultMeta = new MData();
		defaultMeta.setString("id", MStringUtil.EMPTY);
		defaultMeta.setString("created_datetime", MStringUtil.EMPTY);
		defaultMeta.setString("loginUserId", MStringUtil.EMPTY);
		defaultMeta.setString("userRoleId", MStringUtil.EMPTY);
		defaultMeta.setString("requestUri", MStringUtil.EMPTY);
		obj.put(JsonAdaptorObject.TYPE.META, convertMDataToJsonNode(defaultMeta));
	}
	
	private static void checkAuthentication(MData param, MData response) {
		if(!JwtCustomRequestFilter.AllowAccessUri()) {
			JwtCustomRequestFilter.validateToken(param.getMData("header"), response);
		}
	}
	
	private static void validateToken(MData header, MData response) {
		String token = header.getString("login_session_id");
		if(MStringUtil.isEmpty(token)) {
			throw new MException("401", HttpStatus.UNAUTHORIZED.getReasonPhrase());
		}else {
			Claims claims = jwtUtil.extractAllClaims(token);
			boolean isValidToken = jwtUtil.validateToken(token, (String)claims.get("userID"));
			if(!isValidToken) {
				throw new MException("401", HttpStatus.UNAUTHORIZED.getReasonPhrase());
			}
			response.setString("userID", (String) claims.get("userID"));
			response.setString("userRoleID", (String) claims.get("userRoleID"));
		}
	}
	
	public static void checkAuthorization(MData input) {
		try {
			if(!JwtCustomRequestFilter.AllowAccessUri()) {
				if (MStringUtil.isEmpty(MContextUtil.getLoginUserId())) {
					throw new MException(ErrorCode.INVALID_ACCESS.getValue(), ErrorCode.INVALID_ACCESS.getDescription());
				}
				
				String loginUserRoleID = MContextUtil.getUserRoleId();
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
			}
			
		} catch (MException e) {
			throw e;
		}
	}
	
	private static boolean AllowAccessUri() {
		return Arrays.asList(
				UriConst.USER_LOGIN,
				UriConst.USER_LOGOUT,
				UriConst.USER_CHANGE_PASSWORD
			).contains(MContextUtil.getRequestUri());
	}
	
	private static boolean AdminAccessUri() {
		return Arrays.asList(
				UriConst.USER_LIST,
				UriConst.USER_DETAIL,
				UriConst.USER_DOWNLOAD,
				UriConst.USER_UPDATE,
				UriConst.USER_RESET_PASSWORD,
				UriConst.USER_REGISTER,
				
				UriConst.DEPARTMENT_LIST,
				UriConst.DEPARTMENT_DOWNLOAD,
				UriConst.DEPARTMENT_REGISTER,
				UriConst.DEPARTMENT_UDPATE,
				
				UriConst.TEACHER_MAP_LIST,
				UriConst.TEACHER_MAP_REGISTER,
				UriConst.TEACHER_MAP_UDPATE,
				
				UriConst.ROOM_LIST,
				UriConst.ROOM_DOWNLOAD,
				UriConst.ROOM_REGISTER,
				UriConst.ROOM_UDPATE,
				
				UriConst.CLASS_LIST,
				UriConst.CLASS_DOWNLOAD,
				UriConst.CLASS_LIST_STUDENT,
				UriConst.CLASS_REGISTER,
				UriConst.CLASS_REGISTER_STUDENT,
				UriConst.CLASS_UDPATE,
				UriConst.CLASS_UPDATE_STUDENT,
				UriConst.CLASS_UPGRADE,
				
				UriConst.SCHEDULE_LIST,
				UriConst.SCHEDULE_REGISTER,
				UriConst.SCHEDULE_VALIDATE,
				UriConst.SCHEDULE_DELETE,
				
				UriConst.SCORE_LIST,
				UriConst.SCORE_REGISER,
				
				UriConst.SCHEDULE_LIST,
				UriConst.SUBJECT_LIST,
				UriConst.SUBJECT_DOWNLOAD,
				UriConst.SUBJECT_REGISTER,
				UriConst.SUBJECT_UDPATE
			).contains( MContextUtil.getRequestUri()); 
	}
	
	private static boolean DepManagerAccessUri() {
		return Arrays.asList(
				UriConst.USER_LIST,
				UriConst.USER_DETAIL,
				UriConst.USER_DOWNLOAD,
				UriConst.USER_RESET_PASSWORD,
				
				UriConst.DEPARTMENT_LIST,
				UriConst.DEPARTMENT_DOWNLOAD,
				
				UriConst.TEACHER_MAP_LIST,
				UriConst.TEACHER_MAP_REGISTER,
				UriConst.TEACHER_MAP_UDPATE,
				
				UriConst.ROOM_LIST,
				UriConst.ROOM_DOWNLOAD,
				
				UriConst.CLASS_LIST,
				UriConst.CLASS_DOWNLOAD,
				UriConst.CLASS_LIST_STUDENT,
				UriConst.CLASS_REGISTER,
				UriConst.CLASS_REGISTER_STUDENT,
				UriConst.CLASS_UDPATE,
				UriConst.CLASS_UPDATE_STUDENT,
				UriConst.CLASS_UPGRADE,
				
				UriConst.SCHEDULE_LIST,
				UriConst.SCHEDULE_REGISTER,
				UriConst.SCHEDULE_VALIDATE,
				UriConst.SCHEDULE_DELETE,
				
				UriConst.SCORE_LIST,
				UriConst.SCORE_REGISER,
				
				UriConst.SUBJECT_LIST,
				UriConst.SUBJECT_DOWNLOAD,
				UriConst.SUBJECT_REGISTER,
				UriConst.SUBJECT_UDPATE
			).contains( MContextUtil.getRequestUri() );
	}
	
	private static boolean TeacherAccessUri() {
		return Arrays.asList(
				UriConst.USER_LIST,
				
				UriConst.DEPARTMENT_LIST,
				UriConst.DEPARTMENT_DOWNLOAD,
				
				UriConst.TEACHER_MAP_LIST,
				
				UriConst.ROOM_LIST,
				UriConst.ROOM_DOWNLOAD,
				
				UriConst.CLASS_LIST,
				UriConst.CLASS_LIST_STUDENT,
				UriConst.CLASS_DOWNLOAD,
				
				UriConst.SCORE_LIST,
				UriConst.SCORE_REGISER,
				
				UriConst.SCHEDULE_LIST,
				
				UriConst.SUBJECT_LIST,
				UriConst.SUBJECT_DOWNLOAD
			).contains( MContextUtil.getRequestUri()); 
	}
	
	private static boolean StudentAccessUri() {
		return Arrays.asList(
				UriConst.DEPARTMENT_LIST,
				
				UriConst.TEACHER_MAP_LIST,
				
				UriConst.ROOM_LIST,
				UriConst.ROOM_DOWNLOAD,
				
				UriConst.SCORE_LIST,
				
				UriConst.CLASS_LIST,
				UriConst.CLASS_LIST_STUDENT,
				
				UriConst.SCHEDULE_LIST,
				
				UriConst.SUBJECT_LIST,
				UriConst.SUBJECT_DOWNLOAD
			).contains( MContextUtil.getRequestUri() ); 
	}
}
