package com.iti.thesis.helicopter.thesis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iti.thesis.helicopter.thesis.common.SessionUtil;
import com.iti.thesis.helicopter.thesis.context.MContextHolder;
import com.iti.thesis.helicopter.thesis.context.parameter.MContextParameter;
import com.iti.thesis.helicopter.thesis.context.util.MHttpRequestUtil;
import com.iti.thesis.helicopter.thesis.core.Json.JsonAdaptorObject;
import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.util.MGUIDUtil;
import com.iti.thesis.helicopter.thesis.util.MStringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public abstract class BaseTemplate {
	
	@Autowired
	protected ObjectMapper						objectMapper;
	@Autowired
	private PlatformTransactionManager			txManager;
	
	public final JsonNode onProcess(MData param) {
		
		//======================================================
		//# initialize context
		//======================================================
		MContextParameter.initMContextParameter();
		//======================================================
		//# create session data
		//======================================================
		JsonAdaptorObject obj = new JsonAdaptorObject();
		try {
			obj = MHttpRequestUtil.createSession(param);
		} catch (MException e) {
			MContextHolder.clear();
			return makeFailResponse(param, e.getMCode(), e.getMMessage());
		}
		//======================================================
		//# initialize default transaction
		//======================================================
		String transactionName = "TX_MAIN"+ MGUIDUtil.generateGUID();
		DefaultTransactionAttribute defaultTransactionAttribute = new DefaultTransactionAttribute();
		defaultTransactionAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		defaultTransactionAttribute.setName(transactionName);
		TransactionStatus	txStatus	= txManager.getTransaction(defaultTransactionAttribute);
		log.info("Open Transaction :: {}", transactionName);
		//======================================================
		//# define essential variable
		//======================================================
		JsonNode			metaNode			= obj.get(JsonAdaptorObject.TYPE.META);
		MData				requestMessage		= getRequestParams(obj);
		MData				requestHeader		= requestMessage.getMData("header");
		MData				requestBody			= requestMessage.getMData("body");
		MData				responseBody		= new MData();
		String				adapterErrorCode	= MStringUtil.EMPTY;
		String				adapterErrorMessage	= MStringUtil.EMPTY;
		//======================================================
		//# context data
		//======================================================
		// keep request object
		MContextParameter.setContext(obj);
		// keep header into context 
		MContextParameter.setRequestHeader(requestHeader);
		// init session context
		if (!"Unknown".equals(SessionUtil.getSessionId())) {
			MContextParameter.setSessionContext(convertPojoToMData(metaNode));
		}
		try {
//			MHttpRequestUtil.checkAuthorization(responseBody);
			
			responseBody = onExecute(requestBody);
			JsonNode response = prepareResponse(requestMessage, responseBody);
			txManager.commit(txStatus);
			log.info("Commit Transaction :: {}", transactionName);
			JsonNode j= makeResponse(requestMessage, response);
			return j;
		} catch (MException e) {
			txManager.rollback(txStatus);
			log.info("Rollback Transaction :: {}", transactionName);
			log.error(e.getMessage(), e);
			adapterErrorCode	= e.getMCode();
			adapterErrorMessage	= e.getMMessage();
		} catch(Exception e){
			txManager.rollback(txStatus);
			log.info("Rollback Transaction :: {}", transactionName);
			log.error(e.getMessage(), e);
			adapterErrorCode	= CommonErrorCode.UNCAUGHT.getCode();
			adapterErrorMessage	= CommonErrorCode.UNCAUGHT.getDescription();
		} finally {
			MContextHolder.clear();
		}
		return makeFailResponse(requestMessage, adapterErrorCode, adapterErrorMessage);
	}
	
	public abstract JsonNode onRequest(MData requestBody) throws MException;
	public abstract MData onExecute(MData requestBody) throws MException;
	
	public MData convertPojoToMData(Object obj) {
		if (obj == null) {
			return new MData();
		}
		return objectMapper.convertValue(obj, MData.class);
	}
	
	public <T> T convertMDataToPojo(MData data, Class<T> obj) {
		if (data == null || data.isEmpty()) {
			return objectMapper.convertValue(new MData(), obj);
		}
		return objectMapper.convertValue(data, obj);
	}
	
	public JsonNode toJsonNode(Object obj) {
		return objectMapper.valueToTree(obj);
	}
	
	private JsonNode prepareResponse(MData requestMessage, MData reponseBody) {
		MData	resObj		= new MData();
		MData	reqHeader	= requestMessage.getMData("header");
		if (SessionUtil.isLoggedin()) {
			reqHeader.setString("login_session_id", SessionUtil.getSessionId());
		}
		reqHeader.setBoolean("result", true);
		resObj.setMData("header", reqHeader);
		resObj.setMData("body", reponseBody);
		return toJsonNode(resObj);
	}
	
	public final JsonNode makeResponse(MData resObj,JsonNode response) {
		return response;
	}
	
	public final JsonNode makeFailResponse(MData obj, String errorCode, String errorText) {
		MData	res			= new MData();
		MData	resHeader	= obj.getMData("header");
		resHeader.setBoolean("result", false);
		resHeader.setString("error_code", errorCode);
		resHeader.setString("error_text", errorText);
		res.setMData("header", resHeader);
		res.setMData("body", null);
		return toJsonNode(res);
	}
	
	private MData getRequestParams(JsonAdaptorObject obj) {
		JsonNode	rootNode	= obj.get(JsonAdaptorObject.TYPE.REQUEST);
		return new MData(convertPojoToMData(rootNode));
	}
	
}
