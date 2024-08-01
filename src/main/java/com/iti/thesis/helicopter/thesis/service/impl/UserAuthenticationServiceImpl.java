package com.iti.thesis.helicopter.thesis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.common.SessionUtil;
import com.iti.thesis.helicopter.thesis.common.ErrorCode.ErrorCode;
import com.iti.thesis.helicopter.thesis.constant.UserStatusCode;
import com.iti.thesis.helicopter.thesis.constant.YnTypeCode;
import com.iti.thesis.helicopter.thesis.context.builder.SessionDataBuilder;
import com.iti.thesis.helicopter.thesis.context.parameter.MContextParameter;
import com.iti.thesis.helicopter.thesis.context.util.MHttpRequestUtil;
import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.core.exception.MNotFoundException;
import com.iti.thesis.helicopter.thesis.service.UserAuthenticationService;
import com.iti.thesis.helicopter.thesis.service.UserInfoService;
import com.iti.thesis.helicopter.thesis.util.MDateUtil;
import com.iti.thesis.helicopter.thesis.util.MPasswordUtil;
import com.iti.thesis.helicopter.thesis.util.MStringUtil;
import com.iti.thesis.helicopter.thesis.util.MValidatorUtil;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

	@Autowired
	private UserInfoService		userInfoService;
	@Value("${max.password.error}")
	int pwdMaxError;
	@Value("${user.lock.time}")
	int userLockTime;
	
	@Override
	public MData userLogin(MData param) throws MException {
		MData	response				= new MData();
		MData	userInfo				= new MData();
		int		userPasswdErrorCount	= 0;
		boolean isIncorrectPasswd		= false;
		boolean isUserLock				= false;
		try {
			MValidatorUtil.validate(param, "userID", "password");
			
			/* Retrieve Valid User for Login */
			userInfo				= userInfoService.retrieveUserInfoDetail(param);
			String statusCode		= userInfo.getString("statusCode");
			String lockDateTime		= userInfo.getString("lockDateTime");
			userPasswdErrorCount	= userInfo.getInt("userPasswordErrorCount");
			String	userPwd			= userInfo.getString("passwd");
			String	password		= param.getString("password");
			
			// If user is already login, then password need to encrypt
			if(YnTypeCode.YES.getValue().equals(userInfo.getString("loginByUserYn"))) {
				password	= MPasswordUtil.oneWayEnc(password, MStringUtil.EMPTY);
			}
			
			if(!MStringUtil.isEmpty(lockDateTime)) {
				Boolean isInLockTime = isInLockTime(lockDateTime);
				// If user status is lock, and lock time is active then return error
				if(UserStatusCode.LOCK.getValue().equals(statusCode) && isInLockTime) {
					isIncorrectPasswd = true;
					isUserLock = false;
					String lockMessage = String.format("Your user is locked, Please try again in %smn", "5");
					throw new MBizException(ErrorCode.USER_LOCK.getValue(), lockMessage);
				}
				// If user status is lock, and lock time is active then reset user info
				else if(!isInLockTime) {
					userPasswdErrorCount = 0;
					// Reset user data
					userInfo.setInt("userPasswordErrorCount", 0);
					userInfo.setString("lockDateTime", MStringUtil.EMPTY);
					userInfo.setString("statusCode", UserStatusCode.ACTIVE.getValue());
				}
			}
			
			if (!userPwd.equals(password)) {
				isIncorrectPasswd = true;
				userPasswdErrorCount = userInfo.getInt("userPasswordErrorCount") + 1;
				if(userPasswdErrorCount >= pwdMaxError) {
					isUserLock = true;
					String lockMessage = String.format("Your user is locked, Please try again in %smn", userLockTime);
					throw new MBizException(ErrorCode.INCORRECT_PASSWORD.getValue(), lockMessage);
				}
				throw new MBizException(ErrorCode.INCORRECT_PASSWORD.getValue(), ErrorCode.INCORRECT_PASSWORD.getDescription());
			} else {
				if(MStringUtil.isEmpty(userInfo.getString("firstLoginDate"))) {
					userInfo.setString("firstLoginDate", MDateUtil.getCurrentDate());
				}
				
				userInfo.setString("lastLoginDate", MDateUtil.getCurrentDate());
				userInfoService.updateUserLoginInfo(userInfo);
				response.appendFrom(userInfo);
				// Change Context data
				MData sessionData = SessionDataBuilder.create()
						.loginUserId(userInfo.getString("userID"))
						.userRoleID(userInfo.getString("roleID"))
						.build();
				// Update session data
				MContextParameter.setSessionContext(sessionData);
				SessionUtil.updateSession(sessionData);
				MHttpRequestUtil.setSessionAttribute(sessionData);
			}
			
		} catch (MNotFoundException e) {
			throw new MBizException(ErrorCode.USER_NOT_FOUND.getValue(), ErrorCode.USER_NOT_FOUND.getDescription());
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		} finally {
			MData errorData = new MData();
			errorData.setString("userID", userInfo.getString("userID"));
			errorData.setString("roleID", userInfo.getString("roleID"));
			errorData.setString("lockDateTime", userInfo.getString("lockDateTime"));
			errorData.setString("statusCode", userInfo.getString("statusCode"));
			errorData.setInt("userPasswordErrorCount", 0);
			if(isIncorrectPasswd){
				errorData.setInt("userPasswordErrorCount", userPasswdErrorCount);
				if(isUserLock) {
					errorData.setString("lockDateTime", MDateUtil.getCurrentDateTime());
					errorData.setString("statusCode", UserStatusCode.LOCK.getValue());
				}
			}
			userInfoService.updateUserLoginInfo(errorData);
		}
		return response;
	}
	
	private boolean isInLockTime(String lockDateTime) {
		String currentDateTime = MDateUtil.getCurrentDateTime();
		String lockDateTimeAfterAdd5Mn = MDateUtil.addTime(lockDateTime, 0, userLockTime, 0);
		return MDateUtil.compareTo(lockDateTimeAfterAdd5Mn, currentDateTime) > 0;
	}

}
