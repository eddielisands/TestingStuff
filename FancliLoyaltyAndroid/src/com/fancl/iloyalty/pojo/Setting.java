package com.fancl.iloyalty.pojo;

public class Setting {
	private String objectId;
	private String type;
	private String titleZh;
	private String titleSc;
	private String titleEn;
	private String descriptionZh;
	private String descriptionSc;
	private String descriptionEn;
	
	public Setting(String objectId, String type, String titleZh, String titleSc, String titleEn,
			String descriptionZh, String descriptionSc, String descriptionEn)
	{
		this.objectId = objectId;
		this.type = type;
		this.titleZh = titleZh;
		this.titleSc = titleSc;
		this.titleEn = titleEn;
		this.descriptionZh = descriptionZh;
		this.descriptionSc = descriptionSc;
		this.descriptionEn = descriptionEn;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
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

	public String getDescriptionZh() {
		return descriptionZh;
	}

	public void setDescriptionZh(String descriptionZh) {
		this.descriptionZh = descriptionZh;
	}

	public String getDescriptionSc() {
		return descriptionSc;
	}

	public void setDescriptionSc(String descriptionSc) {
		this.descriptionSc = descriptionSc;
	}

	public String getDescriptionEn() {
		return descriptionEn;
	}

	public void setDescriptionEn(String descriptionEn) {
		this.descriptionEn = descriptionEn;
	}

	@Override
	public String toString() {
		return "Setting [type=" + type + ", objectId=" + objectId + ", titleZh=" + titleZh + ", titleSc=" + titleSc + ", titleEn=" + titleEn + ", descriptionZh=" + descriptionZh + ", descriptionSc=" + descriptionSc + ", descriptionEn=" + descriptionEn + "]";
	}
	
	
}
