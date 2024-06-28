package com.iti.thesis.helicopter.thesis.context.builder;

import com.iti.thesis.helicopter.thesis.context.constant.SessionDataConst;
import com.iti.thesis.helicopter.thesis.core.collection.MData;

/**
 * <PRE>
 *   SessionFactory
 * </PRE>
 *
 */
public class SessionDataBuilder {
	
	private MData metaData = new MData();
	public static SessionDataBuilder create() {
		return new SessionDataBuilder();
	}
	
	public SessionDataBuilder loginUserId(String loginUserId) {
		this.metaData.setString("loginUserId", loginUserId);
		return this;
	}
	
	public SessionDataBuilder userRoleID(String roleID) {
		this.metaData.setString("userRoleID", roleID);
		return this;
	}
	
	public SessionDataBuilder setMData(MData data) {
		for (String key : SessionDataConst.META_DATA_FIELD) {
			if (data.containsKey(key)) {
				this.metaData.set(key, data.get(key));
			}
		}
		return this;
	}
	
	public MData build() {
		return this.metaData;
	}
	
}
