package com.iti.thesis.helicopter.thesis.db.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.db.dao.ChannelDBDao;
import com.iti.thesis.helicopter.thesis.db.service.TeacherDetailMapper;

@Service
public class TeacherDetailMapperImpl implements TeacherDetailMapper {

	@Autowired
	private ChannelDBDao channelDBDao;

	@Override
	public MMultiData retrieveEmployeeList(MData requestBody) throws MException {
		return channelDBDao.selectListForPage("retrieveEmployeeList", requestBody);
	}

	@Override
	public MData retrieveEmployeeTotalCount(MData param) throws MException {
		return channelDBDao.selectOne("retrieveEmployeeTotalCount", param);
	}

	@Override
	public int registerTeacherDetail(MData param) throws MException {
		return channelDBDao.insert("registerTeacherDetail", param);
	}

	@Override
	public MData retrieveEmployeeDetail(MData param) throws MException {
		return channelDBDao.selectOne("retrieveEmployeeDetail", param);
	}

	@Override
	public MData retrieveLastTeacherID(MData param) throws MException {
		return channelDBDao.selectOne("retrieveLastTeacherID", param);
	}

	@Override
	public int updateEmployee(MData param) throws MException {
		return channelDBDao.update("updateEmployee", param);
	}
	
}