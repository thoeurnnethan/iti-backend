package com.iti.thesis.helicopter.thesis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.core.exception.MNotFoundException;
import com.iti.thesis.helicopter.thesis.db.service.NewsEventMapper;
import com.iti.thesis.helicopter.thesis.service.NewsEventService;
import com.iti.thesis.helicopter.thesis.util.MValidatorUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NewsEventServiceImpl implements NewsEventService{
	
	@Autowired
	private NewsEventMapper			newsEventMapper;
	
	@Override
	public MMultiData retrieveNewsEventList(MData param) throws MException {
		MMultiData outputData = new MMultiData();
		try {
			outputData = newsEventMapper.retrieveNewsEventList(param);
		} catch (MException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (Exception e){
			throw e;
		}
		return outputData;
	}

	@Override
	public MData retrieveNewsEventTotalCount(MData param) throws MException {
		try {
			return newsEventMapper.retrieveNewsEventTotalCount(param);
		} catch (MException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (Exception e){
			throw e;
		}
	}
	
	@Override
	public MData registerNewsEvent(MData param) {
		try {
			int lastNewsEventID = this.setLastNewsEventID(param);
			param.setInt("newsEventID", lastNewsEventID);
			newsEventMapper.registerNewsEvent(param);
			MData result = param;
			return result;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}
	
	private int setLastNewsEventID(MData param) {
		int result = 0;
		try {
			MData lastStudentID	= newsEventMapper.retrieveLastNewsEventID(param);
			result				= lastStudentID.getInt("lastNewsEventID");
			result++;
		} catch (MNotFoundException e) {
			result = 101;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
		return result;
	}

	@Override
	public MData retrieveNewsEventDetail(MData param) throws MException {
		MData outputData = new MData();
		try {
			outputData = newsEventMapper.retrieveNewsEventDetail(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
		return outputData;
	}

	@Override
	public MData updateNewsEvent(MData param) throws MException {
		try {
			MValidatorUtil.validate(param, "newsEventID");
			MData newsEventInfo = this.retrieveNewsEventDetail(param);
			if(!newsEventInfo.isEmpty()) {
				newsEventMapper.updateNewsEvent(param);
			}
			return param;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	@Override
	public MData retrieveLastNewsEventID(MData param) throws MException {
		// TODO Auto-generated method stub
		return null;
	}

}
