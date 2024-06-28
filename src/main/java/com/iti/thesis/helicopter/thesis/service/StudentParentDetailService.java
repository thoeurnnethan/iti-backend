package com.iti.thesis.helicopter.thesis.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface StudentParentDetailService {
	
	public MData registerStudentParentDetail(MData param) throws MException;
	public MMultiData retrieveStudentParentDetailList(MData param) throws MException;
	
}
