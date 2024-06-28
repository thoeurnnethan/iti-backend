package com.iti.thesis.helicopter.thesis.db.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.db.dao.ChannelDBDao;
import com.iti.thesis.helicopter.thesis.db.service.DepartmentInformationMapper;

@Service
public class DepartmentInformationMapperImpl implements DepartmentInformationMapper {

	@Autowired
	private ChannelDBDao channelDBDao;

	@Override
	public MMultiData retrieveDepartmentInformationList(MData requestBody) throws MException {
		return channelDBDao.selectListForPage("retrieveDepartmentInformationList", requestBody);
	}

	@Override
	public MData retrieveDepartmentInformationTotalCount(MData param) throws MException {
		return channelDBDao.selectOne("retrieveDepartmentInformationTotalCount", param);
	}

	@Override
	public int registerDepartmentInformation(MData param) throws MException {
		return channelDBDao.insert("registerDepartmentInformation", param);
	}

	@Override
	public MData retrieveDepartmentInformationDetail(MData param) throws MException {
		return channelDBDao.selectOne("retrieveDepartmentInformationDetail", param);
	}

	@Override
	public MData retrieveLastNewsEventID(MData param) throws MException {
		return channelDBDao.selectOne("retrieveLastNewsEventID", param);
	}

	@Override
	public int updateDepartmentInformation(MData param) throws MException {
		return channelDBDao.update("updateDepartmentInformation", param);
	}
	
}