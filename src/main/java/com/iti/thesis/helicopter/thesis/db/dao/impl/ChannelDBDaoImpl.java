package com.iti.thesis.helicopter.thesis.db.dao.impl;

import org.apache.ibatis.exceptions.TooManyResultsException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.constant.CommonErrorCode;
import com.iti.thesis.helicopter.thesis.core.exception.MBizException;
import com.iti.thesis.helicopter.thesis.core.exception.MDuplicateException;
import com.iti.thesis.helicopter.thesis.core.exception.MException;
import com.iti.thesis.helicopter.thesis.core.exception.MNotAffectedException;
import com.iti.thesis.helicopter.thesis.core.exception.MNotFoundException;
import com.iti.thesis.helicopter.thesis.core.exception.MTooManyRowException;
import com.iti.thesis.helicopter.thesis.db.dao.ChannelDBDao;
import com.iti.thesis.helicopter.thesis.db.dao.constant.MDaoConst;
import com.iti.thesis.helicopter.thesis.util.MDateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChannelDBDaoImpl implements ChannelDBDao {
    @Qualifier("channelDBSqlSessionTemplate")
	@Autowired
	SqlSessionTemplate sqlSession;
	
	@Override
	public int insert(String statement, MData parameter) throws MException {
		
		try {
			 parameter.appendFrom(getCommonInfo(true));
			return sqlSession.insert(statement, parameter);
		} catch (BadSqlGrammarException e) {
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.REGISTER.getCode(), CommonErrorCode.REGISTER.getDescription(), e);
		} catch (DuplicateKeyException e) {
			log.error(e.getLocalizedMessage());
			throw new MDuplicateException(CommonErrorCode.DUPLICATED_DATA.getCode(), CommonErrorCode.DUPLICATED_DATA.getDescription(), e);
		} catch (DataIntegrityViolationException e) {
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.REGISTER.getCode(), CommonErrorCode.REGISTER.getDescription(), e);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.REGISTER.getCode(), CommonErrorCode.REGISTER.getDescription(), e);
		}
		
	}
	
	@Override
	public int update(String statement, MData parameter) throws MException {
		try {
			 parameter.appendFrom(getCommonInfo(false));
			int rows = sqlSession.update(statement, parameter);
			
			if (rows <= 0) {
				throw new MNotAffectedException(CommonErrorCode.UPDATE_NOT_AFFECT.getCode(), CommonErrorCode.UPDATE_NOT_AFFECT.getDescription());
			}
			
			return rows;
		} catch (MNotAffectedException e) {
			log.error(e.getLocalizedMessage());
			throw e;
		}catch (BadSqlGrammarException e) {
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UPDATE.getCode(), CommonErrorCode.UPDATE.getDescription(), e);
		} catch (DataIntegrityViolationException e) {
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UPDATE.getCode(), CommonErrorCode.UPDATE.getDescription(), e);
		} catch (Exception  e) {
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.UPDATE.getCode(), CommonErrorCode.UPDATE.getDescription(), e);
		}
		
	}
	
	@Override
	public int delete(String statementID, MData parameter) throws MException {
		
		try {
			// parameter.appendFrom(getCommonInfo(false));
			int result = sqlSession.delete(statementID, parameter);
			
			if (result == 0) {
				throw new MNotAffectedException(CommonErrorCode.UPDATE_NOT_AFFECT.getCode(), CommonErrorCode.UPDATE_NOT_AFFECT.getDescription());
			}
			
			return result;
		}catch ( MNotAffectedException e) {
			log.error(e.getLocalizedMessage());
			throw e;
		} catch (BadSqlGrammarException e) {
			log.error(e.getLocalizedMessage());
			throw new MException(CommonErrorCode.DELETE.getCode(), CommonErrorCode.DELETE.getDescription(), e);
		} catch (DataIntegrityViolationException e) {
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.DELETE.getCode(), CommonErrorCode.DELETE.getDescription(), e);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.DELETE.getCode(), CommonErrorCode.DELETE.getDescription(), e);
		}
		
	}
	
	@Override
	public MData selectOne(String statementID, MData parameter) throws MException {
		MData result = null;
		
		try {
			result = sqlSession.selectOne(statementID, parameter);
			
			if (result == null) {
				throw new MNotFoundException(CommonErrorCode.NOT_FOUND.getCode(), CommonErrorCode.NOT_FOUND.getDescription());
			}
			
		}catch (TooManyResultsException e) {
			throw new MTooManyRowException(CommonErrorCode.TOO_MANY_ROWS.getCode(), CommonErrorCode.TOO_MANY_ROWS.getDescription(), e);
		} catch (BadSqlGrammarException e) {
			throw new MBizException(CommonErrorCode.INQUIRY.getCode(), CommonErrorCode.INQUIRY.getDescription(), e);
		}
		
		return new MData(result);
	}
	
	@Override
	public MMultiData selectList(String statementID, MData parameter) throws MException {
		try {
			return new MMultiData(sqlSession.selectList(statementID, parameter));
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.INQUIRY.getCode(), CommonErrorCode.INQUIRY.getDescription(), e);
		}
	}

	@Override
	public MMultiData selectListForPage(String statementID, MData parameter) throws MException {
		try {
			
			setPagination(parameter);
			return new MMultiData(sqlSession.selectList(statementID, parameter));
			
		} catch (Exception e) {
//			log.error(e.getLocalizedMessage());
			throw new MBizException(CommonErrorCode.INQUIRY.getCode(), CommonErrorCode.INQUIRY.getDescription(), e);
		}
	}
	
	private void setPagination(MData parameter) {
		
		if (parameter == null) {
			parameter = new MData();
		}
		
		int	limit		= 0;
		int	pageSize	= parameter.getInt(MDaoConst.PAGE_SIZE);
		
		if (pageSize <= 0) {
			limit = MDaoConst.DEFAULT_LIMIT;
		} else {
			limit = pageSize;
		}
		
		parameter.put(MDaoConst.LIMIT, limit);
		int	offset		= 0;
		int	pageNumber	= parameter.getInt(MDaoConst.PAGE_NUMBER);
		
		if (pageNumber <= 0) {
			offset = MDaoConst.DEFAULT_OFFSET;
		} else {
			offset = (pageNumber - 1) * limit;
		}
		
		parameter.put(MDaoConst.OFFSET, offset);
	}
	
	private MData getCommonInfo(boolean isInsert) {
		MData commonParam = new MData();
		commonParam.setString("lastChangeDate", MDateUtil.getCurrentDate());
		commonParam.setString("lastChangeTime", MDateUtil.getCurrentTime(MDateUtil.FORMAT_TIME_SHORT));
		
		if (isInsert) {
			commonParam.setString("firstRegisterDate", MDateUtil.getCurrentDate());
			commonParam.setString("firstRegisterTime", MDateUtil.getCurrentTime(MDateUtil.FORMAT_TIME_SHORT));
		}
		
		return commonParam;
	}
	
}
