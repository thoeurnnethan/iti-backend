package com.iti.thesis.helicopter.thesis.common.ErrorCode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

	UNCAUGHT_ERROR("001", "Uncaught Error"),
	INCORRECT_PASSWORD("002", "Incorrect Password"),
	USER_LOCK("003", "This user is locked");
	
	private String value;
	private String description;
	
}
