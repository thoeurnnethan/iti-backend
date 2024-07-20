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

@Service
public class TeacherDetailServiceImpl implements TeacherDetailService {
	
	@Autowired
	private TeacherDetailMapper					teacherDetailMapper;
	@Autowired
	private TeacherQualificationHistoryMapper	teacherQualificationHistoryMapper;
	
	@Override
	public MData registerTeacherDetail(MData param) {
		try {
			MValidatorUtil.validate(param, "qualificationList");
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
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
		return prefix + String.valueOf(String.format("%04d", result));
	}

	@Override
	public MData retrieveTeacherDetail(MData param) throws MException {
		try {
			MData outputData = new MData();
			MValidatorUtil.validate(param, "teacherID");
			outputData.setMMultiData("qualificationList", teacherQualificationHistoryMapper.retrieveListTeacherQualificationHistory(param));
			return outputData;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	@Override
	public void updateTeacherDetail(MData param) throws MException {
		try {
			MValidatorUtil.validate(param, "teacherID");
			for(MData qual : param.getMMultiData("qualificationList").toListMData()) {
				qual.setString("teacherID", param.getString("teacherID"));
				boolean isExist = this.isQualificationExist(qual);
				if(isExist) {
					teacherQualificationHistoryMapper.updateTeacherQualification(qual);
				}else {
					qual.setInt("seqNo", this.retrieveLastedSeqNo(param));
					teacherQualificationHistoryMapper.registerTeacherQualificationHistory(qual);
				}
			}
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}
	
	private boolean isQualificationExist(MData param) {
		try {
			teacherQualificationHistoryMapper.retrieveTeacherQualificationHistoryDetail(param);
			return true;
		} catch (MNotFoundException e) {
			return false;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}
	
	private int retrieveLastedSeqNo(MData param) {
		try {
			MData	seqNo	= teacherQualificationHistoryMapper.retrieveLatestQualifySeqNo(param);
			int		result	= seqNo.getInt("seqNo");
			return ++result;
		} catch (MNotFoundException e) {
			return 1;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

}
