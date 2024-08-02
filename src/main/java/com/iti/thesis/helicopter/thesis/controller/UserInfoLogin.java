package com.iti.thesis.helicopter.thesis.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.iti.thesis.helicopter.thesis.constant.YnTypeCode;
import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.service.UserAuthenticationService;
import com.iti.thesis.helicopter.thesis.util.MResponseUtil;
import com.iti.thesis.helicopter.thesis.util.MStringUtil;
import com.iti.thesis.helicopter.thesis.util.MValidatorUtil;

import lombok.extern.slf4j.Slf4j;;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserInfoLogin extends BaseTemplate {
	
	@Autowired
	private UserAuthenticationService userAuthenticationService;
	
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
			return this.prepareResponse(userAuthenticationService.userLogin(param));
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription());
		}
	}
	
	private MData prepareResponse(MData response) {
		String[] removeKey = {"firstLoginDate","lastLoginDate","statusCode","userPasswordErrorCount","lockDateTime"};
		String	loginByUserYn = response.getString("loginByUserYn");
		if(!MStringUtil.isEmpty(loginByUserYn) && YnTypeCode.YES.getValue().equals(loginByUserYn)) {
			List<String> removeKeyList = new ArrayList<>(Arrays.asList(removeKey));
			removeKeyList.add("passwd");
			removeKey = removeKeyList.toArray(new String[0]);
		}
		return MResponseUtil.removeKey(response, removeKey);
	}
	
}
