package com.iti.thesis.helicopter.thesis.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserRoleCode {

	ADMIN("01", "Admin"),
	DEP_MANAGER("02", "Department Manager"),
	TEACHER("03", "Teacher"),
	STUDENT("04", "Student");
	
	private String value;
	private String description;
	
}
