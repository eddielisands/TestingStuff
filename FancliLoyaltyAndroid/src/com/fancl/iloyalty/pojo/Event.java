package com.fancl.iloyalty.pojo;

public class Event {

	private String type;
	private String eventType;
	private String eventId;

	public Event(String type, String eventType, String eventId) {
		super();
		this.type = type;
		this.eventType = eventType;
		this.eventId = eventId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

}
