package com.iti.thesis.helicopter.thesis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.constant.ConstantCodePrefix;
import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.core.exception.MNotFoundException;
import com.iti.thesis.helicopter.thesis.db.service.StudentDetailMapper;
import com.iti.thesis.helicopter.thesis.service.StudentAcademicHistoryService;
import com.iti.thesis.helicopter.thesis.service.StudentDetailService;
import com.iti.thesis.helicopter.thesis.service.StudentParentDetailService;
import com.iti.thesis.helicopter.thesis.util.MValidatorUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StudentDetailServiceImpl implements StudentDetailService {
	
	@Autowired
	private StudentDetailMapper				studentDetailMapper;
	@Autowired
	private StudentParentDetailService		parentDetailService;
	@Autowired
	private StudentAcademicHistoryService	studentAcademicHistoryService;
	
	@Override
	public MData registerStudentDetail(MData param) {
		try {
			String studentID = this.getLastStudentID(param);
			param.setString("studentID", studentID);
			// Register Student Info
			studentDetailMapper.registerStudentDetail(param);
			// Register Parent Info
			parentDetailService.registerStudentParentDetail(param);
			// Register Academic History Info
			studentAcademicHistoryService.registerStudentAcademicHistory(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
		return param;
	}
	
	private String getLastStudentID(MData param) {
		int result = 0;
		try {
			MData	latestStudentInfo	= studentDetailMapper.getLastStudentID(param);
			String	lastStudentID		= latestStudentInfo.getString("studentID");
			lastStudentID	= lastStudentID.substring(ConstantCodePrefix.STUDNET.getValue().length(), lastStudentID.length());
			result			= Integer.valueOf(lastStudentID);
			result ++;
		} catch(MNotFoundException e) {
			result = 1001;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
		return ConstantCodePrefix.STUDNET.getValue() + String.valueOf(String.format("%04d", result));
	}
	
	@Override
	public MData retrieveStudentDetailSummary(MData param) throws MException {
		MData outputData = new MData();
		try {
			MValidatorUtil.validate(param, "studentID","roleID");
//			MData		studentDetail				= studentDetailMapper.retrieveStudentDetail(param);
			MMultiData	studentParentDetailList		= parentDetailService.retrieveStudentParentDetailList(param);
			MMultiData	studentAcademicHistoryList	= studentAcademicHistoryService.retrieveStudentAcademicHistoryList(param);
//			outputData = studentDetail;
			outputData.setMMultiData("parentList", studentParentDetailList);
			outputData.setMMultiData("academicList", studentAcademicHistoryList);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
		return outputData;
	}

	@Override
	public MData retrieveStudentDetail(MData param) throws MException {
		try {
			return studentDetailMapper.retrieveStudentDetail(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	@Override
	public MMultiData retrieveStudentDetailList(MData param) throws MException {
		try {
			return studentDetailMapper.retrieveStudentDetailList(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	@Override
	public MData retrieveTotalCountStudentDetailList(MData param) throws MException {
		try {
			return studentDetailMapper.retrieveTotalCountStudentDetailList(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	@Override
	public void updateStudentDetail(MData param) throws MException {
		try {
			studentDetailMapper.retrieveTotalCountStudentDetailList(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

}
