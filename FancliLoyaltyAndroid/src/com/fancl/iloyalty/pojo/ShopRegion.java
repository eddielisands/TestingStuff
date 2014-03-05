package com.fancl.iloyalty.pojo;

public class ShopRegion {
	private String objectId;
	private String parentId;
	private String titleZh;
	private String titleSc;
	private String titleEn;
	
	public ShopRegion(String objectId, String parentId, String titleZh, String titleSc,
			String titleEn)
	{
		this.objectId = objectId;
		this.parentId = parentId;
		this.titleZh = titleZh;
		this.titleSc = titleSc;
		this.titleEn = titleEn;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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

	@Override
	public String toString() {
		return "ShopRegion [parentId=" + parentId + ", titleZh=" + titleZh + ", titleSc=" + titleSc + ", titleEn=" + titleEn + ", objectId=" + objectId + "]";
	}
	
	
}
