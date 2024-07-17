package com.iti.thesis.helicopter.thesis.common.ErrorCode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserErrorCode {

	USER_NOT_FOUND("USER001", "User Not Found !"),
	INCORRECT_PASSWORD("002", "Incorrect Password"),
	USER_LOCK("003", "This user is locked");
	
	private String value;
	private String description;
	
}
