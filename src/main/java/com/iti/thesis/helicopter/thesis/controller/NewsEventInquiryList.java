package com.iti.thesis.helicopter.thesis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.service.NewsEventService;;

@RestController
@RequestMapping("/api/news-event")
public class NewsEventInquiryList extends BaseTemplate {
	
	@Autowired
	private NewsEventService		newsEventService;
	
	@Override
	@PostMapping("/list")
	public JsonNode onRequest(@RequestBody MData message) throws MException {
		try {
			return super.onProcess(message);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription());
		}
	}
	
	@Override
	public MData onExecute(MData param) throws MException {
		try {
			MMultiData	newsEventList	= newsEventService.retrieveNewsEventList(param);
			MData		newsEventCount	= newsEventService.retrieveNewsEventTotalCount(param);
			return this.prepareResponse(newsEventList,newsEventCount);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription());
		}
	}
	
	private MData prepareResponse (MMultiData list, MData totalCount) {
		MData response = new MData();
		response.setInt("totalCount", totalCount.getInt("totalCount"));
		response.setMMultiData("newsEventList", list);
		return response;
	}

}
