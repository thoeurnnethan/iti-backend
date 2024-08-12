package com.iti.thesis.helicopter.thesis.service.impl;

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
import com.iti.thesis.helicopter.thesis.util.MStringUtil;

@Service
public class ScoreInformationServiceImpl implements ScoreInformationService {
	
	@Autowired
	private ScoreInformationMapper	scoreInformationMapper;
	
	@Override
	public MData retrieveStudentScoreList(MData param) throws MException {
		try {
			MMultiData studentScoreList = scoreInformationMapper.retrieveStudentScoreList(param);
			MData news = this.transformScoreList(studentScoreList);
			return news;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}
	
	public MData transformScoreList(MMultiData scoreList) {
		// Group subjectScoreList by studentID
		Map<String, MMultiData> groupedByStudent = scoreList.stream()
				.collect(Collectors.groupingBy(subjectScore -> (String) subjectScore.getOrDefault("studentID", ""), Collectors.toCollection(MMultiData::new)));
		
		// Get all unique subjects
		Set<String> uniqueSubjects = scoreList.stream()
				.filter(subjectScore -> !MStringUtil.isEmpty((String) subjectScore.get("subjectName")))
				.map(subjectScore -> (String) subjectScore.get("subjectName"))
				.collect(Collectors.toSet());
		
		// Transform the grouped data into the desired format
		MMultiData transformedList = new MMultiData();
		for (Map.Entry<String, MMultiData> entry : groupedByStudent.entrySet()) {
			MData studentData = new MData();
			MMultiData subjectScoreList = entry.getValue();
			if (!subjectScoreList.isEmpty()) {
				MData firstScore = subjectScoreList.getMData(0);
				studentData.setString("studentName", firstScore.getString("firstName") + " " + firstScore.getString("lastName"));
				studentData.setString("studentID", firstScore.getString("studentID"));
				studentData.setString("gender", firstScore.getString("gender"));
				studentData.setString("phoneNumber", firstScore.getString("phoneNumber"));
				
				// Initialize all subjects with a default score of 0
				for (String subject : uniqueSubjects) {
					
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
		MData response = new MData();
		response.setInt("totalCount", transformedList.size());
		response.setMMultiData("data", transformedList);
		response.put("subjects", uniqueSubjects);
		return response;
	}

}
