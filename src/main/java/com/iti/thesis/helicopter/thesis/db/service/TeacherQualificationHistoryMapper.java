package com.iti.thesis.helicopter.thesis.db.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface TeacherQualificationHistoryMapper {

	public int registerTeacherQualificationHistory(MData param) throws MException;

	public MMultiData retrieveListTeacherQualificationHistory(MData param) throws MException;

	public MData retrieveTeacherQualificationHistoryDetail(MData param) throws MException;

	public int updateTeacherQualification(MData param) throws MException;

	public MData retrieveLatestQualifySeqNo(MData param) throws MException;
	
}