package com.iti.thesis.helicopter.thesis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.common.ErrorCode.ApplicationErrorCode;
import com.iti.thesis.helicopter.thesis.constant.ConstantCodePrefix;
import com.iti.thesis.helicopter.thesis.constant.StatusCode;
import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.core.exception.MNotFoundException;
import com.iti.thesis.helicopter.thesis.db.service.RoomInformationMapper;
import com.iti.thesis.helicopter.thesis.service.RoomInformationService;
import com.iti.thesis.helicopter.thesis.util.MStringUtil;

@Service
public class RoomInformationServiceImpl implements RoomInformationService {
	
	@Autowired
	private RoomInformationMapper		roomInformationMapper;
	
	@Override
	public MData registerRoomInformation(MData param) {
		MData	outputData		= param;
		try {
			String roomID = this.retrieveLastRoomID(param);
			param.setString("roomID", roomID);
			param.setString("statusCode", StatusCode.ACTIVE.getValue());
			roomInformationMapper.registerRoomInformation(param);
			return outputData;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}
	
	private String retrieveLastRoomID(MData param) {
		int result = 0 ;
		try {
			MData	roomInfo	= roomInformationMapper.retrieveLastRoomID(param);
			String	resultStr	= roomInfo.getString("roomID");
			resultStr	= resultStr.substring(ConstantCodePrefix.ROOM.getValue().length(), resultStr.length());
			result		= Integer.valueOf(resultStr);
			result ++;
		} catch (MNotFoundException e) {
			result = 1001;
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
		return ConstantCodePrefix.ROOM.getValue() + String.valueOf(String.format("%04d", result));
	}

	@Override
	public MData updateRoomInformation(MData param) throws MException {
		try {
			
			MData roomInfo = roomInformationMapper.retrieveRoomInformationDetail(param);
			if(!MStringUtil.isEmpty(roomInfo)) {
				roomInformationMapper.updateRoomInformation(param);
			}
			return param;
		} catch (MNotFoundException e) {
			throw new MException(ApplicationErrorCode.ROOM_NOT_FOUND.getValue(), ApplicationErrorCode.ROOM_NOT_FOUND.getDescription());
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	@Override
	public MMultiData retrieveRoomInformationList(MData param) throws MException {
		try {
			return roomInformationMapper.retrieveRoomInformationList(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	@Override
	public MData retrieveRoomInformationTotalCount(MData param) throws MException {
		try {
			return roomInformationMapper.retrieveRoomInformationTotalCount(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}

	@Override
	public MMultiData retrieveRoomInformationListForDownload(MData param) throws MException {
		try {
			return roomInformationMapper.retrieveRoomInformationListForDownload(param);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription(), e);
		}
	}
}
