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
import com.iti.thesis.helicopter.thesis.service.DepartmentManagementService;
import com.iti.thesis.helicopter.thesis.util.MResponseUtil;;

@RestController
@RequestMapping("/api/department-mngt")
public class DepartmentManagementInquiryList extends BaseTemplate {
	
	@Autowired
	private DepartmentManagementService		departmentManagementService;

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
			MMultiData	depList		= departmentManagementService.retrieveDepartmentManagementList(param);
			return prepareResponse(depList);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription());
		}
	}
	
	private MData prepareResponse(MMultiData depList) {
		MData response = new MData();
		String[] emptyKey = {"teacherID", "userRoleID", "firstName", "lastName"};
		response.setInt("totalCount", depList.size());
		response.setMMultiData("departmentList", MResponseUtil.responseEmptyKey(depList, emptyKey));
		return response;
	}

}
