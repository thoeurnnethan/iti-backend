package com.iti.thesis.helicopter.thesis.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface ScheduleInformationService {

	public MMultiData retrieveScheduleInformationList(MData param) throws MException;
	public MData registerScheduleInformation(MData param) throws MException;
	public MData validateScheduleInformation(MData param) throws MException;

}
