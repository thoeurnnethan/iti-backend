package com.iti.thesis.helicopter.thesis.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserStatusCode {

	ACTIVE("01", "Active"),
	LOCK("02", "Lock"),
	INACTIVE("09", "InActive");
	
	private String value;
	private String description;
	
}
