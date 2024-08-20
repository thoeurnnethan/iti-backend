package com.iti.thesis.helicopter.thesis.service.impl;

import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.common.ErrorCode.ErrorCode;
import com.iti.thesis.helicopter.thesis.constant.StatusCode;
import com.iti.thesis.helicopter.thesis.constant.YnTypeCode;
import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.core.exception.MNotFoundException;
import com.iti.thesis.helicopter.thesis.db.service.DepartmentManagementMapper;
import com.iti.thesis.helicopter.thesis.service.DepartmentInformationService;
import com.iti.thesis.helicopter.thesis.service.DepartmentManagementService;
import com.iti.thesis.helicopter.thesis.util.MStringUtil;
import com.iti.thesis.helicopter.thesis.util.MValidatorUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DepartmentManagementServiceImpl implements DepartmentManagementService {
	
	@Autowired
	private DepartmentManagementMapper		departmentManagementMapper;
	@Autowired 
	private DepartmentInformationService	departmentInformationService;
	
	@Override
	public MMultiData retrieveDepartmentManagementList(MData param) throws MException {
		try {
			return departmentManagementMapper.retrieveDepartmentManagementList(param);
		} catch (MException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	@Override
	public MData retrieveDepartmentManagementTotalCount(MData param) throws MException {
		try {
			return departmentManagementMapper.retrieveDepartmentManagementTotalCount(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	@Override
	public MData registerDepartmentManagement(MData param, boolean isRegister) {
		try {
			MValidatorUtil.validate(param, "departmentID", "teacherList");
			MMultiData	resList			= new MMultiData();
			boolean		isNotValid		= false;
			MData		departmentInfo	= departmentInformationService.retrieveDepartmentInformationDetail(param);
			if(!departmentInfo.isEmpty()) {
				MMultiData	teacherList	= param.getMMultiData("teacherList");
				boolean hasDuplicate = isDuplicateTeacherID(teacherList);
				if(hasDuplicate) {
					throw new MException(ErrorCode.DUPLICATE_TEACHER_ID.getValue(), ErrorCode.DUPLICATE_TEACHER_ID.getDescription());
				}
				
				boolean isDuplicateManager = isRoleCodeDuplicate(teacherList, "01");
				if(isDuplicateManager) {
					throw new MException(ErrorCode.DUPLICATE_MANAGER_ID.getValue(), ErrorCode.DUPLICATE_MANAGER_ID.getDescription());
				}
				
				for(MData teacher : teacherList.toListMData()) {
					teacher.setString("departmentID", param.getString("departmentID"));
					String	messageText		= MStringUtil.EMPTY;
					String	alreadyExist	= YnTypeCode.NO.getValue();
					MData	depManagement	= this.retrieveAndValidateDepartmentManagement(teacher);
					if(!depManagement.isEmpty()) {
						if(isRegister) {
							isNotValid		= true;
							messageText		= "Already Register";
							alreadyExist	= YnTypeCode.YES.getValue();
						}else {
							teacher.setString("departmentRoleCode", teacher.getString("roleCode"));
							departmentManagementMapper.updateDepartmentManagement(teacher);
						}
					}else if(!isRegister) {
						String teacherRoleInDpm = teacher.getString("roleCode"); 
						if(teacherRoleInDpm.equals("01")) {
							MData checkParam = new MData();
							checkParam.setString("departmentID", param.getString("departmentID"));
							checkParam.setString("departmentRoleCode", teacherRoleInDpm);
							MData isManagerExist = this.retrieveAndValidateDepartmentManagement(checkParam);
							if(!isManagerExist.isEmpty()) {
								throw new MException(ErrorCode.DUPLICATE_MANAGER_ID.getValue(), ErrorCode.DUPLICATE_MANAGER_ID.getDescription());
							}
						}
						teacher.setString("departmentRoleCode", teacher.getString("roleCode"));
						teacher.setString("statusCode", StatusCode.ACTIVE.getValue());
						departmentManagementMapper.registerDepartmentManagement(teacher);
					}
					teacher.setString("alreadyExist", alreadyExist);
					teacher.setString("messageText", messageText);
					resList.addMData(teacher);
				}
				
				if(!isNotValid && isRegister) {
					for(MData teacher : resList.toListMData()) {
						teacher.setString("statusCode", StatusCode.ACTIVE.getValue());
						teacher.setString("departmentRoleCode", teacher.getString("roleCode"));
						departmentManagementMapper.registerDepartmentManagement(teacher);
					}
				}
				
			}
			MData response = new MData();
			response.setString("successYn", !isNotValid ? YnTypeCode.YES.getValue() : YnTypeCode.NO.getValue());
			response.setString("departmentID", param.getString("departmentID"));
			MMultiData responseList = this.retrieveDepartmentManagementList(response);
			response.setInt("totalCount", responseList.size());
			response.setMMultiData("teacherList", this.retrieveDepartmentManagementList(response));
			return response;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}
	
	private static boolean isDuplicateTeacherID(MMultiData teacherList) {
		return teacherList.stream().map(teacher -> teacher.get("teacherID"))
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).values().stream()
				.anyMatch(count -> count > 1);
	}
	
	private static boolean isRoleCodeDuplicate(MMultiData list, String roleCode) {
		return list.stream().filter(item -> roleCode.equals(item.get("roleCode")))
				.map(item -> item.get("roleCode"))
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).values()
				.stream()
				.anyMatch(count -> count > 1);
	}
	
	private MData retrieveAndValidateDepartmentManagement(MData param) {
		try {
			return this.retrieveDepartmentManagementDetail(param);
		} catch (MNotFoundException e) {
			return new MData();
		}
	}
	
	@Override
	public MData retrieveDepartmentManagementDetail(MData param) throws MException {
		MData outputData = new MData();
		try {
			outputData = departmentManagementMapper.retrieveDepartmentManagementDetail(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
		return outputData;
	}

	@Override
	public MData updateDepartmentManagement(MData param) throws MException {
		try {
			return this.registerDepartmentManagement(param, false);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

}
