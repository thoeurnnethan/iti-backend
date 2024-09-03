package com.iti.thesis.helicopter.thesis.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface UserInfoService {
	
	public MMultiData retrieveUserInfoList(MData param) throws MException;
	public MData retrieveUserInfoTotalCount(MData param) throws MException;
	public MData retrieveUserInfoDetail(MData param) throws MException;
	public MData retrieveUserInfoDetailSummary(MData param) throws MException;
	public MData registerUserInfoDetail(MData param) throws MException;
	public MData updateUserInfo(MData param) throws MException;
	public int updateUserLoginInfo(MData param) throws MException;
	public MData updateUserInfoResetPassword(MData param) throws MException;
	public MMultiData retrieveUserInfoListForDownload(MData param) throws MException;
	public MData changePassword(MData param) throws MException;
	
}
