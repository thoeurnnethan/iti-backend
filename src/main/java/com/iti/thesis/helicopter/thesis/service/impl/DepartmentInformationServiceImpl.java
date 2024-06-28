package com.iti.thesis.helicopter.thesis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.core.exception.MNotFoundException;
import com.iti.thesis.helicopter.thesis.db.service.DepartmentInformationMapper;
import com.iti.thesis.helicopter.thesis.service.DepartmentInformationService;
import com.iti.thesis.helicopter.thesis.util.MValidatorUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DepartmentInformationServiceImpl implements DepartmentInformationService {
	
	@Autowired
	private DepartmentInformationMapper		departmentInformationMapper;
	
	@Override
	public MData registerDepartmentInformation(MData param) {
		try {
			MData	outputData	= param;
			departmentInformationMapper.registerDepartmentInformation(param);
			return outputData;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	@Override
	public MMultiData retrieveDepartmentInformationList(MData param) throws MException {
		MMultiData outputData = new MMultiData();
		try {
			outputData = departmentInformationMapper.retrieveDepartmentInformationList(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
		return outputData;
	}

	@Override
	public MData retrieveDepartmentInformationTotalCount(MData param) throws MException {
		try {
			return departmentInformationMapper.retrieveDepartmentInformationTotalCount(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}
	
	@Override
	public MData retrieveDepartmentInformationDetail(MData param) throws MException {
		MData outputData = new MData();
		try {
			outputData = departmentInformationMapper.retrieveDepartmentInformationDetail(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
		return outputData;
	}

	@Override
	public MData updateDepartmentInformation(MData param) throws MException {
		try {
			MValidatorUtil.validate(param, "departmentID");
			MData depDetail = this.retrieveAndValidateDepartmentInfo(param);
			if(!depDetail.isEmpty()) {
				departmentInformationMapper.updateDepartmentInformation(param);
			}
			return param;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}
	
	private MData retrieveAndValidateDepartmentInfo(MData param) {
		try {
			MValidatorUtil.validate(param, "departmentID");
			MData depDetail = this.retrieveDepartmentInformationDetail(param);
			return depDetail;
		} catch (MNotFoundException e) {
			return new MData();
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
