package com.iti.thesis.helicopter.thesis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.service.ClassInformationService;;

@RestController
@RequestMapping("/api/class-info")
public class ClassInformationInquiryList extends BaseTemplate {
	
	@Autowired
	private ClassInformationService		classInformationService;

	@Override
	@PostMapping("/list")
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
			MMultiData	depList		= classInformationService.retrieveClassInformationList(param);
			MData		resCount	= classInformationService.retrieveClassInformationTotalCount(param);
			return prepareResponse(depList, resCount);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription());
		}
	}
	
	private MData prepareResponse(MMultiData depList, MData resCount) {
		MData response = new MData();
		response.setInt("totalCount", resCount.getInt("totalCount"));
		response.setMMultiData("classList", depList);
		return response;
	}

}
