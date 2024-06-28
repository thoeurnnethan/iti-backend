package com.iti.thesis.helicopter.thesis.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface DepartmentManagementService {

	public MMultiData retrieveDepartmentManagementList(MData param) throws MException;
	public MData registerDepartmentManagement(MData param) throws MException;
	public MData retrieveDepartmentManagementDetail(MData param) throws MException;
	public MData retrieveLastNewsEventID(MData param) throws MException;
	public MData retrieveDepartmentManagementTotalCount(MData param) throws MException;
	public MData updateDepartmentManagement(MData param) throws MException;

}
