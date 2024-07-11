package com.iti.thesis.helicopter.thesis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.constant.ConstantCodePrefix;
import com.iti.thesis.helicopter.thesis.constant.UserRoleCode;
import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.core.exception.MNotFoundException;
import com.iti.thesis.helicopter.thesis.db.service.TeacherDetailMapper;
import com.iti.thesis.helicopter.thesis.db.service.TeacherQualificationHistoryMapper;
import com.iti.thesis.helicopter.thesis.service.TeacherDetailService;
import com.iti.thesis.helicopter.thesis.util.MStringUtil;
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
			String tacherID = this.getLastTeacherID(param);
			param.setString("teacherID", tacherID);
			// Register Teacher Detail
			teacherDetailMapper.registerTeacherDetail(param);
			// Register Teacher Detail
			int qualSeqNo = 0;
			for(MData qual : param.getMMultiData("qualificationList").toListMData()) {
				qualSeqNo ++;
				qual.setString("teacherID", tacherID);
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
	
	private String getLastTeacherID(MData param) {
		String	prefix = MStringUtil.EMPTY;
		int		result = 0;
		try {
			String userRole = param.getString("roleID");
			if(UserRoleCode.ADMIN.getValue().equals(userRole)) {
				prefix = ConstantCodePrefix.ADMIN.getValue();
			}else if(UserRoleCode.DEP_MANAGER.getValue().equals(userRole)) {
				prefix = ConstantCodePrefix.DEPARTENT_MGT.getValue();
			}else {
				prefix = ConstantCodePrefix.TEACHER.getValue();
			}
			param.setString("prefix", prefix);
			MData	lastTeacherID	= teacherDetailMapper.retrieveLastTeacherID(param);
			String	teacherID		= lastTeacherID.getString("teacherID");
			teacherID = teacherID.substring(prefix.length(), teacherID.length());
			result			= Integer.valueOf(teacherID);
			result ++;
		} catch (MNotFoundException e) {
			result = 1001;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
		return prefix + String.valueOf(String.format("%04d", result));
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
