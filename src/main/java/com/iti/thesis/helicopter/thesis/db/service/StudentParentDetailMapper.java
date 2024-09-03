package com.iti.thesis.helicopter.thesis.db.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface StudentParentDetailMapper {
	
	public int registerStudentParentDetail(MData param) throws MException;
	public MMultiData retrieveStudentParentDetailList(MData param) throws MException;
	public MData retrieveStudentParentDetail(MData param) throws MException;
	public int updateStudentParentDetail(MData parent) throws MException;
	public MData retrieveLatestParentSeqNo(MData param) throws MException;
}