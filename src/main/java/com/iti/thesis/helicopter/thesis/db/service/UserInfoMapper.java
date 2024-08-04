package com.iti.thesis.helicopter.thesis.db.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface UserInfoMapper {
	
	public int registerUserInfoDetail(MData param) throws MException;
	public MData retrieveUserInfoDetail(MData param) throws MException;
	public MData retrieveUserInfoAllStatus(MData param) throws MException;
	public MMultiData retrieveUserInfoList(MData param) throws MException;
	public MData retrieveUserInfoTotalCount(MData param) throws MException;
	public int updateUserInfo(MData param) throws MException;
	public int updateUserLoginInfo(MData param) throws MException;
	public int updateUserInfoResetPassword(MData param) throws MException;
	public MMultiData retrieveUserInfoListForDownload(MData param) throws MException;
	
}