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
	
}