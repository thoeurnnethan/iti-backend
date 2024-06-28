package com.iti.thesis.helicopter.thesis.db.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.db.dao.ChannelDBDao;
import com.iti.thesis.helicopter.thesis.db.service.StudentDetailMapper;

@Service
public class StudentDetailMapperImpl implements StudentDetailMapper{

	@Autowired
	private ChannelDBDao channelDBDao;
	
	@Override
	public MMultiData retrieveStudentDetailList(MData param) throws MException {
		return channelDBDao.selectListForPage("retrieveStudentDetailList", param);
	}

	@Override
	public MData retrieveTotalCountStudentDetailList(MData param) throws MException {
		return channelDBDao.selectOne("retrieveTotalCountStudentDetailList", param);
	}

	@Override
	public int registerStudentDetail(MData param) throws MException {
		return channelDBDao.insert("registerStudentDetail", param);
	}
	
	@Override
	public MData retrieveStudentDetail(MData param) throws MException {
		return channelDBDao.selectOne("retrieveStudentDetail", param);
	}

	@Override
	public MData getLastStudentID(MData param) throws MException {
		return channelDBDao.selectOne("getLastStudentID", param);
	}
	
}