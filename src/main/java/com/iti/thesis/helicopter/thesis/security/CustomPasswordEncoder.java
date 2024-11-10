package com.iti.thesis.helicopter.thesis.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.iti.thesis.helicopter.thesis.util.MPasswordUtil;

public class CustomPasswordEncoder implements PasswordEncoder {

	private String identifier;
	private long maxPassLen;

	public CustomPasswordEncoder(String id, long maxPassLen) {
		this.identifier = id;
		this.maxPassLen = maxPassLen;
	}
	
	@Override
	public String encode(CharSequence rawPassword) {
		String encodedPassword = MPasswordUtil.oneWayEnc(rawPassword.toString(), identifier, maxPassLen);
		return encodedPassword;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		String encryptedPassword = MPasswordUtil.oneWayEnc(rawPassword.toString(), identifier, maxPassLen);
		boolean isMatch = encodedPassword.equals(encryptedPassword);
		return isMatch;
	}
}
