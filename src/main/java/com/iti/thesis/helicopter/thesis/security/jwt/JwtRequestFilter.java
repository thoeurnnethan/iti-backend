package com.iti.thesis.helicopter.thesis.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iti.thesis.helicopter.thesis.common.ErrorCode.ErrorCode;
import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.security.CustomerUserDetail;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	protected ObjectMapper objectMapper;

	@Autowired
	private CustomerUserDetail userDetailsService;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws IOException {
		try {
			final String authorizationHeader = request.getHeader("Authorization");
			String username = null;
			String jwt = null;
			
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				jwt = authorizationHeader.substring(7);
				username = jwtUtil.extractUsername(jwt);
			}
			
			// If username is present in the token and the SecurityContext is not already
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
				if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
					Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(auth);
				}
			}
			chain.doFilter(request, response);
		} catch (UsernameNotFoundException e) {
			handleError(response, HttpServletResponse.SC_UNAUTHORIZED, ErrorCode.USER_NOT_FOUND.getDescription());
			return;
		} catch (ExpiredJwtException e) {
			handleError(response, HttpServletResponse.SC_UNAUTHORIZED, ErrorCode.UNAUTHORIZE.getDescription());
			return;
		} catch (BadCredentialsException e) {
			handleError(response, HttpServletResponse.SC_UNAUTHORIZED, ErrorCode.BAD_CREDENTIALS.getDescription());
			return;
		} catch (JwtException e) {
			handleError(response, HttpServletResponse.SC_UNAUTHORIZED, ErrorCode.JWT_UNCAUGHT_ERROR.getDescription());
		} catch (Exception e) {
			handleError(response, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
			return;
		}
	}

	private void handleError(HttpServletResponse response, int code, String message) throws IOException {
		JsonNode jsonNode = this.makeFailResponse(null, code+"", message);
		response.setStatus(HttpServletResponse.SC_ACCEPTED);
		response.setContentType("application/json");
		response.getWriter().write(jsonNode.toString());
		response.getWriter().flush();
	}

	private final JsonNode makeFailResponse(MData obj, String errorCode, String errorText) {
		MData res = new MData();
		MData resHeader = new MData();
		if(obj != null) {
			obj.getMData("header");
		}
		resHeader.setString("error_code", errorCode);
		resHeader.setString("error_text", errorText);
		resHeader.setBoolean("result", false);
		res.setMData("header", resHeader);
		res.setMData("body", null);
		return toJsonNode(res);
	}

	private JsonNode toJsonNode(Object obj) {
		return objectMapper.valueToTree(obj);
	}
}
