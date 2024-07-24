package com.iti.thesis.helicopter.thesis.db.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.db.dao.ChannelDBDao;
import com.iti.thesis.helicopter.thesis.db.service.RoomInformationMapper;

@Service
public class RoomInformationMapperImpl implements RoomInformationMapper {

	@Autowired
	private ChannelDBDao channelDBDao;

	@Override
	public int registerRoomInformation(MData param) throws MException {
		return channelDBDao.insert("registerRoomInformation", param);
	}

	@Override
	public MData retrieveLastRoomID(MData param) throws MException {
		return channelDBDao.selectOne("retrieveLastRoomID", param);
	}

	@Override
	public MMultiData retrieveRoomInformationList(MData param) throws MException {
		return channelDBDao.selectListForPage("retrieveRoomInformationList", param);
	}

	@Override
	public MData retrieveRoomInformationTotalCount(MData param) throws MException {
		return channelDBDao.selectOne("retrieveRoomInformationTotalCount", param);
	}

	@Override
	public int updateRoomInformation(MData param) throws MException {
		return channelDBDao.update("updateRoomInformation", param);
	}

	@Override
	public MData retrieveRoomInformationDetail(MData param) throws MException {
		return channelDBDao.selectOne("retrieveRoomInformationDetail", param);
	}

}