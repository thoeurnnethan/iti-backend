package com.iti.thesis.helicopter.thesis.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum StatusCode {

	ACTIVE("01", "Active"),
	DELETE("02", "Delete"),
	INACTIVE("09", "InActive");
	
	private String value;
	private String description;
	
}
