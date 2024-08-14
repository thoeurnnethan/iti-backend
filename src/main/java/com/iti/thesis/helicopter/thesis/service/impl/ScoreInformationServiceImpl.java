package com.iti.thesis.helicopter.thesis.service.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.db.service.ScoreInformationMapper;
import com.iti.thesis.helicopter.thesis.service.ScoreInformationService;
import com.iti.thesis.helicopter.thesis.service.SubjectInformationService;
import com.iti.thesis.helicopter.thesis.util.MStringUtil;
import com.iti.thesis.helicopter.thesis.util.MValidatorUtil;

@Service
public class ScoreInformationServiceImpl implements ScoreInformationService {
	
	@Autowired
	private ScoreInformationMapper	scoreInformationMapper;
	@Autowired 
	private SubjectInformationService subjectInformationService;
	
	@Override
	public MData retrieveStudentScoreList(MData param) throws MException {
		MData outputData = new MData();
		try {
			MValidatorUtil.validate(param, "classInfoID");
			MMultiData studentScoreList = scoreInformationMapper.retrieveStudentScoreList(param);
			if(studentScoreList.size() > 0) {
				MMultiData subjectList = subjectInformationService.retrieveSubjectInformationList(param);
				Set<String> subjectNameList = new HashSet<>();
				MMultiData studentScore = new MMultiData();
				if(subjectList.size() > 0) {
					subjectNameList = subjectList.stream()
						.filter(subject -> !MStringUtil.isEmpty((String) subject.get("subjectName")))
						.map(subject -> (String) subject.get("subjectName"))
						.collect(Collectors.toSet());
					studentScore = this.transformScoreList(studentScoreList, subjectNameList);
				}
				outputData.put("subjects", subjectNameList);
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
	
	public MMultiData transformScoreList(MMultiData scoreList, Set<String> subjectNameList) {
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
				for (String subject : subjectNameList) {
					studentData.setInt(subject, 0);
				}
				
				// Subject subjectScoreList
				for (MData subjectScore : subjectScoreList.toListMData()) {
					String	subjectName		= subjectScore.getString("subjectName");
					int		scoreOfSubject	= subjectScore.getInt("score");
					if(!MStringUtil.isEmpty(subjectName)) {
						studentData.setInt(subjectName, MStringUtil.isEmpty(scoreOfSubject+MStringUtil.EMPTY) ? 0 : scoreOfSubject);
					}
				}
			}
			transformedList.add(studentData);
		}
		return transformedList;
	}

}
