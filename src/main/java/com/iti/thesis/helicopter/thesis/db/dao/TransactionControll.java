package com.iti.thesis.helicopter.thesis.db.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import com.iti.thesis.helicopter.thesis.util.MGUIDUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TransactionControll {
	
	@Autowired
	private PlatformTransactionManager			txManager;
	private TransactionStatus	txStatus 		= null;
	private String				transactionName = null;
	
	public void OpenTransaction() {
		transactionName = "TX_MAIN"+MGUIDUtil.generateGUID();
		DefaultTransactionAttribute defaultTransactionAttribute = new DefaultTransactionAttribute();
		defaultTransactionAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		defaultTransactionAttribute.setName(transactionName);
		txStatus			= txManager.getTransaction(defaultTransactionAttribute);
		log.info("Open Transaction :: {}", transactionName);
	}
	
	public void commitTransaction() {
		txManager.commit(txStatus);
		log.info("Commit Transaction :: {}", transactionName);
	}
	
	public void rollbackTransaction() {
		txManager.rollback(txStatus);
		log.info("Rollback Transaction :: {}", transactionName);
	}
}
