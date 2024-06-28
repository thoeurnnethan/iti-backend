package com.iti.thesis.helicopter.thesis.db.dao;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

/**
 * ChannelDBDao
 */
public interface ChannelDBDao {
	int insert(String statement, MData parameter) throws MException;
	int update(String statement, MData parameter) throws MException;
	int delete(String statementID, MData parameter) throws MException;
	MData selectOne(String statementID, MData parameter) throws MException;
	MMultiData selectList(String statementID, MData parameter) throws MException;
	MMultiData selectListForPage(String statementID, MData parameter) throws MException;
}
