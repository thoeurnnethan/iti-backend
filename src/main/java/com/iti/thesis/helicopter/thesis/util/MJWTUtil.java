package com.iti.thesis.helicopter.thesis.util;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.function.Function;

import org.apache.tomcat.util.codec.binary.Base64;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * MJWTUtil
 * </PRE>
 */
@Slf4j
@UtilityClass
public class MJWTUtil {
	
	public String generateJWTSecretKey() {
		final char[]	list	= "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		SecureRandom	rnd		= new SecureRandom();
		StringBuffer	sb		= new StringBuffer();
		
		for (int i = 0; i < 32; i++ ) {
			int index = rnd.nextInt(62);
			sb.append(list[index]);
		}
		
		return Base64.encodeBase64String(sb.toString().getBytes());
	}
	
	public String createJWT(MData data, String secretKey, Long expiration ) {
		Claims	claims		= Jwts.claims();
		claims.putAll(data);
		return Jwts.builder()
				.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
				.setClaims(claims)
				.setSubject(data.getString("userID"))
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.setIssuedAt(new Date())
				.signWith(getSignInKey(secretKey), SignatureAlgorithm.HS256)
				.compact();
	}
	
	public boolean validateJWT(String jwtToken, String secretKey) throws MException {
		try {
			Jwts.parserBuilder()
				.setSigningKey(getSignInKey(secretKey))
				.build()
				.parseClaimsJws(jwtToken);
			return true;
		} catch (ExpiredJwtException e) {
			log.debug("Token expired: {}", e.getMessage());
			throw new MBizException(CommonErrorCode.INVALID_DATA.getCode(), "Token expired");
		} catch (MalformedJwtException | SecurityException e) {
			log.trace("Invalid JWT Token", e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.INVALID_DATA.getCode(), "Invalid JWT Token", e);
		} catch (UnsupportedJwtException e) {
			log.trace("Unsupported JWT Token", e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.INVALID_DATA.getCode(), "Unsupported JWT Token", e);
		} catch (IllegalArgumentException e) {
			log.trace("JWT claims string is empty.", e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.INVALID_DATA.getCode(), "JWT claims string is empty", e);
		} catch (Exception e) {
			log.trace("JWT validateToken unkown exception.", e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.INVALID_DATA.getCode(), "JWT validateToken unkown exception", e);
		}
		
	}
	
	public String extractPayload(String token,String secretKey) {
		@SuppressWarnings("unchecked")
		Jwt<?, Claims> jwt = Jwts.parserBuilder()
				.setSigningKey(getSignInKey(secretKey))
				.build()
				.parse(token);
		return (String) jwt.getBody().get("userID");
	}
	
	public String extractUserID(String token, String secretKey) {
		return extractClaim(token, Claims::getSubject, secretKey);
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver,String secretKey) {
		final Claims claims = extractAllClaims(token,secretKey);
		return claimsResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token, String secretKey) {
		return Jwts.parserBuilder()
				.setSigningKey(getSignInKey(secretKey))
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	private Key getSignInKey(String secretKey) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
}
