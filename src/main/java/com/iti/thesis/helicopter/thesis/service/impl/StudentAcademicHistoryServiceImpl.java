package com.iti.thesis.helicopter.thesis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.db.service.StudentAcademicHistoryMapper;
import com.iti.thesis.helicopter.thesis.service.StudentAcademicHistoryService;
import com.iti.thesis.helicopter.thesis.util.MValidatorUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StudentAcademicHistoryServiceImpl implements StudentAcademicHistoryService {
	
	@Autowired
	private StudentAcademicHistoryMapper studentAcademicHistoryMapper;
	
	@Override
	public MData registerStudentAcademicHistory(MData param) {
		try {
			MValidatorUtil.validate(param, "academicList");
			int			seqNo			= 0;
			MMultiData	academicList	= param.getMMultiData("academicList");
			for(MData data : academicList.toListMData()) {
				seqNo++;
				data.setInt("seqNo", seqNo);
				data.setString("studentID", param.getString("studentID"));
				studentAcademicHistoryMapper.registerStudentAcademicHistory(data);
			}
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
		return null;
	}

	@Override
	public MMultiData retrieveStudentAcademicHistoryList(MData param) throws MException {
		MMultiData outputData = new MMultiData();
		try {
			MValidatorUtil.validate(param, "studentID");
			outputData = studentAcademicHistoryMapper.retrieveStudentAcademicHistoryList(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
		return outputData;
	}

}
