package com.iti.thesis.helicopter.thesis.db.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface StudentClassMappingMapper {
	
	public int registerStudentClassMappingInfo(MData param) throws MException;
	public int updateStudentClassMappingInfo(MData student) throws MException;
	
}