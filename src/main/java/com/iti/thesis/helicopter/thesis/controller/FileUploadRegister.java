package com.iti.thesis.helicopter.thesis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.service.FileUploadService;;

@RestController
@RequestMapping("/api/file-upload")
public class FileUploadRegister {
	
	@Autowired
	private FileUploadService		fileUploadService;
	@Autowired
	private PlatformTransactionManager	txManager;

	@PostMapping("/register")
	public MData onExecute(@RequestParam("file") MultipartFile file) throws MException {
		TransactionStatus	txStatus	= txManager.getTransaction(new DefaultTransactionAttribute());
		try {
			MData	result		= fileUploadService.storeFile(file);
			txManager.commit(txStatus);
			return result;
		} catch (MException e) {
			txManager.rollback(txStatus);
			throw e;
		} catch (Exception e){
			txManager.rollback(txStatus);
			throw new MBizException(CommonErrorCode.UNCAUGHT.getCode(), CommonErrorCode.UNCAUGHT.getDescription());
		}
	}
	
}
