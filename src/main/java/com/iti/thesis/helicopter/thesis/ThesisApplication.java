package com.iti.thesis.helicopter.thesis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.service.UserInfoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ConfigurationPropertiesScan
@SpringBootApplication
@ComponentScan("com.iti.thesis.helicopter.thesis.controller")
@ComponentScan("com.iti.thesis.helicopter.thesis.service")
@ComponentScan("com.iti.thesis.helicopter.thesis.db")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableTransactionManagement
public class ThesisApplication implements CommandLineRunner{
	
	@Autowired
	private UserInfoService userInfoService;
	

	public static void main(String[] args) {
		SpringApplication.run(ThesisApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			userInfoService.registerUserDefault(new MData());
			log.error("Start up successfuly");
		} catch (Exception e) {
			log.error("Some error occur when doing some login after application Started up.");
		}
	}

}
