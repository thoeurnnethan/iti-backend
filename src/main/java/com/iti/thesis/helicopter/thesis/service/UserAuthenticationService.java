package com.iti.thesis.helicopter.thesis.service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface UserAuthenticationService {
	
	public MData userLogin(MData param) throws MException;

	public MData userLogout(MData param) throws MException;

}
