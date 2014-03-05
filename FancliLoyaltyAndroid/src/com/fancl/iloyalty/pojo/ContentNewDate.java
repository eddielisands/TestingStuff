package com.fancl.iloyalty.pojo;

public class ContentNewDate {
	private String type;
	private String numberOfDate;
	
	public ContentNewDate(String type, String numberOfDate)
	{
		this.type = type;
		this.numberOfDate = numberOfDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNumberOfDate() {
		return numberOfDate;
	}

	public void setNumberOfDate(String numberOfDate) {
		this.numberOfDate = numberOfDate;
	}

	@Override
	public String toString() {
		return "ContentNewDate [type=" + type + ", numberOfDate=" + numberOfDate + "]";
	}
}
