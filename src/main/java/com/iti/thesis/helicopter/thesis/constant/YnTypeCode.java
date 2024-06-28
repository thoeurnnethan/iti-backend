package com.iti.thesis.helicopter.thesis.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum YnTypeCode {

	YES("Y", "Yes"),
	NO("N", "No");
	
	private String value;
	private String description;
	
}
