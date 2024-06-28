package com.iti.thesis.helicopter.thesis.core.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TYPE {
		META,
		SERVER,
		REQUEST,
		RESPONSE,
		RESULT;
	
	TYPE() {}

	private String code;
	
}
