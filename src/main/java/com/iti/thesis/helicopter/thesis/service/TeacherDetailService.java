package com.iti.thesis.helicopter.thesis.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface TeacherDetailService {

	public MData registerTeacherDetail(MData param) throws MException;
	public int updateEmployee(MData param) throws MException;
	public MData retrieveEmployeeDetail(MData reqParam) throws MException;

}
