package com.iti.thesis.helicopter.thesis.common.ErrorCode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApplicationErrorCode {

	ROOM_NOT_FOUNT("RM00001", "Room Information not found !");
	
	private String value;
	private String description;
	
}
