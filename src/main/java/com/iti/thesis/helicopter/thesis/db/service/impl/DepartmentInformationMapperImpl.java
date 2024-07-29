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
	public MMultiData retrieveDepartmentInformationList(MData param) throws MException {
		return channelDBDao.selectListForPage("retrieveDepartmentInformationList", param);
	}

	@Override
	public MData retrieveDepartmentInformationTotalCount(MData param) throws MException {
		return channelDBDao.selectOne("retrieveDepartmentInformationTotalCount", param);
	}

	@Override
	public MMultiData retrieveDepartmentInformationListForDownload(MData param) throws MException {
		return channelDBDao.selectList("retrieveDepartmentInformationListForDownload", param);
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
	public int updateDepartmentInformation(MData param) throws MException {
		return channelDBDao.update("updateDepartmentInformation", param);
	}

	@Override
	public MData retrieveLastDepartmentID(MData param) throws MException {
		return channelDBDao.selectOne("retrieveLastDepartmentID", param);
	}
	
}