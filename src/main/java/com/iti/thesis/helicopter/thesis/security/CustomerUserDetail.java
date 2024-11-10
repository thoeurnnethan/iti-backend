package com.iti.thesis.helicopter.thesis.security;


import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.iti.thesis.helicopter.thesis.constant.StatusCode;
import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.core.exception.MNotFoundException;
import com.iti.thesis.helicopter.thesis.service.UserInfoService;

@Configuration
public class CustomerUserDetail implements UserDetailsService {
	
	@Autowired 
	private UserInfoService userInfoService;

	@Override
	public UserDetails loadUserByUsername(String username) {
		try {
			MData userInfo = new MData();
			userInfo.setString("userID", username);
			userInfo = userInfoService.retrieveUserInfoDetail(userInfo);
			
			boolean isEnable = StatusCode.ACTIVE.getValue().equals(userInfo.getString("statusCode")) ? true : false;
			
			return new org.springframework.security.core.userdetails.User(
					userInfo.getString("userID"),
					userInfo.getString("passwd"),
					isEnable, true, true, true,
					new ArrayList<>()
			);
			
		} catch (MNotFoundException e) {
			throw new MNotFoundException();
		} catch (MException e) {
			throw e;
		} catch (Exception e) {
			throw new MException();
		}
	}
}
