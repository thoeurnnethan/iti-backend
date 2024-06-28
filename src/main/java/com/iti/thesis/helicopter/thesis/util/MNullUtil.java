package com.iti.thesis.helicopter.thesis.util;

import java.util.List;
import java.util.Map;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MNullUtil {
	/**
	 * Check whether the input value is null.
	 * All DB Access methods use this when null checking is required.
	 */
	public boolean isNull(Object value) {
		return value == null;
	}
	
	/**
	 * Check whether the input value is null.
	 * The default is to return both "" and null as true.
	 * All DB Access methods use this when null checking is required.
	 */
	public boolean isNone(String value) {
		return value == null || value.length() == 0;
	}
	
	/**
	 * Returns true if value is null or size () = 0 otherwise false
	 */
	public boolean isNone(List<Object> value) {
		return (value == null || value.isEmpty());
	}
	
	/**
	 * Returns true if value is null or size () = 0 otherwise false
	 */
	public boolean isNone(Object[] value) {
		return (value == null || value.length == 0);
	}
	
	/**
	 * Returns true if value is null or size () = 0 otherwise false
	 * 
	 */
	public boolean isNone(Map<String, Object> value) {
		return (value == null || value.isEmpty());
	}
	
	public boolean notNone(String value) {
		return value != null && value.length() > 0;
	}
	
	/**
	 * If the value of the original String is null or "", it returns defaultStr
	 */
	public String NVL(String originalStr, String defaultStr) {
		if (originalStr == null || originalStr.length() < 1)
			return defaultStr;
		return originalStr;
	}
	
	/**
	 * <code>
	 * If object is null, or object.toString is null or "", defalutValue is returned
	 * , otherwise object.toString is returned.
	 * </code>
	 */
	public String NVL(Object object, String defaultValue) {
		if (object == null) {
			return defaultValue;
		}
		return NVL(object.toString(), defaultValue);
	}
	
}
