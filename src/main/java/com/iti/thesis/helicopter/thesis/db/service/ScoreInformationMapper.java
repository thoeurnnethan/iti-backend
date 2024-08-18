package com.iti.thesis.helicopter.thesis.db.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface ScoreInformationMapper {

	public MMultiData retrieveStudentScoreList(MData param) throws MException;

	public MData retrieveScoreInformation(MData studentClassInfo) throws MException;

	public int updateScoreInformation(MData studentClassInfo) throws MException;

	public int registerScoreInformation(MData studentClassInfo) throws MException;
	
}