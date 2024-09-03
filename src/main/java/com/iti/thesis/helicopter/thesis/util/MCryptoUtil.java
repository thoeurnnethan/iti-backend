package com.iti.thesis.helicopter.thesis.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iti.thesis.helicopter.thesis.core.collection.MData;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class MCryptoUtil {

	private static final ObjectMapper objectMapper = new ObjectMapper();
	private static final String SECRET_KEY = "s3cr3tK3y1234567";
	
	private static final String ALGORITHM = "AES";
	private static final String TRANSFORMATION = "AES";

	public static String encrypt(String input) throws Exception {
		return doCrypto(Cipher.ENCRYPT_MODE, input, SECRET_KEY);
	}
	
	public static String encrypt(MData input) throws Exception {
		try {
			String jsonString = serialize(input);
			return doCrypto(Cipher.ENCRYPT_MODE, jsonString, SECRET_KEY);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
		}
		return MStringUtil.EMPTY;
	}
	
	public static MData decrypt(String input) throws Exception {
		try {			
			String decryptString = doCrypto(Cipher.DECRYPT_MODE, input, SECRET_KEY);
			return deserialize(decryptString, MData.class);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
		}
		return null;
	}
	
	private static String doCrypto(int cipherMode, String input, String key) throws Exception {
		SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), ALGORITHM);
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(cipherMode, secretKey);
		byte[] outputBytes;
		if (cipherMode == Cipher.ENCRYPT_MODE) {
			outputBytes = cipher.doFinal(input.getBytes("UTF-8"));
			return Base64.getEncoder().encodeToString(outputBytes);
		} else {
			byte[] inputBytes = Base64.getDecoder().decode(input);
			outputBytes = cipher.doFinal(inputBytes);
			return new String(outputBytes, "UTF-8");
		}
	}
	
	private static String serialize(MData data) throws Exception {
		return objectMapper.writeValueAsString(data);
	}

	private static <T> T deserialize(String jsonString, Class<T> clazz) throws Exception {
		return objectMapper.readValue(jsonString, clazz);
	}
}
