package com.iti.thesis.helicopter.thesis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.core.exception.MNotFoundException;
import com.iti.thesis.helicopter.thesis.db.service.TeacherDetailMapper;
import com.iti.thesis.helicopter.thesis.db.service.TeacherQualificationHistoryMapper;
import com.iti.thesis.helicopter.thesis.service.TeacherDetailService;
import com.iti.thesis.helicopter.thesis.util.MValidatorUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TeacherDetailServiceImpl implements TeacherDetailService {
	
	@Autowired
	private TeacherDetailMapper					teacherDetailMapper;
	@Autowired
	private TeacherQualificationHistoryMapper	teacherQualificationHistoryMapper;
	
	@Override
	public MData registerTeacherDetail(MData param) {
		try {
			String lastTeacherID = this.setTeacherID(param);
			param.setString("teacherID", lastTeacherID);
			
			// Register Teacher Detail
			teacherDetailMapper.registerTeacherDetail(param);
			
			// Register Teacher Detail
			int qualSeqNo = 0;
			for(MData qual : param.getMMultiData("qualificationList").toListMData()) {
				qualSeqNo ++;
				qual.setString("teacherID", lastTeacherID);
				qual.setInt("seqNo", qualSeqNo);
				teacherQualificationHistoryMapper.registerTeacherQualificationHistory(qual);
			}
			return param;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}
	
	private String setTeacherID(MData param) {
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
		return String.valueOf(result);
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
