package com.iti.thesis.helicopter.thesis.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ConstantCodePrefix {

	DEPARTMENT("DEP", "Department"),
	CLASS("CLS", "Class"),
	ROOM("RM", "Class"),
	USER("US", "Class");
	
	private String value;
	private String description;
	
}
