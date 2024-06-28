package com.iti.thesis.helicopter.thesis.util;

import java.util.UUID;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MGUIDUtil {
	public String generateGUID() {
		String uuid = getUUID();
		return uuid.toUpperCase().replace("-", "");
	}
	
	public String getUUID() {
		return UUID.randomUUID().toString();
	}
}
