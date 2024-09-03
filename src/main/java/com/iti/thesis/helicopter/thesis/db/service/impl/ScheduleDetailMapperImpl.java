package com.iti.thesis.helicopter.thesis.db.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.db.dao.ChannelDBDao;
import com.iti.thesis.helicopter.thesis.db.service.ScheduleDetailMapper;

@Service
public class ScheduleDetailMapperImpl implements ScheduleDetailMapper {

	@Autowired
	private ChannelDBDao channelDBDao;

	@Override
	public int registerScheduleDetail(MData param) throws MException {
		return channelDBDao.insert("registerScheduleDetail", param);
	}

	@Override
	public MMultiData selectCheckDuplicateTime(MData checkParam) throws MException {
		return channelDBDao.selectList("selectCheckDuplicateTime", checkParam);
	}

	@Override
	public MMultiData selectCheckDuplicateTeacher(MData checkParam) throws MException {
		return channelDBDao.selectList("selectCheckDuplicateTeacher", checkParam);
	}

	@Override
	public int updateScheduleDetail(MData data) throws MException {
		return channelDBDao.update("updateScheduleDetail", data);
	}

	@Override
	public MData retrieveScheduleDetail(MData param) throws MException {
		return channelDBDao.selectOne("retrieveScheduleDetail", param);
	}
	
}