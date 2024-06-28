package com.iti.thesis.helicopter.thesis.util;

import org.springframework.util.StringUtils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtil extends StringUtils{
	
	public static String trim(final String str){
		return str == null ? null : str.trim();
	}
	
	public static boolean isEmpty(final String str){
		return (str == null || str.length() == 0) ? true : false;
	}
	
	public static boolean isNotBlank(final CharSequence cs) {
		return !isBlank(cs);
	}
	
	public static boolean isBlank(final CharSequence cs) {
		final int strLen = length(cs);
		if (strLen == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(cs.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	public static int length(final CharSequence cs) {
		return cs == null ? 0 : cs.length();
	}

}
