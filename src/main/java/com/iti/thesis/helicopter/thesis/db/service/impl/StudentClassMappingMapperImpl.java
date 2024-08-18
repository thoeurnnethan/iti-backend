package com.iti.thesis.helicopter.thesis.db.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.db.dao.ChannelDBDao;
import com.iti.thesis.helicopter.thesis.db.service.StudentClassMappingMapper;

@Service
public class StudentClassMappingMapperImpl implements StudentClassMappingMapper {

	@Autowired
	private ChannelDBDao channelDBDao;

	@Override
	public int registerStudentClassMappingInfo(MData param) throws MException {
		return channelDBDao.insert("registerStudentClassMappingInfo", param);
	}

	@Override
	public int updateStudentClassMappingInfo(MData student) throws MException {
		return channelDBDao.update("updateStudentClassMappingInfo", student);
	}

	@Override
	public MData retrieveStudentClassMappingInfo(MData data) throws MException {
		return channelDBDao.selectOne("retrieveStudentClassMappingInfo", data);
	}
	
}