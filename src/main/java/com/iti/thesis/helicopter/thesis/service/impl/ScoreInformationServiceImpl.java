package com.iti.thesis.helicopter.thesis.service.impl;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.common.ErrorCode.ErrorCode;
import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.core.exception.MNotFoundException;
import com.iti.thesis.helicopter.thesis.db.service.ScoreInformationMapper;
import com.iti.thesis.helicopter.thesis.db.service.StudentClassMappingMapper;
import com.iti.thesis.helicopter.thesis.service.ClassInformationService;
import com.iti.thesis.helicopter.thesis.service.ScoreInformationService;
import com.iti.thesis.helicopter.thesis.service.SubjectInformationService;
import com.iti.thesis.helicopter.thesis.util.MStringUtil;
import com.iti.thesis.helicopter.thesis.util.MValidatorUtil;

@Service
public class ScoreInformationServiceImpl implements ScoreInformationService {
	
	@Autowired
	private ScoreInformationMapper		scoreInformationMapper;
	@Autowired 
	private SubjectInformationService	subjectInformationService;
	@Autowired
	private ClassInformationService		classInformationService;
	@Autowired
	private StudentClassMappingMapper	studentClassMappingMapper;
	
	@Override
	public MData retrieveStudentScoreList(MData param) throws MException {
		MData outputData = new MData();
		try {
			MValidatorUtil.validate(param, "classInfoID");
			MMultiData studentScoreList = scoreInformationMapper.retrieveStudentScoreList(param);
			if(studentScoreList.size() > 0) {
				MMultiData subjectList = subjectInformationService.retrieveSubjectInformationList(param);
				MMultiData keyValueSubject = subjectList.toListMData().stream()
						.map(data ->{
							MData newData = new MData();
							newData.setString("subjectID", data.getString("subjectID"));
							newData.setString("subjectName", data.getString("subjectName"));
							return newData;
						}).collect(Collectors.toCollection(MMultiData::new));
				MMultiData studentScore = this.transformScoreList(studentScoreList, keyValueSubject);
				outputData.put("subjects", keyValueSubject);
				outputData.setInt("totalCount", studentScore.size());
				outputData.setMMultiData("data", studentScore);
			}
			return outputData;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}
	
	public MMultiData transformScoreList(MMultiData scoreList, MMultiData keyValueSubject) {
		// Group subjectScoreList by studentID
		Map<String, MMultiData> groupedByStudent = scoreList.stream()
				.collect(Collectors.groupingBy(subjectScore -> (String) subjectScore.getOrDefault("studentID", ""), Collectors.toCollection(MMultiData::new)));
		
		// Transform the grouped data into the desired format
		MMultiData transformedList = new MMultiData();
		for (Map.Entry<String, MMultiData> entry : groupedByStudent.entrySet()) {
			MData studentData = new MData();
			MMultiData subjectScoreList = entry.getValue();
			if (!subjectScoreList.isEmpty()) {
				MData firstScore = subjectScoreList.getMData(0);
				studentData.setString("studentID", firstScore.getString("studentID"));
				studentData.setString("firstName", firstScore.getString("firstName"));
				studentData.setString("lastName", firstScore.getString("lastName"));
				studentData.setString("gender", firstScore.getString("gender"));
				studentData.setString("phoneNumber", firstScore.getString("phoneNumber"));
				
				// Initialize all subjects with a default score of 0
				for (MData subject : keyValueSubject.toListMData()) {
					studentData.setDouble(subject.getString("subjectName"), 0.00D);
				}
				
				// Subject subjectScoreList
				Double totalScore = Double.valueOf(0D);
				for (MData subjectScore : subjectScoreList.toListMData()) {
					String	subjectName		= subjectScore.getString("subjectName");
					Double	scoreOfSubject	= subjectScore.getDouble("score");
					if(!MStringUtil.isEmpty(subjectName)) {
						scoreOfSubject = MStringUtil.isEmpty(scoreOfSubject+MStringUtil.EMPTY) ? 0.00D : scoreOfSubject;
						totalScore += scoreOfSubject;
						studentData.setDouble(subjectName, scoreOfSubject);
					}
				}
				studentData.setDouble("totalScore", totalScore);
				studentData.setDouble("average", totalScore / (subjectScoreList.size()));
			}
			transformedList.add(studentData);
		}
		
		MMultiData finalData = transformedList.toListMData().stream()
				.sorted(Comparator.comparingDouble((MData map) -> map.getDouble("average")).reversed())
				.collect(Collectors.toCollection(MMultiData::new));
		// Assign grades
		for (int i = 0; i < finalData.size(); i++) {
			finalData.get(i).put("grade", i + 1);
		}
		return finalData;
	}
	
	@Override
	public MData registerStudentScore(MData param) throws MException {
		MData outputData = new MData();
		try {
			MValidatorUtil.validate(param, "classID","classYear","semester","studentScoreList");
			MData classInfo = new MData();
			classInfo.setString("classID", param.getString("classID"));
			classInfo.setString("cyear", param.getString("classYear"));
			classInfo.setString("semester", param.getString("semester"));
			classInfo = classInformationService.retrieveClassInformationDetail(classInfo);
			if(!classInfo.isEmpty()) {
				MMultiData validatedStudentScoreList = new MMultiData();
				MMultiData studentScoreList = param.getMMultiData("studentScoreList");
				for(MData studentScore : studentScoreList.toListMData()) {
					MValidatorUtil.validate(studentScore, "studentID","subjectID", "score");
					
					float score = studentScore.getFloat("score");
					studentScore.setFloat("score", score);
					studentScore.setString("classInfoID", classInfo.getString("classInfoID"));
					boolean isValid = this.isStudentExistInClass(studentScore);
					if(!isValid) {
						throw new MException(ErrorCode.STUDENT_NOT_IN_CLASS.getValue(), ErrorCode.STUDENT_NOT_IN_CLASS.getDescription());
					}else {
						boolean isScoreValid = this.isStudentScoreExist(studentScore);
						if(isScoreValid) {
							studentScore.setFloat("score", score);
							scoreInformationMapper.updateScoreInformation(studentScore);
						}else {
							scoreInformationMapper.registerScoreInformation(studentScore);
						}
						validatedStudentScoreList.addMData(studentScore);
					}
				}
				outputData.setMMultiData("studentScoreList", validatedStudentScoreList);
			}
			param.setString("classInfoID", param.getString("classID") + param.getString("classYear") + param.getString("semester"));
			return this.retrieveStudentScoreList(param);
		} catch (MNotFoundException e) {
			throw new MException(ErrorCode.CLASS_NOT_FOUND.getValue(), ErrorCode.CLASS_NOT_FOUND.getDescription());
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}
	
	private boolean isStudentScoreExist(MData data) {
		try {
			MData resData = scoreInformationMapper.retrieveScoreInformation(data);
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
	
	private boolean isStudentExistInClass(MData data) {
		try {
			MData resData = studentClassMappingMapper.retrieveStudentClassMappingInfo(data);
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

}
