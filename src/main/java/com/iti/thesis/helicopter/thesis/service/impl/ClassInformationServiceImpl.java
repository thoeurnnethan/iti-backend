package com.iti.thesis.helicopter.thesis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.common.ErrorCode.ErrorCode;
import com.iti.thesis.helicopter.thesis.constant.ConstantCodePrefix;
import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.core.exception.MNotFoundException;
import com.iti.thesis.helicopter.thesis.db.service.ClassInformationMapper;
import com.iti.thesis.helicopter.thesis.db.service.DepartmentInformationMapper;
import com.iti.thesis.helicopter.thesis.service.ClassInformationService;
import com.iti.thesis.helicopter.thesis.util.MValidatorUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ClassInformationServiceImpl implements ClassInformationService {
	
	@Autowired
	private ClassInformationMapper		classInformationMapper;
	@Autowired
	private DepartmentInformationMapper	departmentInformationMapper;
	
	@Override
	public MMultiData retrieveClassInformationList(MData param) throws MException {
		try {
			return classInformationMapper.retrieveClassInformationList(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	@Override
	public MData retrieveClassInformationTotalCount(MData param) throws MException {
		try {
			return classInformationMapper.retrieveClassInformationTotalCount(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	@Override
	public MMultiData retrieveClassInformationListForDownload(MData param) throws MException {
		try {
			return classInformationMapper.retrieveClassInformationListForDownload(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	@Override
	public MMultiData retrieveClassInformationStudentList(MData param) throws MException {
		try {
			MValidatorUtil.validate(param, "classID");
			return classInformationMapper.retrieveClassInformationStudentList(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	@Override
	public MData registerClassInformation(MData param) {
		MData	outputData		= param;
		try {
			MValidatorUtil.validate(param, "departmentID","cyear","semester", "className","generation");
			MData	departmentInfo	= departmentInformationMapper.retrieveDepartmentInformationDetail(param);
			if(!departmentInfo.isEmpty()) {
				String classID = this.retrieveLastClassID(param);
				param.setString("classID", classID);
				param.setString("classInfoID", classID + param.getString("cyear")+param.getString("semester"));
				classInformationMapper.registerClassInformation(param);
			}
			return outputData;
		} catch (MNotFoundException e) {
			throw new MException(ErrorCode.DEPARTMENT_NOT_FOUND.getValue(), ErrorCode.DEPARTMENT_NOT_FOUND.getDescription());
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}
	
	private String retrieveLastClassID(MData param) {
		int result = 0 ;
		try {
			MData	classInfo	= classInformationMapper.retrieveLastClassID(param);
			String	resultStr	= classInfo.getString("classID");
			resultStr	= resultStr.substring(ConstantCodePrefix.CLASS.getValue().length(), resultStr.length());
			result		= Integer.valueOf(resultStr);
			result ++;
		} catch (MNotFoundException e) {
			result = 1001;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
		return ConstantCodePrefix.CLASS.getValue() + String.valueOf(String.format("%04d", result));
	}
	
	@Override
	public MData retrieveClassInformationDetail(MData param) throws MException {
		MData outputData = new MData();
		try {
			outputData = classInformationMapper.retrieveClassInformationDetail(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
		return outputData;
	}

	@Override
	public MData updateClassInformation(MData param) throws MException {
		try {
			MValidatorUtil.validate(param, "classID","departmentID","className","year","generation","semester","statusCode");
			MData classInfo = classInformationMapper.retrieveClassInformationDetail(param);
			if(!classInfo.isEmpty()) {
				classInformationMapper.updateClassInformation(param);
			}
			return param;
		} catch (MNotFoundException e) {
			throw new MException(ErrorCode.CLASS_NOT_FOUND.getValue(), ErrorCode.CLASS_NOT_FOUND.getDescription());
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

}
