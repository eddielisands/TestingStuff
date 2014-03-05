package com.fancl.iloyalty.pojo;

public class IchannelType {
	private String objectId;
	private String type;
	private String titleZh;
	private String titleSc;
	private String titleEn;
	private String sequence;
	
	public IchannelType(String objectId, String type, String titleZh, String titleSc,
			String titleEn, String sequence)
	{
		this.objectId = objectId;
		this.type = type;
		this.titleZh = titleZh;
		this.titleSc = titleSc;
		this.titleEn = titleEn;
		this.sequence = sequence;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitleZh() {
		return titleZh;
	}

	public void setTitleZh(String titleZh) {
		this.titleZh = titleZh;
	}

	public String getTitleSc() {
		return titleSc;
	}

	public void setTitleSc(String titleSc) {
		this.titleSc = titleSc;
	}

	public String getTitleEn() {
		return titleEn;
	}

	public void setTitleEn(String titleEn) {
		this.titleEn = titleEn;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	@Override
	public String toString() {
		return "IchannelType [objectId=" + objectId + ", type=" + type + ", titleZh=" + titleZh + ", titleSc=" + titleSc + ", titleEn=" + titleEn + ", sequence=" + sequence + "]";
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	
	
}
