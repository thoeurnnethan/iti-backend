package com.iti.thesis.helicopter.thesis.context.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListAllowUrlConst {
	public static final String[]	ALLOW_URL		= {"/public/api/user/login"};
	
	public static List<String> getListAllowUrl() {
		return new ArrayList<String>(Arrays.asList(ALLOW_URL));
	}
}
