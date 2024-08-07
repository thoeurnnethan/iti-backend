package com.iti.thesis.helicopter.thesis.db.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface ScheduleInformationMapper {

	public MMultiData retrieveScheduleInformationList(MData param) throws MException;
	public int registerScheduleInformation(MData param) throws MException;
	
}