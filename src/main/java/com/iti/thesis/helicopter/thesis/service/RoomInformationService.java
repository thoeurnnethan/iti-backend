package com.iti.thesis.helicopter.thesis.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface RoomInformationService {

	public MData registerRoomInformation(MData param) throws MException;

	public MMultiData retrieveRoomInformationList(MData param) throws MException;

	public MData retrieveRoomInformationTotalCount(MData param) throws MException;

	public MData updateRoomInformation(MData param) throws MException;

	public MMultiData retrieveRoomInformationListForDownload(MData param) throws MException;

}
