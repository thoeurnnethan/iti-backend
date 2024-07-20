package com.iti.thesis.helicopter.thesis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.core.exception.MNotFoundException;
import com.iti.thesis.helicopter.thesis.db.service.StudentParentDetailMapper;
import com.iti.thesis.helicopter.thesis.service.StudentParentDetailService;
import com.iti.thesis.helicopter.thesis.util.MValidatorUtil;

@Service
public class StudentParentDetailServiceImpl implements StudentParentDetailService {
	
	@Autowired
	private StudentParentDetailMapper studentParentDetailMapper;
	
	@Override
	public MData registerStudentParentDetail(MData param) {
		try {
			MValidatorUtil.validate(param, "parentList");
			int seqNo = 0;
			MMultiData parentList = param.getMMultiData("parentList");
			for(MData data : parentList.toListMData()) {
				seqNo++;
				data.setInt("seqNo", seqNo);
				data.setString("studentID", param.getString("studentID"));
				studentParentDetailMapper.registerStudentParentDetail(data);
			}
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
		return new MData();
	}

	@Override
	public MMultiData retrieveStudentParentDetailList(MData param) throws MException {
		MMultiData outputData = new MMultiData();
		try {
			MValidatorUtil.validate(param, "studentID");
			outputData = studentParentDetailMapper.retrieveStudentParentDetailList(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
		return outputData;
	}

	@Override
	public void updateStudentParentDetail(MData param) throws MException {
		try {
			MValidatorUtil.validate(param, "studentID", "parentList");
			for(MData parent : param.getMMultiData("parentList").toListMData()) {
				MValidatorUtil.validate(parent, "studentID", "seqNo");
				boolean isExist = this.isParentExist(parent);
				if(isExist) {
					studentParentDetailMapper.updateStudentParentDetail(parent);
				}else {
					parent.setInt("seqNo", this.retrieveLastedSeqNo(parent));
					studentParentDetailMapper.registerStudentParentDetail(parent);
				}
			}
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}
	
	private boolean isParentExist(MData param) {
		try {
			studentParentDetailMapper.retrieveStudentParentDetail(param);
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
			MData	seqNo	= studentParentDetailMapper.retrieveLatestParentSeqNo(param);
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
