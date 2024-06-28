package com.iti.thesis.helicopter.thesis.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.service.UserAuthenticationService;
import com.iti.thesis.helicopter.thesis.util.MResponseUtil;
import com.iti.thesis.helicopter.thesis.util.MValidatorUtil;

import lombok.extern.slf4j.Slf4j;;

@Slf4j
@RestController
@RequestMapping("/public/api/user")
public class UserInfoLogin extends BaseTemplate {
	
	@Autowired
	private UserAuthenticationService userAuthenticationService;
	
	private final String[] removeKey = {"userID","passwd","secretKey","studentID","firstLoginDate", "parentID"
			,"lastLoginDate","lastChangeDate","lastChangeTime","statusCode","email","phone"};
	
	
	@Override
	@PostMapping("/login")
	public JsonNode onRequest(@RequestBody MData message) throws MException {
		try {
			return super.onProcess(message);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.toString());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription());
		}
	}
	
	@Override
	public MData onExecute(MData param) throws MException {
		try {
			MValidatorUtil.validate(param, "userID", "password");
			param.put("password", URLDecoder.decode(param.getString("password"), StandardCharsets.UTF_8.toString()));
			return MResponseUtil.removeKey(userAuthenticationService.userLogin(param), removeKey);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription());
		}
	}
	
}
