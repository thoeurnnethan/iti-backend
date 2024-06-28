package com.iti.thesis.helicopter.thesis.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface DepartmentInformationService {

	public MMultiData retrieveDepartmentInformationList(MData param) throws MException;
	public MData registerDepartmentInformation(MData param) throws MException;
	public MData retrieveDepartmentInformationDetail(MData param) throws MException;
	public MData retrieveLastNewsEventID(MData param) throws MException;
	public MData retrieveDepartmentInformationTotalCount(MData param) throws MException;
	public MData updateDepartmentInformation(MData param) throws MException;

}
