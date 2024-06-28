package com.iti.thesis.helicopter.thesis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MDuplicateException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.core.exception.MNotFoundException;
import com.iti.thesis.helicopter.thesis.db.service.DepartmentManagementMapper;
import com.iti.thesis.helicopter.thesis.service.DepartmentManagementService;
import com.iti.thesis.helicopter.thesis.util.MNullUtil;
import com.iti.thesis.helicopter.thesis.util.MValidatorUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DepartmentManagementServiceImpl implements DepartmentManagementService {
	
	@Autowired
	private DepartmentManagementMapper		departmentManagementMapper;
	
	@Override
	public MMultiData retrieveDepartmentManagementList(MData param) throws MException {
		try {
			return departmentManagementMapper.retrieveDepartmentManagementList(param);
		} catch (MException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	@Override
	public MData retrieveDepartmentManagementTotalCount(MData param) throws MException {
		try {
			return departmentManagementMapper.retrieveDepartmentManagementTotalCount(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	@Override
	public MData registerDepartmentManagement(MData param) {
		try {
			MData	outputData		= param;
			MData	depManagement	= this.retrieveAndValidateDepartmentManagement(param);
			if(!MNullUtil.isNull(depManagement)) {
				throw new MDuplicateException(CommonErrorCode.DUPLICATED_DATA.getCode(), "Already register");
			}
			departmentManagementMapper.registerDepartmentManagement(param);
			return outputData;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}
	
	private MData retrieveAndValidateDepartmentManagement(MData param) {
		try {
			return this.retrieveDepartmentManagementDetail(param);
		} catch (MNotFoundException e) {
			return new MData();
		}
	}
	
	@Override
	public MData retrieveDepartmentManagementDetail(MData param) throws MException {
		MData outputData = new MData();
		try {
			outputData = departmentManagementMapper.retrieveDepartmentManagementDetail(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
		return outputData;
	}

	@Override
	public MData updateDepartmentManagement(MData param) throws MException {
		try {
			MValidatorUtil.validate(param, "teacherID","departmentID");
			MData depMngtInfo = this.retrieveAndValidateDepartmentManagement(param);
			if(!depMngtInfo.isEmpty()) {
				departmentManagementMapper.updateDepartmentManagement(param);
			}
			return depMngtInfo;
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
