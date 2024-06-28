package com.iti.thesis.helicopter.thesis.util;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public class MSha512Util {
	private static final String	ALGORITHM_SHA512	= "SHA-512";
	private final static int	RADIX				= 16;
	
	public static String encrypt(String plainText) throws MException {
		byte[]			passwordBytes	= null;
		MessageDigest	sha512			= null;
		byte[]			resultBytes		= null;
		StringBuilder	resultBuffer	= new StringBuilder();
		String			result			= null;
		
		try {
			passwordBytes	= plainText.getBytes(StandardCharsets.UTF_8);
			sha512			= MessageDigest.getInstance(ALGORITHM_SHA512);
			sha512.reset();
			sha512.update(passwordBytes);
			resultBytes = sha512.digest();
			
			for (int i = 0; i < resultBytes.length; i++ ) {
				resultBuffer.append(Integer.toString((resultBytes[i] & 0xf0) >> 4, RADIX));
				resultBuffer.append(Integer.toString(resultBytes[i] & 0x0f, RADIX));
			}
			
			result = resultBuffer.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new MBizException("encrypt( String plainText, String secretKey )");
		} catch (Exception e) {
			throw new MBizException("encrypt( String plainText, String secretKey )");
		}
		
		return result;
	}
	
	public static String encrypt(String plainText, String secretKey) throws MException {
		byte[]			passwordBytes	= null;
		MessageDigest	sha512			= null;
		byte[]			resultBytes		= null;
		StringBuilder	resultBuffer	= new StringBuilder();
		String			result			= null;
		
		try {
			passwordBytes	= plainText.getBytes(StandardCharsets.UTF_8);
			sha512			= MessageDigest.getInstance(ALGORITHM_SHA512);
			sha512.reset();
			sha512.update(passwordBytes);
			resultBytes = sha512.digest(secretKey.getBytes(StandardCharsets.UTF_8));
			
			for (int i = 0; i < resultBytes.length; i++ ) {
				resultBuffer.append(Integer.toString((resultBytes[i] & 0xf0) >> 4, RADIX));
				resultBuffer.append(Integer.toString(resultBytes[i] & 0x0f, RADIX));
			}
			
			result = resultBuffer.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new MBizException("encrypt( String plainText, String secretKey )");
		} catch (Exception e) {
			throw new MBizException("encrypt( String plainText, String secretKey )");
		}
		
		return result;
	}
}
