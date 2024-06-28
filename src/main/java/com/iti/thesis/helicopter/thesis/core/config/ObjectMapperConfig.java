package com.iti.thesis.helicopter.thesis.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

@Configuration
public class ObjectMapperConfig {
    
    @Bean
    public ObjectMapper objectMapper(){
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); //To avoid not support pojo fields   
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false); //To avoid empty pojo fields
		objectMapper.setNodeFactory(JsonNodeFactory.withExactBigDecimals(true)); //To avoid Exponential Notation, Ex: 1e+6
		return objectMapper;
    }
}
