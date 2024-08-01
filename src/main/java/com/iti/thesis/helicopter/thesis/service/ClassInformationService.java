package com.iti.thesis.helicopter.thesis.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface ClassInformationService {

	public MMultiData retrieveClassInformationList(MData param) throws MException;
	public MData registerClassInformation(MData param) throws MException;
	public MData retrieveClassInformationDetail(MData param) throws MException;
	public MData retrieveClassInformationTotalCount(MData param) throws MException;
	public MData updateClassInformation(MData param) throws MException;
	public MMultiData retrieveClassInformationListForDownload(MData param) throws MException;
	public MMultiData retrieveClassInformationStudentList(MData param) throws MException;

}
