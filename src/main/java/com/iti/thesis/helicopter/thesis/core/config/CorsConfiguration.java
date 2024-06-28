package com.iti.thesis.helicopter.thesis.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(@NonNull CorsRegistry registry) {
		registry
		.addMapping("/**")
//		.allowedOrigins("http://localhost:9090")
		.allowedOriginPatterns("*")
		.allowCredentials(true)
		.allowedHeaders("*")
		.exposedHeaders("*")
		.maxAge(60*30)
		.allowedMethods("GET","POST","PUT","DELETE","OPTIONS","HEAD")
		.allowedMethods("POST")
		;
	}
}
