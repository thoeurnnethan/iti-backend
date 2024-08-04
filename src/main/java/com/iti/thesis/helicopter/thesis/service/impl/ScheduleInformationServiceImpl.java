package com.iti.thesis.helicopter.thesis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.db.service.ScheduleInformationMapper;
import com.iti.thesis.helicopter.thesis.service.ScheduleInformationService;

@Service
public class ScheduleInformationServiceImpl implements ScheduleInformationService {
	
	@Autowired
	private ScheduleInformationMapper	scheduleInformationMapper;
	
	
	@Override
	public MMultiData retrieveScheduleInformationList(MData param) throws MException {
		try {
			return scheduleInformationMapper.retrieveScheduleInformationList(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

}
