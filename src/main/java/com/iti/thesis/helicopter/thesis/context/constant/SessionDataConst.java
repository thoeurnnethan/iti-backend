package com.iti.thesis.helicopter.thesis.context.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SessionDataConst {
	public static final String		SESSION_ID			= "id";
	public static final String		LAST_ACCESSED_TIME	= "lastAccessedTime";
	public static final String		TRANSACTION_ID		= "TRANSACTION_ID";
	public static final String[]	META_DATA_FIELD		= {"id","created_datetime","loginUserId", "userRoleId","requestUri"};
	
	public static List<String> getMetaDataField() {
		return new ArrayList<String>(Arrays.asList(META_DATA_FIELD));
	}
}
