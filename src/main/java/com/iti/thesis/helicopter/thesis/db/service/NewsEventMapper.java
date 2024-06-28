package com.iti.thesis.helicopter.thesis.db.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface NewsEventMapper{

	public MMultiData retrieveNewsEventList(MData param) throws MException;
	public int registerNewsEvent(MData param) throws MException;
	public MData retrieveNewsEventDetail(MData param) throws MException;
	public MData retrieveLastNewsEventID(MData param) throws MException;
	public MData retrieveNewsEventTotalCount(MData param) throws MException;
	public int updateNewsEvent(MData param) throws MException;
}