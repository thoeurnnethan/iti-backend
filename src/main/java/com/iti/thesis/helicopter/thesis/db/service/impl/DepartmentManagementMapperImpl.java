package com.iti.thesis.helicopter.thesis.db.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.db.dao.ChannelDBDao;
import com.iti.thesis.helicopter.thesis.db.service.DepartmentManagementMapper;

@Service
public class DepartmentManagementMapperImpl implements DepartmentManagementMapper {

	@Autowired
	private ChannelDBDao channelDBDao;

	@Override
	public MMultiData retrieveDepartmentManagementList(MData requestBody) throws MException {
		return channelDBDao.selectList("retrieveDepartmentManagementList", requestBody);
	}

	@Override
	public MData retrieveDepartmentManagementTotalCount(MData param) throws MException {
		return channelDBDao.selectOne("retrieveDepartmentManagementTotalCount", param);
	}

	@Override
	public int registerDepartmentManagement(MData param) throws MException {
		return channelDBDao.insert("registerDepartmentManagement", param);
	}

	@Override
	public MData retrieveDepartmentManagementDetail(MData param) throws MException {
		return channelDBDao.selectOne("retrieveDepartmentManagementDetail", param);
	}
	
	@Override
	public MData retrieveDepartmentManager(MData param) throws MException {
		return channelDBDao.selectOne("retrieveDepartmentManager", param);
	}

	@Override
	public MData retrieveLastNewsEventID(MData param) throws MException {
		return channelDBDao.selectOne("retrieveLastNewsEventID", param);
	}

	@Override
	public int updateDepartmentManagement(MData param) throws MException {
		return channelDBDao.update("updateDepartmentManagement", param);
	}
	
}