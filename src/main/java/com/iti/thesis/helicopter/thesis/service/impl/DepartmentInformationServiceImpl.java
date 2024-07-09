package com.iti.thesis.helicopter.thesis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.constant.ConstantCodePrefix;
import com.iti.thesis.helicopter.thesis.constant.DepartmentRoleCode;
import com.iti.thesis.helicopter.thesis.constant.StatusCode;
import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.core.exception.MNotFoundException;
import com.iti.thesis.helicopter.thesis.db.service.DepartmentInformationMapper;
import com.iti.thesis.helicopter.thesis.db.service.DepartmentManagementMapper;
import com.iti.thesis.helicopter.thesis.service.DepartmentInformationService;
import com.iti.thesis.helicopter.thesis.util.MStringUtil;
import com.iti.thesis.helicopter.thesis.util.MValidatorUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DepartmentInformationServiceImpl implements DepartmentInformationService {
	
	@Autowired
	private DepartmentInformationMapper		departmentInformationMapper;
	@Autowired
	private DepartmentManagementMapper		departmentManagementMapper;
	
	@Override
	public MData registerDepartmentInformation(MData param) {
		try {
			MValidatorUtil.validate(param, "departmentName", "departmentDesc");
			MData	outputData	= param;
			String lastDeptID = this.retrieveLastDepartmentID(param);
			param.setString("departmentID", lastDeptID);
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
		try {
			return departmentInformationMapper.retrieveDepartmentInformationList(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
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
			String departmentID	= param.getString("departmentID");
			String teacherID	= param.getString("teacherID");
			
			MData departmentInfo = this.retrieveDepartmentInformationDetail(param);
			if(!departmentInfo.isEmpty()) {
				departmentInformationMapper.updateDepartmentInformation(param);
				if (!MStringUtil.isEmpty(teacherID)) {
					MData department = new MData();
					department.setString("departmentID", departmentID);
					department.setString("teacherID", teacherID);
					department.setString("departmentRoleCode", DepartmentRoleCode.MANAGER.getValue());
					department.setString("statusCode", StatusCode.ACTIVE.getValue());
					boolean isExist = this.retrieveAndValidateDepartmentMgt(department);
					if(isExist) {
						departmentManagementMapper.updateDepartmentManagement(department);
					}else {
						departmentManagementMapper.registerDepartmentManagement(department);
					}
				}
			}
			return param;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}
	
	private boolean retrieveAndValidateDepartmentMgt(MData param) {
		try {
			MValidatorUtil.validate(param, "departmentID");
			departmentManagementMapper.retrieveDepartmentManagementDetail(param);
			return true;
		} catch (MNotFoundException e) {
			return false;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	private String retrieveLastDepartmentID(MData param) throws MException {
		int result = 0;
		try {
			MData departmentInfo = departmentInformationMapper.retrieveLastDepartmentID(param);
			String resultStr = departmentInfo.getString("departmentID");
			resultStr = resultStr.substring(ConstantCodePrefix.DEPARTMENT.getValue().length(), resultStr.length());
			result = Integer.valueOf(resultStr);
			result ++;
		} catch (MNotFoundException e) {
			result = 1001;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
		return ConstantCodePrefix.DEPARTMENT.getValue() + String.valueOf(String.format("%04d", result));
	}

}
