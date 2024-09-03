package com.iti.thesis.helicopter.thesis.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserStatusCode {

	ACTIVE("01", "Active"),
	DELETE("02", "Delete"),
	LOCK("03", "Lock"),
	INACTIVE("09", "Inactive");
	
	private String value;
	private String description;
	
}
