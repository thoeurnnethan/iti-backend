package com.iti.thesis.helicopter.thesis.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface ScoreInformationService {

	public MData retrieveStudentScoreList(MData param) throws MException;
	public MData registerStudentScore(MData param) throws MException;

}
