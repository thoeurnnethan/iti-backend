package com.iti.thesis.helicopter.thesis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.core.exception.MNotFoundException;
import com.iti.thesis.helicopter.thesis.db.service.TeacherDetailMapper;
import com.iti.thesis.helicopter.thesis.service.TeacherDetailService;
import com.iti.thesis.helicopter.thesis.util.MValidatorUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TeacherDetailServiceImpl implements TeacherDetailService {
	
	@Autowired
	private TeacherDetailMapper			teacherDetailMapper;
	
	@Override
	public MMultiData retrieveEmployeeList(MData param) throws MException {
		MMultiData outputData = new MMultiData();
		try {
			outputData = teacherDetailMapper.retrieveEmployeeList(param);
		} catch (MException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (Exception e){
			throw e;
		}
		return outputData;
	}

	@Override
	public MData retrieveEmployeeTotalCount(MData param) throws MException {
		try {
			return teacherDetailMapper.retrieveEmployeeTotalCount(param);
		} catch (MException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (Exception e){
			throw e;
		}
	}
	
	@Override
	public MData registerTeacherDetail(MData param) {
		try {
			int lastTeacherID = this.setTeacherID(param);
			param.setInt("teacherID", lastTeacherID);
			teacherDetailMapper.registerTeacherDetail(param);
			return param;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}
	
	private int setTeacherID(MData param) {
		int result = 0;
		try {
			MData lastTeacherID	= teacherDetailMapper.retrieveLastTeacherID(param);
			result				= lastTeacherID.getInt("lastTeacherID");
			result++;
		} catch (MNotFoundException e) {
			result = 1001;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
		return result;
	}

	@Override
	public MData retrieveEmployeeDetail(MData param) throws MException {
		MData outputData = new MData();
		try {
			outputData = teacherDetailMapper.retrieveEmployeeDetail(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
		return outputData;
	}

	@Override
	public int updateEmployee(MData param) throws MException {
		try {
			MValidatorUtil.validate(param, "studentID");
			MData employeeInfo = this.retrieveEmployeeDetail(param);
			if(!employeeInfo.isEmpty()) {
				return teacherDetailMapper.updateEmployee(param);
			}
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
		return 0;
	}

}
