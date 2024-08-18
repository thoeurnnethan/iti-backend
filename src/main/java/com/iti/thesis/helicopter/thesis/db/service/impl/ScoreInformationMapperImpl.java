package com.iti.thesis.helicopter.thesis.db.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.db.dao.ChannelDBDao;
import com.iti.thesis.helicopter.thesis.db.service.ScoreInformationMapper;

@Service
public class ScoreInformationMapperImpl implements ScoreInformationMapper {

	@Autowired
	private ChannelDBDao channelDBDao;

	@Override
	public MMultiData retrieveStudentScoreList(MData param) throws MException {
		return channelDBDao.selectList("retrieveStudentScoreList", param);
	}
	
	@Override
	public MData retrieveScoreInformation(MData param) throws MException {
		return channelDBDao.selectOne("retrieveScoreInformation", param);
	}

	@Override
	public int updateScoreInformation(MData studentClassInfo) throws MException {
		return channelDBDao.update("updateScoreInformation", studentClassInfo);
	}

	@Override
	public int registerScoreInformation(MData studentClassInfo) throws MException {
		return channelDBDao.insert("registerScoreInformation", studentClassInfo);
	}
	
}