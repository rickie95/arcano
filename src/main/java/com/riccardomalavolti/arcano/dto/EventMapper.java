package com.riccardomalavolti.arcano.dto;

import org.modelmapper.ModelMapper;

import com.riccardomalavolti.arcano.model.Event;

public final class EventMapper {
	
	private EventMapper() {}
	
	private static ModelMapper mapperInstance() {
		return new ModelMapper();
	}
	
	public static EventBrief toEventBrief(Event event) {
		return mapperInstance().map(event, EventBrief.class);
	}
	
	public static EventDetails toEventDetails(Event event) {
		return mapperInstance().map(event, EventDetails.class);
	}
	

}
