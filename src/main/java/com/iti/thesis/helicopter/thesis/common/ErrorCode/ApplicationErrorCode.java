package com.iti.thesis.helicopter.thesis.common.ErrorCode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApplicationErrorCode {

	ROOM_NOT_FOUND("ER0001", "Room ID Information not found !"),
	CLASS_NOT_FOUND("ER0002", "Class ID Information not found !"),
	DEPARTMENT_NOT_FOUND("ER0003", "Department ID Information not found !"),
	SUBJECT_ALREADY_REGISTER("ER0004", "This class subject are already registered, Please try to update it.");
	
	private String value;
	private String description;
	
}
