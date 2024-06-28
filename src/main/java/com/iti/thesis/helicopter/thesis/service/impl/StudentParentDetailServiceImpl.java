package com.iti.thesis.helicopter.thesis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.db.service.StudentParentDetailMapper;
import com.iti.thesis.helicopter.thesis.service.StudentParentDetailService;
import com.iti.thesis.helicopter.thesis.util.MValidatorUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
			log.error(e.getLocalizedMessage());
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
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
		return outputData;
	}

}
