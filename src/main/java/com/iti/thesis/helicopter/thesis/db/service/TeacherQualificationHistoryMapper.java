package com.iti.thesis.helicopter.thesis.db.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface TeacherQualificationHistoryMapper {

	public int registerTeacherQualificationHistory(MData param) throws MException;
	
}