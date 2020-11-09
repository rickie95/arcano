package com.riccardomalavolti.arcano.dto;

import org.modelmapper.ModelMapper;

import com.riccardomalavolti.arcano.model.Event;

public class EventMapper {
	
	private static ModelMapper mapper;
	
	private EventMapper() {
		throw new IllegalStateException("Utility class");
	}
	
	public static EventDTO toEventDTO(Event event) {
		mapper = new ModelMapper();
		return mapper.map(event, EventDTO.class);
	}
	
	public static EventDetails toEventDetails(Event event) {
		mapper = new ModelMapper();
		return mapper.map(event, EventDetails.class);
	}
	

}
