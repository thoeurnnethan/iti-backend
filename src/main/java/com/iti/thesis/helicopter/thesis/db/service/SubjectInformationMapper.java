package com.iti.thesis.helicopter.thesis.db.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface SubjectInformationMapper {

	public MMultiData retrieveSubjectInformationList(MData param) throws MException;
	public int registerSubjectInformation(MData param) throws MException;
	public MData retrieveSubjectIsExist(MData param) throws MException;
	public MData retrieveLastClassID(MData param) throws MException;
	public int updateSubjectInformation(MData param) throws MException;
	public MMultiData retrieveSubjectInformationListForDownload(MData param) throws MException;
}