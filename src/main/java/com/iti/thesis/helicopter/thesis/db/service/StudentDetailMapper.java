package com.iti.thesis.helicopter.thesis.db.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface StudentDetailMapper {
	
	public int registerStudentDetail(MData param) throws MException;
	public MData retrieveStudentDetail(MData param) throws MException;
	public MData getLastStudentID(MData param) throws MException;
	public MMultiData retrieveStudentDetailList(MData param) throws MException;
	public MData retrieveTotalCountStudentDetailList(MData param) throws MException;
	
}