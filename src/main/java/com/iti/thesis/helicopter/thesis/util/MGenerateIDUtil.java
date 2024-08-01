package com.iti.thesis.helicopter.thesis.util;

import java.util.Random;
import java.util.UUID;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MGenerateIDUtil {
	
	private final String ID_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private final String PASSWORD_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*";
	private final int ID_LENGTH = 10;
	private final int PASSWORD_LENGTH = 12;
	
	public String generateGUID() {
		String uuid = getUUID();
		return uuid.toUpperCase().replace("-", "");
	}
	
	public String getUUID() {
		return UUID.randomUUID().toString();
	}
	
	public String generateUserID() {
		Random			random			= new Random();
		StringBuilder	userIDBuilder	= new StringBuilder(ID_LENGTH);
		for (int i = 0; i < ID_LENGTH; i++) {
			userIDBuilder.append(ID_CHARACTERS.charAt(random.nextInt(ID_CHARACTERS.length())));
		}
		return userIDBuilder.toString();
	}
	
	public String generateUserPassword() {
		Random			random			= new Random();
		StringBuilder	userIDBuilder	= new StringBuilder(PASSWORD_LENGTH);
		for (int i = 0; i < PASSWORD_LENGTH; i++) {
			userIDBuilder.append(PASSWORD_CHARACTERS.charAt(random.nextInt(PASSWORD_CHARACTERS.length())));
		}
		return userIDBuilder.toString();
	}

}
