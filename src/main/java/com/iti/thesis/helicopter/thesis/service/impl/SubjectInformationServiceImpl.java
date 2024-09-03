package com.iti.thesis.helicopter.thesis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.common.ErrorCode.ErrorCode;
import com.iti.thesis.helicopter.thesis.constant.StatusCode;
import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.core.exception.MNotFoundException;
import com.iti.thesis.helicopter.thesis.db.service.ClassInformationMapper;
import com.iti.thesis.helicopter.thesis.db.service.SubjectInformationMapper;
import com.iti.thesis.helicopter.thesis.service.SubjectInformationService;
import com.iti.thesis.helicopter.thesis.util.MStringUtil;
import com.iti.thesis.helicopter.thesis.util.MValidatorUtil;

@Service
public class SubjectInformationServiceImpl implements SubjectInformationService {
	
	@Autowired
	private ClassInformationMapper		classInformationMapper;
	@Autowired
	private SubjectInformationMapper	subjectInformationMapper;
	
	
	@Override
	public MMultiData retrieveSubjectInformationList(MData param) throws MException {
		try {
			return subjectInformationMapper.retrieveSubjectInformationList(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	@Override
	public MMultiData retrieveSubjectInformationListForDownload(MData param) throws MException {
		try {
			return subjectInformationMapper.retrieveSubjectInformationListForDownload(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	@Override
	public MData registerSubjectInformation(MData param) {
		MData	outputData		= new MData();
		try {
			MValidatorUtil.validate(param, "classID", "subjectList");
			
			MData classInfo = classInformationMapper.retrieveClassInformationDetailByClassInfoID(param);
			if(!classInfo.isEmpty()) {
				MData subjectInfo = new MData();
				subjectInfo.setString("classInfoID", param.getString("classID"));
				MMultiData subjectList = this.retrieveSubjectInformationList(subjectInfo);
				if(subjectList.size() > 0) {
					throw new MException(ErrorCode.SUBJECT_ALREADY_REGISTER.getValue(), ErrorCode.SUBJECT_ALREADY_REGISTER.getDescription());
				}else {
					MMultiData	subList		= new MMultiData();
					int seqNo = 0;
					for(MData subject : param.getMMultiData("subjectList").toListMData()) {
						MValidatorUtil.validate(subject, "subjectName");
						
						subject.setString("classID", param.getString("classID"));
						subject.setString("subjectID", param.getString("classID") + "_SUB"+ (++seqNo));
						subject.setString("statusCode", StatusCode.ACTIVE.getValue());
						boolean isExist = this.retrieveValidateSubject(subject);
						if(isExist) {
							subjectInformationMapper.updateSubjectInformation(subject);
						}else {
							subjectInformationMapper.registerSubjectInformation(subject);
						}
						subList.addMData(subject);
					}
					outputData.setMMultiData("subjectList", subList);
				}
			}
			return outputData;
		} catch (MNotFoundException e) {
			throw new MException(ErrorCode.CLASS_NOT_FOUND.getValue(), ErrorCode.CLASS_NOT_FOUND.getDescription());
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}
	
	private boolean retrieveValidateSubject(MData param) {
		try {
			MValidatorUtil.validate(param, "classID");
			subjectInformationMapper.retrieveSubjectIsExist(param);
			return true;
		} catch (MNotFoundException e) {
			return false;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}
	
	@Override
	public MData retrieveClassInformationDetail(MData param) throws MException {
		MData outputData = new MData();
		try {
			outputData = classInformationMapper.retrieveClassInformationDetail(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
		return outputData;
	}

	@Override
	public MData updateSubjectInformation(MData param) throws MException {
		MData outputData = new MData();
		try {
			MValidatorUtil.validate(param, "classID","subjectList");
			MData classInfo = classInformationMapper.retrieveClassInformationDetailByClassInfoID(param);
			if(!classInfo.isEmpty()) {
				for(MData subject : param.getMMultiData("subjectList").toListMData()) {
					MValidatorUtil.validate(subject, "subjectID");
					subject.setString("classID", param.getString("classID"));
					boolean isExist = this.retrieveValidateSubject(subject);
					if(isExist) {
						subjectInformationMapper.updateSubjectInformation(subject);
					}else {
						subject.setString("statusCode", StatusCode.ACTIVE.getValue());
						subjectInformationMapper.registerSubjectInformation(subject);
					}
				}
			}
			MData resParam = new MData();
			resParam.setString("classInfoID", param.getString("classID"));
			resParam.setString("searchKey", MStringUtil.EMPTY);
			MMultiData subjectList = this.retrieveSubjectInformationList(resParam);
			outputData.setMMultiData("subjectList", subjectList);
			return outputData;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

}
