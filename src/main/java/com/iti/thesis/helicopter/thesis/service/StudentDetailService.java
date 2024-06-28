package com.iti.thesis.helicopter.thesis.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface StudentDetailService {
	
	public MData registerStudentDetail(MData param) throws MException;
	public MData retrieveStudentDetailSummary(MData param) throws MException;
	public MData retrieveStudentDetail(MData userInfo) throws MException;
	public MMultiData retrieveStudentDetailList(MData param) throws MException;
	public MData retrieveTotalCountStudentDetailList(MData param) throws MException;
	
}
