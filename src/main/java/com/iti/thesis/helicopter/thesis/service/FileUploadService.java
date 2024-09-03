package com.iti.thesis.helicopter.thesis.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

public interface FileUploadService {

	public MData storeFile(MultipartFile file) throws MException;
	public ResponseEntity<Resource> downloadFile(String file) throws MException;

}
