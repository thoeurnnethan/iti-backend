package com.iti.thesis.helicopter.thesis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ConfigurationPropertiesScan
@SpringBootApplication
@ComponentScan("com.iti.thesis.helicopter.thesis.controller")
@ComponentScan("com.iti.thesis.helicopter.thesis.service")
@ComponentScan("com.iti.thesis.helicopter.thesis.db")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableTransactionManagement
public class ThesisApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThesisApplication.class, args);
	}

}
