package com.fancl.iloyalty.pojo;

import java.io.Serializable;

public class HotItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String objectId;
	private String type;
	private String thumbnail;
	private String highlightImage;
	private String titleZh;
	private String titleSc;
	private String titleEn;
	private String descriptionZh;
	private String descriptionSc;
	private String descriptionEn;
	private String image1;
	private String image2;
	private String image3;
	private String image4;
	private String image5;
	private String linkRecordType;
	private String linkRecordId;
	private String startDatetime;
	private String endDatetime;
	private String linkType;
	private String linkZh;
	private String linkSc;
	private String linkEn;
	private String publishStatus;
	private String isHighlight;
	private String orderNumber;
	private String isRead;
	private String createDatetime;
	
	public HotItem(String objectId, String type, String thumbnail, String highlightImage,
			String titleZh, String titleSc, String titleEn,
			String descriptionZh, String descriptionSc, String descriptionEn,
			String image1, String image2, String image3, String image4,
			String image5, String linkRecordType, String linkRecordId,
			String startDatetime, String endDatetime, String linkType,
			String linkZh, String linkSc, String linkEn, String publishStatus,
			String isHighlight, String orderNumber, String isRead, String createDatetime)
	{
		this.objectId = objectId;
		this.type = type;
		this.thumbnail = thumbnail;
		this.highlightImage = highlightImage;
		this.titleZh = titleZh;
		this.titleSc = titleSc;
		this.titleEn = titleEn;
		this.descriptionZh = descriptionZh;
		this.descriptionSc = descriptionSc;
		this.descriptionEn = descriptionEn;
		this.image1 = image1;
		this.image2 = image2;
		this.image3 = image3;
		this.image4 = image4;
		this.image5 = image5;
		this.linkRecordType = linkRecordType;
		this.linkRecordId = linkRecordId;
		this.startDatetime = startDatetime;
		this.endDatetime = endDatetime;
		this.linkType = linkType;
		this.linkZh = linkZh;
		this.linkSc = linkSc;
		this.linkEn = linkEn;
		this.publishStatus = publishStatus;
		this.isHighlight = isHighlight;
		this.orderNumber = orderNumber;
		this.setIsRead(isRead);
		this.createDatetime = createDatetime;
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

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getHighlightImage() {
		return highlightImage;
	}

	public void setHighlightImage(String highlightImage) {
		this.highlightImage = highlightImage;
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

	public String getImage1() {
		if (image1 == null) {
			return "";
		}
		return image1;
	}

	public void setImage1(String image1) {
		this.image1 = image1;
	}

	public String getImage2() {
		if (image2 == null) {
			return "";
		}
		return image2;
	}

	public void setImage2(String image2) {
		this.image2 = image2;
	}

	public String getImage3() {
		if (image3 == null) {
			return "";
		}
		return image3;
	}

	public void setImage3(String image3) {
		this.image3 = image3;
	}

	public String getImage4() {
		if (image4 == null) {
			return "";
		}
		return image4;
	}

	public void setImage4(String image4) {
		this.image4 = image4;
	}

	public String getImage5() {
		if (image5 == null) {
			return "";
		}
		return image5;
	}

	public void setImage5(String image5) {
		this.image5 = image5;
	}

	public String getLinkRecordType() {
		return linkRecordType;
	}

	public void setLinkRecordType(String linkRecordType) {
		this.linkRecordType = linkRecordType;
	}

	public String getLinkRecordId() {
		return linkRecordId;
	}

	public void setLinkRecordId(String linkRecordId) {
		this.linkRecordId = linkRecordId;
	}

	public String getStartDatetime() {
		return startDatetime;
	}

	public void setStartDatetime(String startDatetime) {
		this.startDatetime = startDatetime;
	}

	public String getEndDatetime() {
		return endDatetime;
	}

	public void setEndDatetime(String endDatetime) {
		this.endDatetime = endDatetime;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public String getLinkZh() {
		return linkZh;
	}

	public void setLinkZh(String linkZh) {
		this.linkZh = linkZh;
	}

	public String getLinkSc() {
		return linkSc;
	}

	public void setLinkSc(String linkSc) {
		this.linkSc = linkSc;
	}

	public String getLinkEn() {
		return linkEn;
	}

	public void setLinkEn(String linkEn) {
		this.linkEn = linkEn;
	}

	public String getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}

	public String getIsHighlight() {
		return isHighlight;
	}

	public void setIsHighlight(String isHighlight) {
		this.isHighlight = isHighlight;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}

	public String getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
	}

	@Override
	public String toString() {
		return "HotItem [type=" + type + ", objectId=" + objectId + ", thumbnail=" + thumbnail + ", highlightImage=" + highlightImage + ", titleZh=" + titleZh + ", titleSc=" + titleSc + ", titleEn=" + titleEn + ", descriptionZh=" + descriptionZh + ", descriptionSc=" + descriptionSc + ", descriptionEn=" + descriptionEn + ", image1=" + image1 + ", image2=" + image2 + ", image3=" + image3 + ", image4=" + image4 + ", image5=" + image5 + ", linkRecordType=" + linkRecordType + ", linkRecordId=" + linkRecordId + ", startDatetime=" + startDatetime + ", endDatetime=" + endDatetime + ", linkType=" + linkType + ", linkZh=" + linkZh + ", linkSc=" + linkSc + ", linkEn=" + linkEn + ", publishStatus=" + publishStatus + ", isHighlight=" + isHighlight + ", orderNumber=" + orderNumber + ", isRead=" + isRead + ", createDatetime=" + createDatetime + "]";
	}
	
}
