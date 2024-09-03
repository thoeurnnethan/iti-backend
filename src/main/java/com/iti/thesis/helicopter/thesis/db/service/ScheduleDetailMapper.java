package com.iti.thesis.helicopter.thesis.db.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface ScheduleDetailMapper {

	public int registerScheduleDetail(MData param) throws MException;
	public MMultiData selectCheckDuplicateTime(MData checkParam) throws MException;
	public MMultiData selectCheckDuplicateTeacher(MData checkParam) throws MException;
	public int updateScheduleDetail(MData data) throws MException;
	public MData retrieveScheduleDetail(MData param) throws MException;
	
}