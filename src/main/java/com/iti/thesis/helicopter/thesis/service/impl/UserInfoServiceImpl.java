package com.iti.thesis.helicopter.thesis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import com.iti.thesis.helicopter.thesis.common.ErrorCode.UserErrorCode;
import com.iti.thesis.helicopter.thesis.constant.StatusCode;
import com.iti.thesis.helicopter.thesis.constant.UserRoleCode;
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
import com.iti.thesis.helicopter.thesis.util.MStringUtil;
import com.iti.thesis.helicopter.thesis.util.MValidatorUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserInfoServiceImpl implements UserInfoService {
	
	@Autowired
	private UserInfoMapper			userInfoMapper;
	@Autowired
	private StudentDetailService	studentDetailService;
	@Autowired
	private TeacherDetailService	teacherDetailService;
	@Autowired
	private PlatformTransactionManager			txManager;
	
	@Override
	public MMultiData retrieveUserInfoList(MData param) throws MException {
		try {
			return userInfoMapper.retrieveUserInfoList(param);
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
	public MData retrieveUserInfoDetail(MData param) throws MException {
		MData	response	= new MData();
		try {
			MValidatorUtil.validate(param, "userID");
			
			/* Retrieve Valid User for Login */
			MData	userInfo = userInfoMapper.retrieveUserInfoDetail(param);
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
	public MData registerUserInfoDetail(MData param) throws MException {
		try {
			MValidatorUtil.validate(param, "userList");
			MData		outputData	= new MData();
			
			MMultiData	userList	= param.getMMultiData("userList");
			for(MData userInfo		: userList.toListMData()) {
				String	roleID		= userInfo.getString("roleID");
				String	userID		= this.generateUserID(userInfo);
				String	passwd		= MGenerateIDUtil.generateUserPassword();
				
				userInfo.setString("userID", userID);
				userInfo.setString("passwd", passwd);
				userInfo.setString("statusCode", StatusCode.ACTIVE.getValue());
				
				// Register User Information
				userInfoMapper.registerUserInfoDetail(userInfo);
				
				String specificID = MStringUtil.EMPTY;
				// Register Admin, Department Manager, Teacher Information
				if (UserRoleCode.ADMIN.getValue().equals(roleID) ||
						UserRoleCode.DEP_MANAGER.getValue().equals(roleID) ||
						UserRoleCode.TEACHER.getValue().equals(roleID)) {
					MData	teacherInfo		= userInfo.getMData("teacherInfo");
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

	private String generateUserID(MData param) {
		try {
			MValidatorUtil.validate(param, "roleID");
			String	userID			= MStringUtil.EMPTY;
			Boolean	isValidUserID	= false;
			while(!isValidUserID) {
				userID = MGenerateIDUtil.generateUserID();
				param.setString("userID", userID);
				isValidUserID = isValidUserID(param);
				if (isValidUserID) {
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
			userInfoMapper.retrieveUserInfoDetailByUserIDAndRoleID(param);
			return false;
		} catch (MNotFoundException e) {
			return true;
		}
	}
	

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
			throw new MException(UserErrorCode.USER_NOT_FOUND.getValue(), UserErrorCode.USER_NOT_FOUND.getDescription());
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription());
		}
	}
	
}
