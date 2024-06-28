package com.iti.thesis.helicopter.thesis.util;

import com.iti.thesis.helicopter.thesis.core.exception.MBizException;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MPasswordUtil {
	public final static String		TEXT_TYPE_PLAIN		= "0";
	public final static String		TEXT_TYPE_ONE_WAY	= "1";
	public final static String		TEXT_TYPE_TWO_WAY	= "2";
	private final static Boolean	CRYPT_PASSWD_USE	= Boolean.TRUE;
	
	public static String oneWayEnc(String passwd, String identifier, long maxPassLen) throws MBizException {
		String result = passwd;
		
		try {
			boolean use = CRYPT_PASSWD_USE;
			
			if (use) {
				result = MSha512Util.encrypt(result, identifier);
			}
			
		} catch (Exception e) {
			throw new MBizException("oneWayEnc( String passwd, String identifier, long maxPassLen )");
		}
		
		return result;
	}
	public static String oneWayEnc(String passwd, String identifier) throws MBizException {
		String result = passwd;
		
		try {
			boolean use = CRYPT_PASSWD_USE;
			
			if (use) {
				result = MSha512Util.encrypt(result);
			}
			
		} catch (Exception e) {
			throw new MBizException("oneWayEnc( String passwd, String identifier )");
		}
		
		return result;
	}
}
