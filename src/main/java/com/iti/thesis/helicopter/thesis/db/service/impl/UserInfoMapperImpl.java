package com.iti.thesis.helicopter.thesis.db.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.db.dao.ChannelDBDao;
import com.iti.thesis.helicopter.thesis.db.service.UserInfoMapper;

@Service
public class UserInfoMapperImpl implements UserInfoMapper {
	
	@Autowired
	private ChannelDBDao channelDBDao;
	
	@Override
	public MMultiData registerUserInfoList(MData param) throws MException {
		return channelDBDao.selectListForPage("registerUserInfoList", param);
	}
	
	@Override
	public MData retrieveUserInfoDetail(MData param) throws MException {
		return channelDBDao.selectOne("retrieveUserInfoDetail", param);
	}
	
	@Override
	public int updateUserLoginInfo(MData param) throws MException {
		return channelDBDao.update("updateUserLoginInfo", param);
	}
	
	@Override
	public int registerUserInfoDetail(MData param) throws MException {
		return channelDBDao.insert("registerUserInfoDetail", param);
	}
	
	@Override
	public MData retrieveUserInfoDetailByUserIDAndRoleID(MData param) throws MException {
		return channelDBDao.selectOne("retrieveUserInfoDetailByUserIDAndRoleID", param);
	}

	@Override
	public MData retrieveUserInfoTotalCount(MData param) throws MException {
		return channelDBDao.selectOne("retrieveUserInfoTotalCount", param);
	}

	@Override
	public int updateUserInfo(MData param) throws MException {
		return channelDBDao.update("updateUserInfo", param);
	}
	
}