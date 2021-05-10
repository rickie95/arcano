package com.riccardomalavolti.arcano.endpoints.rest;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

	private final ObjectMapper mapper;
	
	public ObjectMapperContextResolver() {
		this.mapper = createCustomObjectMapper();
	}
	
	private ObjectMapper createCustomObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules()
		.registerModule(new Jdk8Module())
		.registerModule(new JavaTimeModule())
		.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return mapper;
	}

}
