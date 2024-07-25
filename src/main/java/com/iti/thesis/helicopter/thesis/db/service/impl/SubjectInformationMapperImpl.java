package com.iti.thesis.helicopter.thesis.db.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.db.dao.ChannelDBDao;
import com.iti.thesis.helicopter.thesis.db.service.SubjectInformationMapper;

@Service
public class SubjectInformationMapperImpl implements SubjectInformationMapper {

	@Autowired
	private ChannelDBDao channelDBDao;

	@Override
	public MMultiData retrieveSubjectInformationList(MData requestBody) throws MException {
		return channelDBDao.selectListForPage("retrieveSubjectInformationList", requestBody);
	}

	@Override
	public int registerSubjectInformation(MData param) throws MException {
		return channelDBDao.insert("registerSubjectInformation", param);
	}

	@Override
	public MData retrieveSubjectIsExist(MData param) throws MException {
		return channelDBDao.selectOne("retrieveSubjectIsExist", param);
	}

	@Override
	public MData retrieveLastClassID(MData param) throws MException {
		return channelDBDao.selectOne("retrieveLastClassID", param);
	}

	@Override
	public int updateSubjectInformation(MData param) throws MException {
		return channelDBDao.update("updateSubjectInformation", param);
	}
	
}