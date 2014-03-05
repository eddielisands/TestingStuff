package com.fancl.iloyalty.pojo;

public class UserMessage {
	private String userId;
	private String messageId;
	private String createDatetime;
	
	public UserMessage(String userId, String messageId, String createDatetime)
	{
		this.userId = userId;
		this.messageId = messageId;
		this.createDatetime = createDatetime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
	}

	@Override
	public String toString() {
		return "UserMessage [userId=" + userId + ", messageId=" + messageId + ", createDatetime=" + createDatetime + "]";
	}
	
	
}
