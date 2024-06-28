package com.iti.thesis.helicopter.thesis.db.interceptors;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;


@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
	@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
	@Signature(type = Executor.class, method = "queryCursor", args = {MappedStatement.class, Object.class, RowBounds.class}),
	@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),})
@Slf4j
public class DBInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object			proceed		= null;
		MappedStatement	ms			= null;
		Object			parameter	= null;
		BoundSql		boundSql	= null;
		long			stime		= 0L;
		long			etime		= 0L;
		LocalDateTime	startDate	= null;
		LocalDateTime	endDate		= null;
		
		//#########################################################################
		//# Prepare basis data
		//#########################################################################
		for (Object arg : invocation.getArgs()) {
			
			if (arg instanceof MappedStatement) {
				ms = (MappedStatement)arg;
			} else if (arg instanceof RowBounds || arg instanceof ResultHandler || arg instanceof CacheKey) {
				//
			} else if (arg instanceof BoundSql) {
				boundSql = (BoundSql)arg;
			} else if (arg != null) {
				parameter = arg;
			}
			
		}
		
		if (boundSql == null) {
			boundSql = ms.getBoundSql(parameter);
		}
		
		//#########################################################################
		//# Execute query 
		//#########################################################################
		try {
			// process start time
			stime		= System.currentTimeMillis();
			startDate	= LocalDateTime.now();
			proceed		= invocation.proceed();
		} catch (Throwable e) {
			throw e;
		} finally {
			//#########################################################################
			//# Make result and print log 
			//#########################################################################
			// process end time
			etime	= System.currentTimeMillis();
			endDate	= LocalDateTime.now();
			log.debug("MapperId : {}", ms.getId());
			log.trace("Param : {}", parameter);
			log.debug("Query Start : {}", startDate != null ? startDate.toString().replace("T", " ") : "null");
			log.debug("SQL : \n{}", printLog(boundSql, parameter));
			log.debug("Query End : {} ", endDate != null ? endDate.toString().replace("T", " ") : "null");
			
			if (proceed instanceof Collection) {
				int count = ((Collection<?>)proceed).size();
				log.debug(String.format("(%d ms, row count %d)", (etime - stime), count));
			} else {
				log.debug(String.format("(%d ms)", (etime - stime)));
			}
			
		}
		
		return proceed;
	}
	
	private String printLog(BoundSql boundSql, Object parameter) {
		String			stringBoundSql	= boundSql.getSql().replaceAll("(?m)^[ \t]*\r?\n", "");
		StringBuilder	sql				= new StringBuilder(stringBoundSql);
		
		if (parameter != null) {
			
			for (ParameterMapping parameterMapping : boundSql.getParameterMappings()) {
				String	property	= parameterMapping.getProperty();
				Object	value		= "<NotFound>";
				
				if (boundSql.hasAdditionalParameter(property)) {
					value = boundSql.getAdditionalParameter(property);
				} else if (ClassUtils.isPrimitiveOrWrapper(parameter.getClass())) {
					value = parameter;
				} else if (parameter instanceof Map) {
					value = ((Map<?, ?>)parameter).get(property);
				} else if (parameter instanceof String) {
					value = parameter;
				} else {
					
					try {
						PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(parameter.getClass()).getPropertyDescriptors();
						
						for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
							String name = propertyDescriptor.getName();
							
							if (property.equals(name) || property.equals(StringUtils.capitalize(name))) {
								value = propertyDescriptor.getReadMethod().invoke(parameter);
								break;
							}
							
						}
						
					} catch (Exception e) {
						// skipped
					}
					
				}
				
				int	start	= sql.indexOf("?");
				int	end		= start + 1;
				
				if (value == null) {
					sql.replace(start, end, "NULL");
				} else if (value instanceof String) {
					sql.replace(start, end, "'" + value.toString() + "'");
				} else {
					sql.replace(start, end, value.toString());
				}
				
			}
			
		}
		
		return sql.toString();
	}

}
