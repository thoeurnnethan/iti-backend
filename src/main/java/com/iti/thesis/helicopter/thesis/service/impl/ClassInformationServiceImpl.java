package com.iti.thesis.helicopter.thesis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
			log.error(e.getMessage(), e);
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
	public MData registerClassInformation(MData param) {
		try {
			MValidatorUtil.validate(param, "departmentID", "className","cyear","generation","semester");
			
			MData	outputData		= param;
			MData	departmentInfo	= departmentInformationMapper.retrieveDepartmentInformationDetail(param);
			if(!departmentInfo.isEmpty()) {
				int classID = this.retrieveLastClassID(param);
				param.setInt("classID", classID);
				classInformationMapper.registerClassInformation(param);
			}
			return outputData;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}
	
	private int retrieveLastClassID(MData param) {
		try {
			MData classInfo = classInformationMapper.retrieveLastClassID(param);
			int classID = classInfo.getInt("classID");
			classID++;
			return classID;
		} catch (MNotFoundException e) {
			return 1001;
		}
	}
	
	private MData retrieveAndValidateClassInfo(MData param) {
		try {
			return this.retrieveClassInformationDetail(param);
		} catch (MNotFoundException e) {
			return new MData();
		}
	}
	
	@Override
	public MData retrieveClassInformationDetail(MData param) throws MException {
		MData outputData = new MData();
		try {
			outputData = classInformationMapper.retrieveClassInformationDetail(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
		return outputData;
	}

	@Override
	public MData updateClassInformation(MData param) throws MException {
		try {
			MValidatorUtil.validate(param, "classID","departmentID");
			MData classInfo = classInformationMapper.retrieveClassInformationDetail(param);
			if(!classInfo.isEmpty()) {
				classInformationMapper.updateClassInformation(param);
			}
			return classInfo;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	@Override
	public MData retrieveLastNewsEventID(MData param) throws MException {
		return null;
	}

}
