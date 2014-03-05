package com.fancl.iloyalty.pojo;

public class Administrator {
	private String username;
	private String password;
	private String lastLoginDatetime;
	private String createDatetime;
	
	public Administrator(String username, String password,
			String lastLoginDatetime, String createDatetime)
	{
		this.username = username;
		this.password = password;
		this.lastLoginDatetime = lastLoginDatetime;
		this.createDatetime = createDatetime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLastLoginDatetime() {
		return lastLoginDatetime;
	}

	public void setLastLoginDatetime(String lastLoginDatetime) {
		this.lastLoginDatetime = lastLoginDatetime;
	}

	public String getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
	}

	@Override
	public String toString() {
		return "Administrator [username=" + username + ", password=" + password + ", lastLoginDatetime=" + lastLoginDatetime + ", createDatetime=" + createDatetime + "]";
	}
}
