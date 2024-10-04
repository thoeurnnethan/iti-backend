package com.iti.thesis.helicopter.thesis.service.impl;

import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.common.ErrorCode.ErrorCode;
import com.iti.thesis.helicopter.thesis.constant.ConstantCodePrefix;
import com.iti.thesis.helicopter.thesis.constant.StatusCode;
import com.iti.thesis.helicopter.thesis.constant.YnTypeCode;
import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.core.exception.MNotFoundException;
import com.iti.thesis.helicopter.thesis.db.service.ClassInformationMapper;
import com.iti.thesis.helicopter.thesis.db.service.DepartmentInformationMapper;
import com.iti.thesis.helicopter.thesis.db.service.StudentClassMappingMapper;
import com.iti.thesis.helicopter.thesis.service.ClassInformationService;
import com.iti.thesis.helicopter.thesis.util.MStringUtil;
import com.iti.thesis.helicopter.thesis.util.MValidatorUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ClassInformationServiceImpl implements ClassInformationService {
	
	@Autowired
	private ClassInformationMapper		classInformationMapper;
	@Autowired
	private DepartmentInformationMapper	departmentInformationMapper;
	@Autowired 
	private StudentClassMappingMapper	studentClassMappingMapper;
	
	@Override
	public MMultiData retrieveClassInformationList(MData param) throws MException {
		try {
			return classInformationMapper.retrieveClassInformationList(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	@Override
	public MData retrieveClassInformationTotalCount(MData param) throws MException {
		try {
			return classInformationMapper.retrieveClassInformationTotalCount(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	@Override
	public MMultiData retrieveClassInformationListForDownload(MData param) throws MException {
		try {
			return classInformationMapper.retrieveClassInformationListForDownload(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	@Override
	public MMultiData retrieveClassInformationStudentList(MData param) throws MException {
		try {
			MValidatorUtil.validate(param, "classInfoID");
			return classInformationMapper.retrieveClassInformationStudentList(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	@Override
	public MData registerClassInformation(MData param) {
		MData	outputData		= param;
		try {
			MValidatorUtil.validate(param, "departmentID","cyear","semester", "className","generation");
			MData	departmentInfo	= departmentInformationMapper.retrieveDepartmentInformationDetail(param);
			if(!departmentInfo.isEmpty()) {
				String classID = this.retrieveLastClassID(param);
				param.setString("classID", classID);
				param.setString("classInfoID", classID + param.getString("cyear")+param.getString("semester"));
				classInformationMapper.registerClassInformation(param);
			}
			return outputData;
		} catch (MNotFoundException e) {
			throw new MException(ErrorCode.DEPARTMENT_NOT_FOUND.getValue(), ErrorCode.DEPARTMENT_NOT_FOUND.getDescription());
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}
	
	private String retrieveLastClassID(MData param) {
		int result = 0 ;
		try {
			MData	classInfo	= classInformationMapper.retrieveLastClassID(param);
			String	resultStr	= classInfo.getString("classID");
			resultStr	= resultStr.substring(ConstantCodePrefix.CLASS.getValue().length(), resultStr.length());
			result		= Integer.valueOf(resultStr);
			result ++;
		} catch (MNotFoundException e) {
			result = 1001;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
		return ConstantCodePrefix.CLASS.getValue() + String.valueOf(String.format("%04d", result));
	}
	
	@Override
	public MData retrieveClassInformationDetail(MData param) throws MException {
		MData outputData = new MData();
		try {
			outputData = classInformationMapper.retrieveClassInformationDetail(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
		return outputData;
	}

	@Override
	public MData updateClassInformation(MData param) throws MException {
		try {
			MValidatorUtil.validate(param, "classID","departmentID","className","year","generation","semester","statusCode");
			MData classInfo = classInformationMapper.retrieveClassInformationDetail(param);
			if(!classInfo.isEmpty()) {
				classInformationMapper.updateClassInformation(param);
			}
			return param;
		} catch (MNotFoundException e) {
			throw new MException(ErrorCode.CLASS_NOT_FOUND.getValue(), ErrorCode.CLASS_NOT_FOUND.getDescription());
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}
	
	@Override
	public MData registerStudentToClassInformation(MData param, boolean isRegister) throws MException {
		try {
			MValidatorUtil.validate(param, "classInfoID", "studentList");
			MMultiData	resList		= new MMultiData();
			boolean		isNotValid	= false;
			MData		classInfo	= new MData();
			classInfo.setString("classID", param.getString("classInfoID"));
			classInfo = classInformationMapper.retrieveClassInformationDetailByClassInfoID(classInfo);
			if(!classInfo.isEmpty()) {
				MMultiData studentList = param.getMMultiData("studentList");
				boolean isDuplicateStudent = studentList.stream().map(teacher -> teacher.get("studentID"))
							.collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).values().stream()
							.anyMatch(count -> count > 1);
				if(isDuplicateStudent) {
					throw new MException(ErrorCode.DUPLICATE_STUDENT_ID.getValue(), ErrorCode.DUPLICATE_STUDENT_ID.getDescription());
				}
				for(MData student : studentList.toListMData()) {
					student.setString("classInfoID", param.getString("classInfoID"));
					isNotValid = false;
					String	messageText		= MStringUtil.EMPTY;
					String	alreadyExist	= YnTypeCode.NO.getValue();
					String	statusCode		= student.getString("statusCode");
					boolean	isExist			= this.isStudentExistInClass(student);
					if(isExist) {
						if(isRegister) {
							isNotValid		= true;
							messageText		= "Already Register";
							alreadyExist	= YnTypeCode.YES.getValue();
							if(StatusCode.DELETE.getValue().equals(student.getString("statusCode"))) {
								student.setString("statusCode", StatusCode.ACTIVE.getValue());
								student.setString("scoreID", param.getString("classInfoID") + student.getString("studentID"));
								studentClassMappingMapper.updateStudentClassMappingInfo(student);
								isNotValid		= true;
								messageText		= "";
								alreadyExist	= YnTypeCode.YES.getValue();
							}
						}else {
							student.setString("statusCode", statusCode);
							studentClassMappingMapper.updateStudentClassMappingInfo(student);
						}
					} else if(!isRegister) {
						student.setString("statusCode", StatusCode.ACTIVE.getValue());
						student.setString("scoreID", param.getString("classInfoID") + student.getString("studentID"));
						studentClassMappingMapper.registerStudentClassMappingInfo(student);
					}
					student.setString("alreadyExist", alreadyExist);
					student.setString("messageText", messageText);
					resList.addMData(student);
				}
				if(!isNotValid && isRegister) {
					for(MData student : resList.toListMData()) {
						student.setString("statusCode", StatusCode.ACTIVE.getValue());
						student.setString("scoreID", param.getString("classInfoID") + student.getString("studentID"));
						studentClassMappingMapper.registerStudentClassMappingInfo(student);
					}
				}
			}
			MData response = new MData();
			response.setString("successYn", !isNotValid ? YnTypeCode.YES.getValue() : YnTypeCode.NO.getValue());
			response.setString("classInfoID", param.getString("classInfoID"));
			MMultiData stdResList = this.retrieveClassInformationStudentList(param);
			response.setInt("totalCount", stdResList.size());
			response.setMMultiData("studentList", stdResList);
			return response;
		} catch (MNotFoundException e) {
			throw new MException(ErrorCode.CLASS_NOT_FOUND.getValue(), ErrorCode.CLASS_NOT_FOUND.getDescription());
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}
	
	private boolean isStudentExistInClass(MData data) {
		try {
			MData resData = studentClassMappingMapper.retrieveStudentInClass(data);
			data.appendFrom(resData);
			return true;
		} catch (MNotFoundException e) {
			return false;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	@Override
	public MData upgradeClassInformation(MData param) throws MException {
		MData outputData = new MData();
		try {
			MValidatorUtil.validate(param, "classID","oldYear","oldSemester","newYear","newSemester");
			boolean valid = this.checkIsValidClassUpdgrade(param);
			if(!valid) {
				throw new MException(ErrorCode.CLASS_INVALID_UPGRADE.getValue(), ErrorCode.CLASS_INVALID_UPGRADE.getDescription());
			}else {
				MData oldClassInfo = new MData();
				oldClassInfo.setString("classID", param.getString("classID") + param.getString("oldYear") + param.getString("oldSemester"));
				oldClassInfo = classInformationMapper.retrieveClassInformationDetailByClassInfoID(oldClassInfo);
				
				MData newClassInfo = new MData();
				newClassInfo.setString("classID", param.getString("classID"));
				newClassInfo.setString("cyear", param.getString("newYear"));
				newClassInfo.setString("semester", param.getString("newSemester"));
				boolean isExist = this.checkNewClassExist(newClassInfo);
				String classInfoID = param.getString("classID") + param.getString("newYear") + param.getString("newSemester");
				if(isExist) {
					newClassInfo.setString("year", newClassInfo.getString("cyear"));
					newClassInfo.setString("statusCode", StatusCode.ACTIVE.getValue());
					classInformationMapper.updateClassInformation(newClassInfo);
				}else {
					MData classInfo = new MData();
					classInfo.setString("classID", param.getString("classID"));
					classInfo.setString("cyear", param.getString("newYear"));
					classInfo.setString("semester", param.getString("newSemester"));
					classInfo.setString("classInfoID", classInfoID);
					classInfo.setString("departmentID", oldClassInfo.getString("departmentID"));
					classInfo.setString("className", oldClassInfo.getString("className"));
					classInfo.setString("classDesc", oldClassInfo.getString("classDesc"));
					classInfo.setString("classType", oldClassInfo.getString("classType"));
					classInfo.setString("generation", oldClassInfo.getString("generation"));
					classInfo.setString("statusCode", StatusCode.ACTIVE.getValue());
					classInformationMapper.registerClassInformation(classInfo);
				}
				
				MMultiData studentListOldClass = classInformationMapper.retrieveClassInformationStudentList(oldClassInfo);
				if(studentListOldClass.size() > 0) {
					MMultiData studentListNewClass = new MMultiData();
					for(MData oldStudentInfo : studentListOldClass.toListMData()) {
						oldStudentInfo.setString("classYear", param.getString("newYear"));
						oldStudentInfo.setString("semester", param.getString("newSemester"));
						studentListNewClass.addMData(oldStudentInfo);
					}
					
					outputData.setString("classID", param.getString("classID"));
					outputData.setString("classYear", param.getString("newYear"));
					outputData.setString("semester", param.getString("newSemester"));
					outputData.setMMultiData("studentList", studentListNewClass);
					
					newClassInfo.setString("classInfoID", classInfoID);
					newClassInfo.setMMultiData("studentList", studentListNewClass);
					this.registerStudentToClassInformation(newClassInfo, true);
				}
			}
			
		} catch (MNotFoundException e) {
			throw new MException(ErrorCode.CLASS_NOT_FOUND.getValue(), ErrorCode.CLASS_NOT_FOUND.getDescription());
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
		return outputData;
	}
	
	private boolean checkIsValidClassUpdgrade(MData param) {
		String oldYear = param.getString("oldYear");
		String oldSemester = param.getString("oldSemester");
		String newYear = param.getString("newYear");
		String newSemester = param.getString("newSemester");
		if(oldYear.equals(newYear)) {
			if(oldSemester.equals(newSemester)) {
				return false;
			}else {
				return true;
			}
		}
		return true;
	}
	
	private boolean checkNewClassExist(MData newClassInfo) {
		try {
			classInformationMapper.retrieveClassInformationDetail(newClassInfo);
			return true;
		} catch (MNotFoundException e) {
			return false;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

}
