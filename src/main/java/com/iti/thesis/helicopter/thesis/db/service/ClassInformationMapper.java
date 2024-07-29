package com.iti.thesis.helicopter.thesis.db.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface ClassInformationMapper {

	public MMultiData retrieveClassInformationList(MData param) throws MException;
	public int registerClassInformation(MData param) throws MException;
	public MData retrieveClassInformationDetail(MData param) throws MException;
	public MData retrieveLastClassID(MData param) throws MException;
	public MData retrieveClassInformationTotalCount(MData param) throws MException;
	public int updateClassInformation(MData param) throws MException;
	public MMultiData retrieveClassInformationListForDownload(MData param) throws MException;
}