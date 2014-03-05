package com.fancl.iloyalty.pojo;

public class ValidateUserParam {
	String fanclMemberId;
	String uuid;
	String userToken;
	String language;
	String location;
	
	public ValidateUserParam() {
		super();
	}
	
	public ValidateUserParam(String fanclMemberId, String uuid, String userToken, String language, String location) {
		super();
		this.fanclMemberId = fanclMemberId;
		this.uuid = uuid;
		this.userToken = userToken;
		this.language = language;
		this.location = location;
	}

	public String getFanclMemberId() {
		return fanclMemberId;
	}

	public void setFanclMemberId(String fanclMemberId) {
		this.fanclMemberId = fanclMemberId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "ValidateUserParam [fanclMemberId=" + fanclMemberId + ", uuid="
				+ uuid + ", userToken=" + userToken + ", language=" + language
				+ ", location=" + location + "]";
	}
	
	
}
