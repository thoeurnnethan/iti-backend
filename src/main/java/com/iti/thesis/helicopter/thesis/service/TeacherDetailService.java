package com.iti.thesis.helicopter.thesis.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface TeacherDetailService {

	public MMultiData retrieveEmployeeList(MData param) throws MException;
	public MData retrieveEmployeeTotalCount(MData param) throws MException;
	public MData registerTeacherDetail(MData param) throws MException;
	public int updateEmployee(MData param) throws MException;
	public MData retrieveEmployeeDetail(MData reqParam) throws MException;

}
