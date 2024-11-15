package com.iti.thesis.helicopter.thesis.service.impl;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import com.iti.thesis.helicopter.thesis.common.ErrorCode.ErrorCode;
import com.iti.thesis.helicopter.thesis.constant.StatusCode;
import com.iti.thesis.helicopter.thesis.constant.UserRoleCode;
import com.iti.thesis.helicopter.thesis.constant.YnTypeCode;
import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.core.exception.MNotFoundException;
import com.iti.thesis.helicopter.thesis.db.service.UserInfoMapper;
import com.iti.thesis.helicopter.thesis.service.StudentDetailService;
import com.iti.thesis.helicopter.thesis.service.TeacherDetailService;
import com.iti.thesis.helicopter.thesis.service.UserInfoService;
import com.iti.thesis.helicopter.thesis.util.MGUIDUtil;
import com.iti.thesis.helicopter.thesis.util.MGenerateIDUtil;
import com.iti.thesis.helicopter.thesis.util.MPasswordUtil;
import com.iti.thesis.helicopter.thesis.util.MStringUtil;
import com.iti.thesis.helicopter.thesis.util.MValidatorUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserInfoServiceImpl implements UserInfoService {
	
	@Autowired
	private UserInfoMapper						userInfoMapper;
	@Autowired
	private StudentDetailService				studentDetailService;
	@Autowired
	private TeacherDetailService				teacherDetailService;
	@Autowired
	private PlatformTransactionManager			txManager;
	
	@Override
	public MMultiData retrieveUserInfoList(MData param) throws MException {
		try {
			MMultiData userList = userInfoMapper.retrieveUserInfoList(param);
			MMultiData filterList = userList.toListMData().stream()
					.map(data ->{
						MData user = data;
						String loginYn = data.getString("loginByUserYn");
						if(!MStringUtil.isEmpty(loginYn) && YnTypeCode.YES.getValue().equalsIgnoreCase(loginYn)) {
							user.setString("passwd", MStringUtil.EMPTY);
						}
						return user;
					}).collect(Collectors.toCollection(MMultiData::new));
			return filterList;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	@Override
	public MData retrieveUserInfoTotalCount(MData param) throws MException {
		try {
			return userInfoMapper.retrieveUserInfoTotalCount(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription());
		}
	}

	@Override
	public MMultiData retrieveUserInfoListForDownload(MData param) throws MException {
		try {
			MMultiData userList		= userInfoMapper.retrieveUserInfoListForDownload(param);
			MMultiData filterList	= userList.toListMData().stream()
					.map(data ->{
						MData user = data;
						String loginYn = data.getString("loginByUserYn");
						if(!MStringUtil.isEmpty(loginYn) && YnTypeCode.YES.getValue().equalsIgnoreCase(loginYn)) {
							user.setString("passwd", MStringUtil.EMPTY);
						}
						return user;
					}).collect(Collectors.toCollection(MMultiData::new));
			return filterList;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	@Override
	public MData retrieveUserInfoDetailSummary(MData param) throws MException {
		MData	response	= new MData();
		try {
			MValidatorUtil.validate(param, "userID");
			
			/* Retrieve Valid User for Login */
			MData	userInfo = userInfoMapper.retrieveUserInfoDetail(param);
			String	loginByUserYn = userInfo.getString("loginByUserYn");
			if(!MStringUtil.isEmpty(loginByUserYn) && YnTypeCode.YES.getValue().equalsIgnoreCase(loginByUserYn)) {
				userInfo.setString("passwd", MStringUtil.EMPTY);
			}
			response = userInfo;
			
			String userRoleCode = userInfo.getString("roleID");
			if (UserRoleCode.ADMIN.getValue().equals(userRoleCode)
					|| UserRoleCode.DEP_MANAGER.getValue().equals(userRoleCode)
					|| UserRoleCode.TEACHER.getValue().equals(userRoleCode)) {
				MData teacherParam = new MData();
				teacherParam.setString("teacherID", userInfo.getString("specificID"));
				teacherParam.setString("roleID", userInfo.getString("roleID"));
				response.setMData("teacherInfo", teacherDetailService.retrieveTeacherDetail(teacherParam));
			} else if (UserRoleCode.STUDENT.getValue().equals(userRoleCode)) {
				MData studentParam = new MData();
				studentParam.setString("studentID", userInfo.getString("specificID"));
				studentParam.setString("roleID", userInfo.getString("roleID"));
				response.setMData("studentInfo", studentDetailService.retrieveStudentDetailSummary(studentParam));
			}
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
		return response;
	}
	
	@Override
	public MData retrieveUserInfoDetail(MData param) throws MException {
		try {
			MValidatorUtil.validate(param, "userID");
			return userInfoMapper.retrieveUserInfoDetail(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}
	
	@Override
	public MData registerUserInfoDetail(MData param) throws MException {
		try {
			MValidatorUtil.validate(param, "userList");
			MData		outputData	= new MData();
			
			MMultiData	userList	= param.getMMultiData("userList");
			for(MData userInfo		: userList.toListMData()) {
				String	roleID		= userInfo.getString("roleID");
				String	userID		= this.generateUserID();
				String	passwd		= MGenerateIDUtil.generateUserPassword();
				
				userInfo.setString("userID", userID);
				userInfo.setString("passwd", passwd);
				userInfo.setString("statusCode", StatusCode.ACTIVE.getValue());
				userInfo.setString("loginByUserYn", YnTypeCode.NO.getValue());
				
				// Register User Information
				userInfoMapper.registerUserInfoDetail(userInfo);
				
				String specificID = MStringUtil.EMPTY;
				// Register Admin, Department Manager, Teacher Information
				if (UserRoleCode.ADMIN.getValue().equals(roleID) ||
						UserRoleCode.DEP_MANAGER.getValue().equals(roleID) ||
						UserRoleCode.TEACHER.getValue().equals(roleID)) {
					MData	teacherInfo		= userInfo.getMData("teacherInfo");
					teacherInfo.setString("roleID", roleID);
					MData	response		= teacherDetailService.registerTeacherDetail(teacherInfo);
					specificID = response.getString("teacherID");
				} 
				// Register Student Information 
				else if (UserRoleCode.STUDENT.getValue().equals(roleID)) {
					MData	studentInfo		= userInfo.getMData("studentInfo");
					MData	response		= studentDetailService.registerStudentDetail(studentInfo);
					specificID = response.getString("studentID");
				}else {
					throw new MException("User role not match !");
				}
				// Update Specific Identifier (StudentID or TeacherID)
				userInfo.setString("specificID", specificID);
				userInfoMapper.updateUserLoginInfo(userInfo);
				
				outputData.setString("userID", userID);
				outputData.setString("password", passwd);
			}
			return outputData;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	@SuppressWarnings("null")
	@Override
	public int updateUserLoginInfo(MData param) throws MException {
		String subTransactionName = "TX_SUB_updateLoginErr_"+ MGUIDUtil.generateGUID();
		DefaultTransactionAttribute defaultTransactionAttribute = new DefaultTransactionAttribute();
		defaultTransactionAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		defaultTransactionAttribute.setName(subTransactionName);
		TransactionStatus	txStatus	= null;
		log.info("Open Transaction : {}", subTransactionName);
		int result = 0;
		try {
			txStatus	= txManager.getTransaction(defaultTransactionAttribute);
			
			result		= userInfoMapper.updateUserLoginInfo(param);
			
			log.info("Commit Transaction : {}", subTransactionName);
			txManager.commit(txStatus);
		} catch (DataAccessException e) {
			if (txStatus != null) {
				txManager.rollback(txStatus);
			}
			log.error("{}", "Failed to open transaction due to database connection issue", e);
		} catch (MException e) {
			log.error("{}: {}", e.getMCode(), e.getMessageAddContents());
			txManager.rollback(txStatus);
		} catch (Exception e){
			log.error("{}: {}", e.getLocalizedMessage());
			txManager.rollback(txStatus);
		}
		return result;
	}

	@Override
	public MData updateUserInfo(MData param) throws MException {
		try {
			MValidatorUtil.validate(param, "userID");
			
			// Retrieve User Info 
			MData	userInfo	= userInfoMapper.retrieveUserInfoDetail(param);
			String	roleID		= userInfo.getString("roleID");
			
			// Update User Info
			param.setString("specificID", userInfo.getString("specificID"));
			userInfoMapper.updateUserInfo(param);
			if(!MStringUtil.isEmpty(param.getString("isDeleted")) && YnTypeCode.YES.getValue().equalsIgnoreCase(param.getString("isDeleted"))) {
				return param;
			}
			// Update Specific User Info
			if(UserRoleCode.ADMIN.getValue().equals(roleID)
					|| UserRoleCode.DEP_MANAGER.getValue().equals(roleID)
					|| UserRoleCode.TEACHER.getValue().equals(roleID)) {
				MValidatorUtil.validate(param, "teacherInfo");
				
				MData	teacherInfo		= param.getMData("teacherInfo");
				teacherInfo.setString("teacherID", userInfo.getString("specificID"));
				teacherDetailService.updateTeacherDetail(teacherInfo);
			}else if(UserRoleCode.STUDENT.getValue().equals(roleID)) {
				MValidatorUtil.validate(param, "studentInfo");

				MData	studentInfo		= param.getMData("studentInfo");
				studentInfo.setString("studentID", userInfo.getString("specificID"));
				studentDetailService.updateStudentDetail(studentInfo);
			}
			
			return param;
		} catch (MNotFoundException e) {
			throw new MException(ErrorCode.USER_NOT_FOUND.getValue(), ErrorCode.USER_NOT_FOUND.getDescription());
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription());
		}
	}

	@Override
	public MData updateUserInfoResetPassword(MData param) throws MException {
		MData response = new MData();
		try {
			MValidatorUtil.validate(param, "LIUserID","resetUserID");
			MData	adminInfo	= new MData();
			adminInfo.setString("userID", param.getString("LIUserID"));
			adminInfo	= userInfoMapper.retrieveUserInfoDetail(adminInfo);
			String adminRole = adminInfo.getString("roleID");
			if(!UserRoleCode.ADMIN.getValue().equals(adminRole)) {
				throw new MException(ErrorCode.ONLY_ADMIN_CAN_UPDATE.getValue(), ErrorCode.ONLY_ADMIN_CAN_UPDATE.getDescription());
			}else {
				MData userInfo = new MData();
				userInfo.setString("userID", param.getString("resetUserID"));
				userInfo = userInfoMapper.retrieveUserInfoDetail(userInfo);
				String userRoleID = userInfo.getString("roleID");
				if(UserRoleCode.ADMIN.getValue().equals(userRoleID)) {
					throw new MException(ErrorCode.CANNOT_RESET_ADMIN_PASS.getValue(), ErrorCode.CANNOT_RESET_ADMIN_PASS.getDescription());
				}else {
					String newUserID		= this.generateUserID();
					String newUserPasword	= MGenerateIDUtil.generateUserPassword();
					userInfo.setString("newUserID", newUserID);
					userInfo.setString("newUserPasword", newUserPasword);
					userInfo.setInt("userPasswordErrorCount", 0);
					userInfo.setString("lockDateTime", MStringUtil.EMPTY);
					userInfo.setString("loginByUserYn", MStringUtil.EMPTY);
					userInfo.setString("firstLoginDate", MStringUtil.EMPTY);
					userInfo.setString("statusCode", StatusCode.ACTIVE.getValue());
					userInfoMapper.updateUserInfoResetPassword(userInfo);
					response.setString("userID", newUserID);
					response.setString("password", newUserPasword);
				}
			}
			
			return response;
		} catch (MNotFoundException e) {
			throw new MException(ErrorCode.USER_NOT_FOUND.getValue(), ErrorCode.USER_NOT_FOUND.getDescription());
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription());
		}
	}

	private String generateUserID() {
		try {
			MData	param			= new MData();
			String	userID			= MStringUtil.EMPTY;
			Boolean	isValidUserID	= false;
			while(!isValidUserID) {
				userID = MGenerateIDUtil.generateUserID();
				param.setString("userID", userID);
				if (!isValidUserID) {
					isValidUserID = this.isValidUserID(param);
					return userID;
				}
			}
			return userID;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription());
		}
	}

	private boolean isValidUserID(MData param) {
		try {
			userInfoMapper.retrieveUserInfoAllStatus(param);
			return false;
		} catch (MNotFoundException e) {
			return true;
		}
	}

	@Override
	public MData changePassword(MData param) throws MException {
		try {
			MValidatorUtil.validate(param, "userID","oldPasswd", "newPasswd");
			MData	userInfo		= userInfoMapper.retrieveUserInfoDetail(param);
			String	loginByUserYn	= userInfo.getString("loginByUserYn");
			String	statusCode		= userInfo.getString("statusCode");
			if(StatusCode.INACTIVE.getValue().equals(statusCode)) {
				throw new MException(ErrorCode.USER_NOT_ACTIVE.getValue(),ErrorCode.USER_NOT_ACTIVE.getDescription());
			}
			if(YnTypeCode.YES.getValue().equals(loginByUserYn)) {
				throw new MException(ErrorCode.ALREADY_LOGIN.getValue(),ErrorCode.ALREADY_LOGIN.getDescription());
			}
			String oldPassword	= URLDecoder.decode(userInfo.getString("passwd"), StandardCharsets.UTF_8.toString());
			String password		= URLDecoder.decode(param.getString("oldPasswd"), StandardCharsets.UTF_8.toString());
			if(!oldPassword.equals(password)) {
				throw new MException(ErrorCode.OLD_PASSWORD_NOT_MATCH.getValue(),ErrorCode.OLD_PASSWORD_NOT_MATCH.getDescription());
			}
			String newPasswd	= URLDecoder.decode(param.getString("newPasswd"), StandardCharsets.UTF_8.toString());
			String encPasswd	= MPasswordUtil.oneWayEnc(newPasswd, MStringUtil.EMPTY);
			param.setString("roleID", userInfo.getString("roleID"));
			param.setString("loginByUserYn", YnTypeCode.YES.getValue());
			param.setString("newPasswd", encPasswd);
			userInfoMapper.updateUserPassword(param);
			return param;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription());
		}
	}

	@Override
	public MData registerUserDefault(MData param) throws MException {
		try {
			param.setString("userID", "Admin");
			boolean isExist = this.isValidUserID(param);
			if(!isExist) {
				return param;
			}else {
				MData inputParam = new MData();
				inputParam.setString("userID", "Admin");
				inputParam.setString("passwd", "Admin");
				inputParam.setString("firstName", "Admin");
				inputParam.setString("lastName", "Admin");
				inputParam.setString("roleID", UserRoleCode.ADMIN.getValue());
				inputParam.setString("statusCode", StatusCode.ACTIVE.getValue());
				inputParam.setString("loginByUserYn", YnTypeCode.NO.getValue());
				inputParam.setString("gender", "M");
				inputParam.setString("address", "NA");
				inputParam.setString("phone", "NA");
				inputParam.setString("email", "admin123@gmail.com");
				inputParam.setMMultiData("qualificationList", new MMultiData());
				MData adminInfo = teacherDetailService.registerTeacherDetail(inputParam);
				inputParam.setString("specificID", adminInfo.getString("teacherID"));
				userInfoMapper.registerUserInfoDetail(inputParam);
				return inputParam;
			}
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription());
		}
	}
	
}
