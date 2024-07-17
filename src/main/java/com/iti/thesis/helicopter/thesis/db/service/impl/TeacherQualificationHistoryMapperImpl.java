package com.iti.thesis.helicopter.thesis.db.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.db.dao.ChannelDBDao;
import com.iti.thesis.helicopter.thesis.db.service.TeacherQualificationHistoryMapper;

@Service
public class TeacherQualificationHistoryMapperImpl implements TeacherQualificationHistoryMapper {

	@Autowired
	private ChannelDBDao channelDBDao;

	@Override
	public int registerTeacherQualificationHistory(MData param) throws MException {
		return channelDBDao.insert("registerTeacherQualificationHistory", param);
	}

	@Override
	public MMultiData retrieveListTeacherQualificationHistory(MData param) throws MException {
		return channelDBDao.selectList("retrieveListTeacherQualificationHistory", param);
	}

	@Override
	public MData retrieveTeacherQualificationHistoryDetail(MData param) throws MException {
		return channelDBDao.selectOne("retrieveTeacherQualificationHistoryDetail", param);
	}

	@Override
	public int updateTeacherQualification(MData param) throws MException {
		return channelDBDao.update("updateTeacherQualification", param);
	}

	@Override
	public MData retrieveLatestSeqNo(MData param) throws MException {
		return channelDBDao.selectOne("retrieveLatestSeqNo", param);
	}
	
}