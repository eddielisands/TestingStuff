package com.fancl.iloyalty.pojo;

import com.fancl.iloyalty.util.DataUtil;

public class Notification {
	private String createDatetime;
	private String contentType;
	private String recordId;
	private String content;
	
	public Notification() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getCreateDatetime() {
		String returnString = DataUtil.trimDateString(createDatetime);
		return returnString;
	}

	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Notification [createDatetime=" + createDatetime
				+ ", contentType=" + contentType + ", recordId=" + recordId
				+ ", content=" + content + "]";
	}
}
