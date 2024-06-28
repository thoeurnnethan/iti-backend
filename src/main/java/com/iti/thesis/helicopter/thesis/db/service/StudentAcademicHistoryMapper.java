package com.iti.thesis.helicopter.thesis.db.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface StudentAcademicHistoryMapper{
	
	public int registerStudentAcademicHistory(MData param) throws MException;
	public MMultiData retrieveStudentAcademicHistoryList(MData param) throws MException;
	
}