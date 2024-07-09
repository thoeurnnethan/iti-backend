package com.iti.thesis.helicopter.thesis.db.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface DepartmentInformationMapper {

	public MMultiData retrieveDepartmentInformationList(MData param) throws MException;
	public int registerDepartmentInformation(MData param) throws MException;
	public MData retrieveDepartmentInformationDetail(MData param) throws MException;
	public MData retrieveDepartmentInformationTotalCount(MData param) throws MException;
	public int updateDepartmentInformation(MData param) throws MException;
	public MData retrieveLastDepartmentID(MData param) throws MException;
}