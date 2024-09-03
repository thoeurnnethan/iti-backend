package com.iti.thesis.helicopter.thesis.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DepartmentRoleCode {

	MANAGER("01", "Manager"),
	TEACHER("02", "Teacher");
	
	private String value;
	private String description;
	
}
