package com.iti.thesis.helicopter.thesis.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DaysOfWeek {

	MONDAY		("Monday"			, "Monday"),
	TUESDAY		("Tuesday"			, "Tuesday"),
	WEDNESDAY	("Wednesday"		, "Wednesday"),
	THURSDAY	("Thursday"			, "Thursday"),
	FRIDAY		("Friday"			, "Friday"),
	SATURDAY	("Saturday"			, "Saturday"),
	SUNDAY		("Sunday"			, "Delete");
	
	private String value;
	private String description;
	
}