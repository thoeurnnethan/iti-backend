package com.iti.thesis.helicopter.thesis.service.impl;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.common.ErrorCode.ErrorCode;
import com.iti.thesis.helicopter.thesis.constant.YnTypeCode;
import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.core.exception.MNotFoundException;
import com.iti.thesis.helicopter.thesis.db.service.ScheduleDetailMapper;
import com.iti.thesis.helicopter.thesis.db.service.ScheduleInformationMapper;
import com.iti.thesis.helicopter.thesis.service.ClassInformationService;
import com.iti.thesis.helicopter.thesis.service.ScheduleInformationService;
import com.iti.thesis.helicopter.thesis.util.MStringUtil;
import com.iti.thesis.helicopter.thesis.util.MValidatorUtil;

@Service
public class ScheduleInformationServiceImpl implements ScheduleInformationService {
	
	@Autowired
	private ScheduleInformationMapper	scheduleInformationMapper;
	@Autowired
	private ScheduleDetailMapper		scheduleDetailMapper;
	@Autowired
	private ClassInformationService		classInformationService;
	
	
	@Override
	public MMultiData retrieveScheduleInformationList(MData param) throws MException {
		try {
			MMultiData prepareResponse = scheduleInformationMapper.retrieveScheduleInformationList(param);
			if(!MStringUtil.isEmpty(param.getString("departmentID")) && !MStringUtil.isEmpty(param.getString("classID"))) {
				MData prepare = this.prepareScheduleByDepartmentAndClass(prepareResponse);
				prepareResponse = prepare.getMMultiData("tableList");
			}
			return prepareResponse;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}
	
	private MData prepareScheduleByDepartmentAndClass(MMultiData scheduleList) {
		MMultiData			formattedScheduleList		= new MMultiData();
		Map<Integer, MData>	groupedSchedules			= new LinkedHashMap<>();
		for(MData schedule : scheduleList.toListMData()) {
			int		seqNo	= schedule.getInt("seqNo");
			String	day		= schedule.getString("scheduleDay");
			if(!groupedSchedules.containsKey(seqNo)) {
				MData weekSchedule = new MData();
				groupedSchedules.put(seqNo, weekSchedule);
			}
			MData dayMap = groupedSchedules.get(seqNo);
			dayMap.setMData(day, this.prepareDataByDay(schedule));
		}
		for (MData weekSchedule : groupedSchedules.values()) {
			MData fullWeekSchedule = new MData();
			for (String day : Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")) {
				if (!weekSchedule.containsKey(day)) {
					weekSchedule.setMData(day, new MData());
				}
			}
			fullWeekSchedule.putAll(weekSchedule);
			formattedScheduleList.add(fullWeekSchedule);
		}
		MData result = new MData();
		result.setMMultiData("tableList", formattedScheduleList);
		return result;
	}
	
	private MData prepareDataByDay(MData scheduleInfo) {
		MData tableInfo = new MData();
		tableInfo.setString("scheduleDay", scheduleInfo.getString("scheduleDay"));
		tableInfo.setInt("seqNo", scheduleInfo.getInt("seqNo"));
		tableInfo.setString("teacherID", scheduleInfo.getString("teacherID"));
		tableInfo.setString("firstName", scheduleInfo.getString("firstName"));
		tableInfo.setString("lastName", scheduleInfo.getString("lastName"));
		tableInfo.setString("phoneNumber", scheduleInfo.getString("phoneNumber"));
		tableInfo.setString("subjectID", scheduleInfo.getString("subjectID"));
		tableInfo.setString("subjectName", scheduleInfo.getString("subjectName"));
		tableInfo.setString("roomID", scheduleInfo.getString("roomID"));
		tableInfo.setString("roomName", scheduleInfo.getString("roomName"));
		tableInfo.setString("building", scheduleInfo.getString("building"));
		tableInfo.setString("floor", scheduleInfo.getString("floor"));
		tableInfo.setString("startTime", scheduleInfo.getString("startTime"));
		tableInfo.setString("endTime", scheduleInfo.getString("endTime"));
		return tableInfo;
	}

	@Override
	public MData registerScheduleInformation(MData param) throws MException {
		try {
			// Retrieve Class Info
			MValidatorUtil.validate(param, "schYear", "classID", "cyear", "semester");
			MData classInfo = classInformationService.retrieveClassInformationDetail(param);
			
			MData validateDate = this.validateAndPrepareScheduleData(param);
			
			scheduleInformationMapper.registerScheduleInformation(validateDate);
			
			for(MData data : validateDate.getMMultiData("scheduleList").toListMData()) {
				data.setString("scheduleDay", data.getString("schDay"));
				scheduleDetailMapper.registerScheduleDetail(data);
			}
			return classInfo;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}
	
	private MData validateAndPrepareScheduleData(MData param) {
		try {
			// Retrieve Class Info
			MValidatorUtil.validate(param, "schYear", "classID", "cyear", "semester");
			MData	classInfo			= classInformationService.retrieveClassInformationDetail(param);
			String	scheduleYear		= param.getString("schYear");
			String	classInfoID			= classInfo.getString("classInfoID");
			String	schduleID			= scheduleYear.concat(classInfoID);
			
			MData	scheduleInfoParam	= new MData();
			scheduleInfoParam.setString("schYear", param.getString("schYear"));
			scheduleInfoParam.setString("classInfoID", classInfoID);
			scheduleInfoParam.setString("scheduleID", schduleID);
			
			MMultiData validateScheduleList = new MMultiData();
			MMultiData scheduleList = param.getMMultiData("scheduleList");
			for(MData scheduleInfo : scheduleList.toListMData()) {
				MValidatorUtil.validate(scheduleInfo, "schDay","seqNo","teacherID","subjectID","roomID","startTime","endTime");
				
				scheduleInfo.setString("scheduleYear", scheduleYear);
				scheduleInfo.setString("scheduleID", schduleID);
				scheduleInfo.setString("cyear", param.getString("cyear"));
				scheduleInfo.setString("semester", param.getString("semester"));
				
				// Validate Duplicate Times
				boolean isDuplicateTime = this.checkDuplicateTime(scheduleInfo);
				if(isDuplicateTime) {
					scheduleInfo.setString("duplicateTimeYn", YnTypeCode.YES.getValue());
				}else {
					scheduleInfo.setString("duplicateTimeYn", YnTypeCode.NO.getValue());
				}
				// Validate Duplicate Teacher
				boolean isDuplicateTeacher = this.checkDuplicateTeacher(scheduleInfo);
				if(isDuplicateTeacher) {
					scheduleInfo.setString("duplicateTeacherYn", YnTypeCode.YES.getValue());
				}else {
					scheduleInfo.setString("duplicateTeacherYn", YnTypeCode.NO.getValue());
				}
				
				validateScheduleList.addMData(scheduleInfo);
			}
			scheduleInfoParam.setMMultiData("scheduleList", validateScheduleList);
			return scheduleInfoParam;
		} catch (MNotFoundException e) {
			throw new MException(ErrorCode.CLASS_NOT_FOUND.getValue(), ErrorCode.CLASS_NOT_FOUND.getDescription());
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}
	
	private boolean checkDuplicateTeacher(MData teacherInfo) {
		try {
			MValidatorUtil.validate(teacherInfo, "scheduleYear","cyear","semester","teacherID");
			MData	checkParam	= new MData();
			boolean	duplicate	= false;
			checkParam.setString("scheduleYear", teacherInfo.getString("scheduleYear"));
			checkParam.setString("classYear", teacherInfo.getString("cyear"));
			checkParam.setString("semester", teacherInfo.getString("semester"));
			checkParam.setString("teacherID", teacherInfo.getString("teacherID"));
			checkParam.setString("departmentID", MStringUtil.EMPTY);
			
			MMultiData	teacherList	= scheduleDetailMapper.selectCheckDuplicateTeacher(checkParam);
			if(teacherList.size() > 0) {
				for(MData teacher : teacherList.toListMData()) {
					if(teacherInfo.getString("teacherID").equals(teacher.getString("teacherID"))) {
						if(teacherInfo.getString("schDay").equals(teacher.getString("scheduleDay")))
							if(teacherInfo.getString("startTime").equals(teacher.getString("startTime")) || teacherInfo.getString("endTime").equals(teacher.getString("endTime"))) {
								duplicate = true;
						}
					}
					
				}
			}
			return duplicate;
		} catch (MNotFoundException e) {
			return false;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}	
	
	private boolean checkDuplicateTime(MData scheduleDetail) {
		try {
			MData	checkParam	= new MData();
			boolean	duplicate	= false;
			checkParam.setString("scheduleID", scheduleDetail.getString("scheduleID"));
			checkParam.setString("scheduleDay", scheduleDetail.getString("schDay"));
			
			String startTime	= scheduleDetail.getString("startTime");
			String endTime		= scheduleDetail.getString("endTime");
			MMultiData	dayList	= scheduleDetailMapper.selectCheckDuplicateTime(checkParam);
			
			for(MData day : dayList.toListMData()) {
				if(day.getString("startTime").equals(startTime) || day.getString("endTime").equals(endTime)) {
					duplicate = true;
				}
			}
			return duplicate;
		} catch (MNotFoundException e) {
			return false;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	@Override
	public MData validateScheduleInformation(MData param) throws MException {
		try {
			return this.validateAndPrepareScheduleData(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}
	

}