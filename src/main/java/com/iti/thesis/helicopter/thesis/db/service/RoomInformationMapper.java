package com.iti.thesis.helicopter.thesis.db.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface RoomInformationMapper {

	public int registerRoomInformation(MData param) throws MException;
	public MData retrieveLastRoomID(MData param) throws MException;
	public MMultiData retrieveRoomInformationList(MData param) throws MException;
	public MData retrieveRoomInformationTotalCount(MData param) throws MException;
	public int updateRoomInformation(MData param) throws MException;
	public MData retrieveRoomInformationDetail(MData param) throws MException;
}