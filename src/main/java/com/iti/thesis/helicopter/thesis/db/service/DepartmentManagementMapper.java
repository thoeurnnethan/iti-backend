package com.iti.thesis.helicopter.thesis.db.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface DepartmentManagementMapper {

	public MMultiData retrieveDepartmentManagementList(MData param) throws MException;
	public int registerDepartmentManagement(MData param) throws MException;
	public MData retrieveDepartmentManagementDetail(MData param) throws MException;
	public MData retrieveDepartmentManager(MData param) throws MException;
	public MData retrieveDepartmentManagementTotalCount(MData param) throws MException;
	public int updateDepartmentManagement(MData param) throws MException;
}