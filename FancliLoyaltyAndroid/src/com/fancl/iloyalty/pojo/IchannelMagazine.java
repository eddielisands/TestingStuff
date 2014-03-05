package com.fancl.iloyalty.pojo;

import java.io.Serializable;

public class IchannelMagazine implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 460096002830880618L;
	private String objectId;
	private String templateType;
	private String thumbnail;
	private String image;
	private String titleZh;
	private String titleSc;
	private String titleEn;
	private String descriptionZh;
	private String descriptionSc;
	private String descriptionEn;
	private String videoThumbnailZh;
	private String videoThumbnailSc;
	private String videoThumbnailEn;
	private String videoLinkZh;
	private String videoLinkSc;
	private String videoLinkEn;
	private String videoDuration;
	private String type;
	private String publishStatus;
	private String isNew;
	private String publishStartDatetime;
	private String publishEndDatetime;
	private String createDatetime;
	private String isRead;
	
	public IchannelMagazine(String objectId, String templateType, String thumbnail,
			String titleZh, String titleSc, String titleEn,
//			String descriptionZh, String descriptionSc, String descriptionEn,
			String videoThumbnailZh, String videoThumbnailSc,
			String videoThumbnailEn, String videoLinkZh, String videoLinkSc,
			String videoLinkEn, String videoDuration, String type, String publishStatus,
			String isNew, String publishStartDatetime,
			String publishEndDatetime, String createDatetime, String isRead)
	{
		super();
		this.objectId = objectId;
		this.templateType = templateType;
		this.thumbnail = thumbnail;
//		this.image = image;
		this.titleZh = titleZh;
		this.titleSc = titleSc;
		this.titleEn = titleEn;
//		this.descriptionZh = descriptionZh;
//		this.descriptionSc = descriptionSc;
//		this.descriptionEn = descriptionEn;
		this.videoThumbnailZh = videoThumbnailZh;
		this.videoThumbnailSc = videoThumbnailSc;
		this.videoThumbnailEn = videoThumbnailEn;
		this.videoLinkZh = videoLinkZh;
		this.videoLinkSc = videoLinkSc;
		this.videoLinkEn = videoLinkEn;
		this.videoDuration = videoDuration;
		this.type = type;
		this.publishStatus = publishStatus;
		this.isNew = isNew;
		this.publishStartDatetime = publishStartDatetime;
		this.publishEndDatetime = publishEndDatetime;
		this.createDatetime = createDatetime;
		this.isRead = isRead;
	}
	
	public IchannelMagazine(String objectId, String image, 
			String descriptionZh, String descriptionSc, String descriptionEn) {
		super();
		this.objectId = objectId;
		this.image = image;
		this.descriptionZh = descriptionZh;
		this.descriptionSc = descriptionSc;
		this.descriptionEn = descriptionEn;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
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

	public String getVideoThumbnailZh() {
		return videoThumbnailZh;
	}

	public void setVideoThumbnailZh(String videoThumbnailZh) {
		this.videoThumbnailZh = videoThumbnailZh;
	}

	public String getVideoThumbnailSc() {
		return videoThumbnailSc;
	}

	public void setVideoThumbnailSc(String videoThumbnailSc) {
		this.videoThumbnailSc = videoThumbnailSc;
	}

	public String getVideoThumbnailEn() {
		return videoThumbnailEn;
	}

	public void setVideoThumbnailEn(String videoThumbnailEn) {
		this.videoThumbnailEn = videoThumbnailEn;
	}

	public String getVideoLinkZh() {
		return videoLinkZh;
	}

	public void setVideoLinkZh(String videoLinkZh) {
		this.videoLinkZh = videoLinkZh;
	}

	public String getVideoLinkSc() {
		return videoLinkSc;
	}

	public void setVideoLinkSc(String videoLinkSc) {
		this.videoLinkSc = videoLinkSc;
	}

	public String getVideoLinkEn() {
		return videoLinkEn;
	}

	public void setVideoLinkEn(String videoLinkEn) {
		this.videoLinkEn = videoLinkEn;
	}
	
	public String getVideoDuration() {
		return videoDuration;
	}

	public void setVideoDuration(String videoDuration) {
		this.videoDuration = videoDuration;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}

	public String getIsNew() {
		return isNew;
	}

	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}

	public String getPublishStartDatetime() {
		return publishStartDatetime;
	}

	public void setPublishStartDatetime(String publishStartDatetime) {
		this.publishStartDatetime = publishStartDatetime;
	}

	public String getPublishEndDatetime() {
		return publishEndDatetime;
	}

	public void setPublishEndDatetime(String publishEndDatetime) {
		this.publishEndDatetime = publishEndDatetime;
	}

	public String getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
	}

	@Override
	public String toString() {
		return "IchannelMagazine [templateType=" + templateType + ", thumbnail=" + thumbnail + ", titleZh=" + titleZh + ", titleSc=" + titleSc + ", titleEn=" + titleEn + ", videoThumbnailZh=" + videoThumbnailZh + ", videoThumbnailSc=" + videoThumbnailSc + ", videoThumbnailEn=" + videoThumbnailEn + ", videoLinkZh=" + videoLinkZh + ", videoLinkSc=" + videoLinkSc + ", videoLinkEn=" + videoLinkEn + ", videoDuration=" + videoDuration + ", type=" + type + ", publishStatus=" + publishStatus + ", isNew=" + isNew + ", publishStartDatetime=" + publishStartDatetime + ", publishEndDatetime=" + publishEndDatetime + ", createDatetime=" + createDatetime + "]";
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}
}
