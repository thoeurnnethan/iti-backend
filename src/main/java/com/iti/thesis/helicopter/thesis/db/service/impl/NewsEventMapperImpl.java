package com.iti.thesis.helicopter.thesis.db.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.db.dao.ChannelDBDao;
import com.iti.thesis.helicopter.thesis.db.service.NewsEventMapper;

@Service
public class NewsEventMapperImpl implements NewsEventMapper{

	@Autowired
	private ChannelDBDao channelDBDao;

	@Override
	public MMultiData retrieveNewsEventList(MData requestBody) throws MException {
		return channelDBDao.selectListForPage("retrieveNewsEventList", requestBody);
	}

	@Override
	public MData retrieveNewsEventTotalCount(MData param) throws MException {
		return channelDBDao.selectOne("retrieveNewsEventTotalCount", param);
	}

	@Override
	public int registerNewsEvent(MData param) throws MException {
		return channelDBDao.insert("registerNewsEvent", param);
	}

	@Override
	public MData retrieveNewsEventDetail(MData param) throws MException {
		return channelDBDao.selectOne("retrieveNewsEventDetail", param);
	}

	@Override
	public MData retrieveLastNewsEventID(MData param) throws MException {
		return channelDBDao.selectOne("retrieveLastNewsEventID", param);
	}

	@Override
	public int updateNewsEvent(MData param) throws MException {
		return channelDBDao.update("updateNewsEvent", param);
	}
	
}