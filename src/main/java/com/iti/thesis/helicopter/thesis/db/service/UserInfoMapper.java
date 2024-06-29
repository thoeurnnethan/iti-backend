package com.iti.thesis.helicopter.thesis.db.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface UserInfoMapper {
	
	public MData retrieveUserInfoDetail(MData param) throws MException;
	public int updateUserInfoDetail(MData param) throws MException;
	public int registerUserInfoDetail(MData param) throws MException;
	public MData retrieveUserInfoDetailByUserIDAndRoleID(MData param) throws MException;
	public MMultiData registerUserInfoList(MData param) throws MException;
	public MData retrieveUserInfoTotalCount(MData param) throws MException;
	
}