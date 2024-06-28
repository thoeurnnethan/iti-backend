package com.iti.thesis.helicopter.thesis.core.Json;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonAdaptorObject extends HashMap<JsonAdaptorObject.TYPE, JsonNode> {
	
	private static final long serialVersionUID = 6234450658638598387L;
	private List<MultipartFile> uploadFileList;
	
	public JsonAdaptorObject() {
	}

	public JsonNode get(TYPE t) {
		return (JsonNode)super.get(t);
	}
	
	public JsonNode put(TYPE t, JsonNode obj) {
		return (JsonNode)super.put(t, obj);
	}
	
	public List<MultipartFile> getUploadFileList() {
		return this.uploadFileList;
	}
	
	public void setUploadFileList(List<MultipartFile> uploadFileList) {
		this.uploadFileList = uploadFileList;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("{ \r\n");
		@SuppressWarnings("rawtypes")
		Iterator var2 = super.keySet().iterator();
		
		while(var2.hasNext()) {
		TYPE t = (TYPE)var2.next();
			buf.append(t).append(" : ");
			buf.append(this.get(t).toString());
			buf.append("\r\n");
		}
		buf.append("}");
		return buf.toString();
	}
	
	public enum TYPE{
		META,
		SERVER,
		REQUEST,
		RESPONSE,
		RESULT;
	}
}
