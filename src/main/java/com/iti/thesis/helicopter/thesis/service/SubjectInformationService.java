package com.iti.thesis.helicopter.thesis.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface SubjectInformationService {

	public MMultiData retrieveSubjectInformationList(MData param) throws MException;
	public MData registerSubjectInformation(MData param) throws MException;
	public MData retrieveClassInformationDetail(MData param) throws MException;
	public MData updateSubjectInformation(MData param) throws MException;
	public MMultiData retrieveSubjectInformationListForDownload(MData param) throws MException;

}
