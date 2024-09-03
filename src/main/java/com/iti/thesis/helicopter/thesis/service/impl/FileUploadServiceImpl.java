package com.iti.thesis.helicopter.thesis.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.service.FileUploadService;
import com.iti.thesis.helicopter.thesis.util.MDateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {
	
	@Value("${file.upload-dir}")
	private String uploadDir;
	
	@Override
	public MData storeFile(MultipartFile multipartFile) throws MException {
		boolean isErrorUpload = false;
		try {
//			String	uploadDateTime			= MDateUtil.getCurrentDateTime();
//			String	fileName				= StringUtils.cleanPath(multipartFile.getOriginalFilename());
//			Path	fileStorageLocation		= Paths.get(uploadDir + File.separator + uploadDateTime).toAbsolutePath().normalize();
			String	fileName				= StringUtils.cleanPath(multipartFile.getOriginalFilename());
			Path	fileStorageLocation		= Paths.get(uploadDir + File.separator).toAbsolutePath().normalize();
			Files.createDirectories(fileStorageLocation);
			
			Path	targetLocation			= fileStorageLocation.resolve(fileName);
			Files.copy(multipartFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			
			MData	fileInfo				= new MData();
			fileInfo.put("fileName", fileName);
			fileInfo.put("fileType", multipartFile.getContentType());
			fileInfo.put("fileSize", multipartFile.getSize());
			fileInfo.put("filePath", targetLocation.toString());
			
//			fileInfoMapper.insertFileInfo(fileInfo);
			return fileInfo;
		} catch (MException e) {
			isErrorUpload = true;
			throw e;
		} catch (Exception e){
			isErrorUpload = true;
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription());
		} finally {
			if(isErrorUpload) {
				log.error("Error register file: {}", multipartFile.getOriginalFilename() );
			}
		}
		
	}

	@Override
	public ResponseEntity<Resource> downloadFile(String fileName) throws MException {
		try {
			String		filePath	= "D://temp//data//Upload//";
			Path		path		= Paths.get(filePath + fileName);
			Resource	resource	= new UrlResource(path.toUri());
			resource.getFilename();
			return ResponseEntity.ok()
					.contentType(MediaType.IMAGE_JPEG)
					.body(resource);
		} catch (MException e) {
			throw e;
		} catch (Exception e){
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription());
		}
	}
}
