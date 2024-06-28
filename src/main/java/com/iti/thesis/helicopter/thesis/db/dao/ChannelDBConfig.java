package com.iti.thesis.helicopter.thesis.db.dao;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class ChannelDBConfig {

	@Autowired
	Environment env;
		
	@Bean(name = "channelDBDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public DataSource channelDBDataSource() throws Exception {
		return DataSourceBuilder.create().build();
	}
	
	@Bean(name = "channelDBSqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("channelDBDataSource") DataSource dataSource, ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource(env.getProperty("channeldb.path.config")));
		sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(env.getProperty("channeldb.path.mapperlocation")));
		return sqlSessionFactoryBean.getObject();
	}
	
	@Bean(name = "channelDBSqlSessionTemplate")
	public SqlSessionTemplate sqlSessionTemplate(@Qualifier("channelDBSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
	
	@Bean(name = "channelDBTransactionManager")
	public PlatformTransactionManager dataSourceTransactionManager(@Qualifier("channelDBDataSource") DataSource dataSource) {
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
		dataSourceTransactionManager.setDataSource(dataSource);
		return dataSourceTransactionManager;
	}

}
