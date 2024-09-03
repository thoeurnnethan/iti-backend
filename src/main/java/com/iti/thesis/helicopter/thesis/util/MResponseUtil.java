package com.iti.thesis.helicopter.thesis.util;

import org.apache.commons.lang3.StringUtils;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MResponseUtil {
	
	public MData toMData(MData MData, String... param) {
		MData outputData = new MData();
		
		for (String key : param) {
			
			if (MData.containsKey(key)) {
				outputData.set(key, MData.getString(key));
			} else {
				outputData.set(key, StringUtils.EMPTY);
			}
			
		}
		
		return outputData;
	}
	
	public MData removeKey(MData MData, String... param) {
		MData outputData = MData;
		
		for (String key : param) {
			
			if (outputData.containsKey(key)) {
				outputData.remove(key);
			} else {
				outputData.set(key, StringUtils.EMPTY);
			}
			
		}
		
		return outputData;
	}
	
	public MMultiData removeKey(MMultiData MMultiData, String... param) {
		MMultiData outputData = new MMultiData();
		
		for(MData data : MMultiData.toListMData()) {
			for (String key : param) {
				
				if (data.containsKey(key)) {
					data.remove(key);
				} else {
					data.set(key, StringUtils.EMPTY);
				}
				
			}
			outputData.addMData(data);
		}
		
		return outputData;
	}

	public static MMultiData responseEmptyKey(MMultiData list, String ...keys) {
		MMultiData	resList		= new MMultiData();
		MData		resData		= new MData();
		for(MData data : list.toListMData() ) {
			for(String key : keys) {
				String value = data.getString(key);
				resData = data;
				resData.set(key, MStringUtil.defaultIfEmpty(value, MStringUtil.EMPTY));
			}
			resList.addMData(resData);
		}
		return resList;
	}
	
	public static MData responseEmptyKey(MData data, String... keys) {
		for (String key : keys) {
			String value = data.getString(key);
			data.set(key, MStringUtil.defaultIfEmpty(value, MStringUtil.EMPTY));
		}
		return data;
	}
}
