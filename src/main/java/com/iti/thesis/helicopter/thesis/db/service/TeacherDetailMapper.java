package com.iti.thesis.helicopter.thesis.db.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface TeacherDetailMapper {

	public MMultiData retrieveEmployeeList(MData param) throws MException;
	public int registerTeacherDetail(MData param) throws MException;
	public MData retrieveTeacherDetail(MData param) throws MException;
	public MData retrieveLastTeacherID(MData param) throws MException;
	public MData retrieveEmployeeTotalCount(MData param) throws MException;
	public int updateEmployee(MData param) throws MException;
}