package com.fancl.iloyalty.pojo;

import com.fancl.iloyalty.util.DataUtil;

public class Promotion {
	private String objectId;
	private String code;
	private String thumbnail;
	private String image;
	private String titleZh;
	private String titleSc;
	private String titleEn;
	private String shortDescriptionZh;
	private String shortDescriptionSc;
	private String shortDescriptionEn;
	private String descriptionZh;
	private String descriptionSc;
	private String descriptionEn;
	private String promotionStartDatetime;
	private String promotionEndDatetime;
	private String publishStatus;
	private String promotionType;
	private String isLuckyDraw;
	private String luckyDrawType;
	private String isNew;
	private String isPublic;
	private String gp;
	private String createDatetime;
	
	private String couponStatus;
	private String couponStatusCode;
	private String couponSerialNumber;
	
	private String isParticipated;
	
	private String isRead;
	
	public Promotion(String objectId, String code, String thumbnail, String image,
			String titleZh, String titleSc, String titleEn,
			String shortDescriptionZh, String shortDescriptionSc,
			String shortDescriptionEn, String descriptionZh,
			String descriptionSc, String descriptionEn,
			String promotionStartDatetime, String promotionEndDatetime,
			String publishStatus, String promotionType,
			String isLuckyDraw, String luckyDrawType,
			String isNew, String isPublic, String gp, String createDatetime, String isParticipated, String couponSerialNumber, String isRead)
	{
		this.objectId = objectId;
		this.code = code;
		this.thumbnail = thumbnail;
		this.image = image;
		this.titleZh = titleZh;
		this.titleSc = titleSc;
		this.titleEn = titleEn;
		this.shortDescriptionZh = shortDescriptionZh;
		this.shortDescriptionSc = shortDescriptionSc;
		this.shortDescriptionEn = shortDescriptionEn;
		this.descriptionZh = descriptionZh;
		this.descriptionSc = descriptionSc;
		this.descriptionEn = descriptionEn;
		this.promotionStartDatetime = promotionStartDatetime;
		this.promotionEndDatetime = promotionEndDatetime;
		this.publishStatus = publishStatus;
		this.promotionType = promotionType;
		this.setIsLuckyDraw(isLuckyDraw);
		this.setLuckyDrawType(luckyDrawType);
		this.isNew = isNew;
		this.isPublic = isPublic;
		this.gp = gp;
		this.createDatetime = createDatetime;
		this.isParticipated = isParticipated;
		this.couponSerialNumber = couponSerialNumber;
		this.isRead = isRead;
	}
	
	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getImage() {
		if (image == null) {
			return "";
		}
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

	public String getShortDescriptionZh() {
		return shortDescriptionZh;
	}

	public void setShortDescriptionZh(String shortDescriptionZh) {
		this.shortDescriptionZh = shortDescriptionZh;
	}

	public String getShortDescriptionSc() {
		return shortDescriptionSc;
	}

	public void setShortDescriptionSc(String shortDescriptionSc) {
		this.shortDescriptionSc = shortDescriptionSc;
	}

	public String getShortDescriptionEn() {
		return shortDescriptionEn;
	}

	public void setShortDescriptionEn(String shortDescriptionEn) {
		this.shortDescriptionEn = shortDescriptionEn;
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

	public String getPromotionStartDatetime() {
		return DataUtil.trimDateString(promotionStartDatetime);
	}

	public void setPromotionStartDatetime(String promotionStartDatetime) {
		this.promotionStartDatetime = promotionStartDatetime;
	}

	public String getPromotionEndDatetime() {
		return DataUtil.trimDateString(promotionEndDatetime);
	}

	public void setPromotionEndDatetime(String promotionEndDatetime) {
		this.promotionEndDatetime = promotionEndDatetime;
	}

	public String getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}

	public String getPromotionType() {
		return promotionType;
	}

	public void setPromotionType(String promotionType) {
		this.promotionType = promotionType;
	}

	public String getIsNew() {
		return isNew;
	}

	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}

	public String getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(String isPublic) {
		this.isPublic = isPublic;
	}

	public String getGp() {
		return gp;
	}

	public void setGp(String gp) {
		this.gp = gp;
	}

	public String getCreateDatetime() {
		return DataUtil.trimDateString(createDatetime);
	}

	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
	}

	@Override
	public String toString() {
		return "Promotion [code=" + code + ", objectId=" + objectId + ", thumbnail=" + thumbnail + ", image=" + image + ", titleZh=" + titleZh + ", titleSc=" + titleSc + ", titleEn=" + titleEn + ", shortDescriptionZh=" + shortDescriptionZh + ", shortDescriptionSc=" + shortDescriptionSc + ", shortDescriptionEn=" + shortDescriptionEn + ", descriptionZh=" + descriptionZh + ", descriptionSc=" + descriptionSc + ", descriptionEn=" + descriptionEn + ", promotionStartDatetime=" + promotionStartDatetime + ", promotionEndDatetime=" + promotionEndDatetime + ", publishStatus=" + publishStatus + ", promotionType=" + promotionType + ", isLuckyDraw=" + isLuckyDraw + ", luckyDrawType=" + luckyDrawType + ", isNew=" + isNew + ", isPublic=" + isPublic + ", gp=" + gp + ", isParticipated="+isParticipated +", createDatetime=" + createDatetime + "]";
	}

	public String getIsLuckyDraw() {
		return isLuckyDraw;
	}

	public void setIsLuckyDraw(String isLuckyDraw) {
		this.isLuckyDraw = isLuckyDraw;
	}

	public String getLuckyDrawType() {
		return luckyDrawType;
	}

	public void setLuckyDrawType(String luckyDrawType) {
		this.luckyDrawType = luckyDrawType;
	}

	public String getCouponStatus() {
		return couponStatus;
	}

	public void setCouponStatus(String couponStatus) {
		this.couponStatus = couponStatus;
	}

	public String getCouponStatusCode() {
		return couponStatusCode;
	}

	public void setCouponStatusCode(String couponStatusCode) {
		this.couponStatusCode = couponStatusCode;
	}

	public String getIsParticipated() {
		return isParticipated;
	}

	public void setIsParticipated(String isParticipated) {
		this.isParticipated = isParticipated;
	}

	public String getCouponSerialNumber() {
		return couponSerialNumber;
	}

	public void setCouponSerialNumber(String couponSerialNumber) {
		this.couponSerialNumber = couponSerialNumber;
	}

	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}
}
