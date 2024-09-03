package com.iti.thesis.helicopter.thesis.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface TeacherDetailService {

	public MData registerTeacherDetail(MData param) throws MException;
	public void updateTeacherDetail(MData param) throws MException;
	public MData retrieveTeacherDetail(MData reqParam) throws MException;

}
