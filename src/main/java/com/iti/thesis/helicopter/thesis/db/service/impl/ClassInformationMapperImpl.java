package com.iti.thesis.helicopter.thesis.db.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.db.dao.ChannelDBDao;
import com.iti.thesis.helicopter.thesis.db.service.ClassInformationMapper;

@Service
public class ClassInformationMapperImpl implements ClassInformationMapper {

	@Autowired
	private ChannelDBDao channelDBDao;

	@Override
	public MMultiData retrieveClassInformationList(MData requestBody) throws MException {
		return channelDBDao.selectListForPage("retrieveClassInformationList", requestBody);
	}

	@Override
	public MData retrieveClassInformationTotalCount(MData param) throws MException {
		return channelDBDao.selectOne("retrieveClassInformationTotalCount", param);
	}

	@Override
	public MMultiData retrieveClassInformationListForDownload(MData param) throws MException {
		return channelDBDao.selectList("retrieveClassInformationListForDownload", param);
	}

	@Override
	public int registerClassInformation(MData param) throws MException {
		return channelDBDao.insert("registerClassInformation", param);
	}

	@Override
	public MData retrieveClassInformationDetail(MData param) throws MException {
		return channelDBDao.selectOne("retrieveClassInformationDetail", param);
	}

	@Override
	public MData retrieveLastClassID(MData param) throws MException {
		return channelDBDao.selectOne("retrieveLastClassID", param);
	}

	@Override
	public int updateClassInformation(MData param) throws MException {
		return channelDBDao.update("updateClassInformation", param);
	}
	
}