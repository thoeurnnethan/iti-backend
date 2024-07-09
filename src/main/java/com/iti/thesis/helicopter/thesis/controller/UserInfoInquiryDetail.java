package com.iti.thesis.helicopter.thesis.controller;

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
import com.iti.thesis.helicopter.thesis.service.UserInfoService;
import com.iti.thesis.helicopter.thesis.util.MResponseUtil;

import lombok.extern.slf4j.Slf4j;;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserInfoInquiryDetail extends BaseTemplate {
	
	@Autowired
	private UserInfoService		userInfoService;
	private final String[] removeKey = {"secretKey","parentID","firstLoginDate", "parentID"
			,"lastLoginDate","statusCode","userPasswordErrorCount"};
	
	@Override
	@PostMapping("/detail")
	public JsonNode onRequest(@RequestBody MData message) throws MException {
		try {
			return super.onProcess(message);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription());
		}
	}

	@Override
	public MData onExecute(MData param) throws MException {
		try {
			return MResponseUtil.removeKey(userInfoService.retrieveUserInfoDetail(param), removeKey);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription());
		}
		
	}
	
}
