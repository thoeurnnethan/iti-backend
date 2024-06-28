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

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
//				updateSessionInfoData(param);
				userInfo.setString("lastLoginDate", MDateUtil.getCurrentDate());
				userInfoService.updateUserInfoDetail(userInfo);
				response.appendFrom(userInfo);
				// Change Context data
				log.error(MContextParameter.getSessionContext().toString());
				MData sessionData = SessionDataBuilder.create()
						.loginUserId(userInfo.getString("userID"))
						.userRoleID(userInfo.getString("roleID"))
						.build();
				// Update session data
				MContextParameter.setSessionContext(sessionData);
				log.error(MContextParameter.getSessionContext().toString());
				SessionUtil.updateSession(sessionData);
				log.error(SessionUtil.getSessionId());
				log.error(SessionUtil.isLoggedin()+"");
				MData session = MContextParameter.getSessionContext();
				response.setMData("session", session);
				response.appendFrom(userInfo);
			}
			
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
			if(isIncorrectPasswd){
				errorData.setInt("userPasswordErrorCount", userPasswdErrorCount);
				if(isUserLock) {
					errorData.setString("lockDateTime", MDateUtil.getCurrentDateTime());
					errorData.setString("statusCode", UserStatusCode.LOCK.getValue());
				}
			}
			userInfoService.updateUserInfoDetail(errorData);
		}
		return response;
	}
	
	private MData updateSessionInfoData(MData param) {
		MData sessionMData = new MData();
		try {
			MValidatorUtil.validate(param, "userID");
			MData	userLoginInfoMData	= new MData();
			try {
				userLoginInfoMData	= userInfoService.retrieveUserInfoDetail(param);
			} catch (MNotFoundException e) {
				userLoginInfoMData	= new MData();
			}
			
			//=========================================================
			//# Start Creating Session
			//=========================================================
			String userID = param.getString("userID");
			sessionMData = SessionDataBuilder.create()
					.loginUserId(userID)
					.build();
			//change context data
			MContextParameter.setSessionContext(sessionMData);
			//update session data
			SessionUtil.updateSession(sessionMData);
			log.info("Write SessionID>>>{}", SessionUtil.getSessionId());
			log.info("Write Data>>>{}", sessionMData);
			
			/* kill duplicated session */
			String	sessionId = userLoginInfoMData.getString("loginUuID");
			log.info("Trying Read SesionID <<< {}", sessionId);
			//#########################################################
			//# End Creating Session
			//#########################################################
			
			MData	userLoginInfo			= new MData();
//			MData	userLoginInfoForUpdate	= retrieveUserLoginInfoForUpdate(param);
			userLoginInfo.setString("userID", param.getString("userID"));
			userLoginInfo.setString("deviceID", param.getString("deviceID"));
			userLoginInfo.setString("loginUuID", SessionUtil.getSessionId());
			userLoginInfo.setString("lastLoginDate", MDateUtil.getCurrentDate());
			userLoginInfo.setString("lastLoginTime", MDateUtil.getCurrentTime());
			
		} catch (MException e) {
			throw e;
		} catch (Exception e) {
			throw new MBizException("00017", "Create Session Uncaught Exception Error");
		}
		
		return sessionMData;
	}
	
	private boolean isInLockTime(String lockDateTime) {
		String currentDateTime = MDateUtil.getCurrentDateTime();
		String lockDateTimeAfterAdd5Mn = MDateUtil.addTime(lockDateTime, 0, userLockTime, 0);
		return MDateUtil.compareTo(lockDateTimeAfterAdd5Mn, currentDateTime) > 0;
	}

}
