package com.iti.thesis.helicopter.thesis.context;

import java.util.Map;

import com.iti.thesis.helicopter.thesis.core.collection.MData;

public class MContextThreadLocal extends ThreadLocal<Map<String, Object>> {

	@Override
	protected Map<String, Object> initialValue() {
		return new MData();
	}

	public MData getMData() {
		return (MData) this.get();
	}

	public void clear() {
		((MData) this.get()).clear();
	}
}
