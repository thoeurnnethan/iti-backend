package com.iti.thesis.helicopter.thesis.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ConstantCodePrefix {

	// Value must be 3 digits
	DEPARTMENT("DEP", "Department"),
	CLASS("CLS", "Class"),
	ROOM("ROM", "Room"),
	ADMIN("ADM", "Admin"),
	DEPARTENT_MGT("DMG", "Admin"),
	TEACHER("TCH", "Admin"),
	STUDNET("STD", "Student");
	
	private String value;
	private String description;
	
}
