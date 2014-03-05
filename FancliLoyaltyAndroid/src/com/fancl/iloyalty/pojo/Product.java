package com.fancl.iloyalty.pojo;

import java.io.Serializable;

public class Product implements Serializable {
	private static final long serialVersionUID = 460096002830880618L;

	private String objectId;
	private String seriesId;
	private String thumbnail;
	private String image;
	private String titleZh;
	private String titleSc;
	private String titleEn;
	private String benefitZh;
	private String benefitSc;
	private String benefitEn;
	private String sizeZh;
	private String sizeSc;
	private String sizeEn;
	private String ingredientZh;
	private String ingredientSc;
	private String ingredientEn;
	private String howToUseZh;
	private String howToUseSc;
	private String howToUseEn;
	private String volumnZh;
	private String volumnSc;
	private String volumnEn;
	private String publishStatus;
	private String isNew;
	private String isSeason;
	private String isRead;
	private String createDatetime;

	public Product(String objectId, String seriesId, String thumbnail, String image,
			String titleZh, String titleSc, String titleEn, String benefitZh,
			String benefitSc, String benefitEn, String sizeZh, String sizeSc,
			String sizeEn, String ingredientZh, String ingredientSc,
			String ingredientEn, String howToUseZh, String howToUseSc,
			String howToUseEn, String volumnZh, String volumnSc,
			String volumnEn, String publishStatus, String isNew,
			String isSeason, String isRead, String createDatetime)
	{
		this.objectId = objectId;
		this.seriesId = seriesId;
		this.thumbnail = thumbnail;
		this.image = image;
		this.titleZh = titleZh;
		this.titleSc = titleSc;
		this.titleEn = titleEn;
		this.benefitZh = benefitZh;
		this.benefitSc = benefitSc;
		this.benefitEn = benefitEn;
		this.sizeZh = sizeZh;
		this.sizeSc = sizeSc;
		this.sizeEn = sizeEn;
		this.ingredientZh = ingredientZh;
		this.ingredientSc = ingredientSc;
		this.ingredientEn = ingredientEn;
		this.howToUseZh = howToUseZh;
		this.howToUseSc = howToUseSc;
		this.howToUseEn = howToUseEn;
		this.volumnZh = volumnZh;
		this.volumnSc = volumnSc;
		this.volumnEn = volumnEn;
		this.publishStatus = publishStatus;
		this.isNew = isNew;
		this.isSeason = isSeason;
		this.isRead = isRead;
		this.createDatetime = createDatetime;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getSeriesId() {
		return seriesId;
	}

	public void setSeriesId(String seriesId) {
		this.seriesId = seriesId;
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

	public String getBenefitZh() {
		return benefitZh;
	}

	public void setBenefitZh(String benefitZh) {
		this.benefitZh = benefitZh;
	}

	public String getBenefitSc() {
		return benefitSc;
	}

	public void setBenefitSc(String benefitSc) {
		this.benefitSc = benefitSc;
	}

	public String getBenefitEn() {
		return benefitEn;
	}

	public void setBenefitEn(String benefitEn) {
		this.benefitEn = benefitEn;
	}

	public String getSizeZh() {
		return sizeZh;
	}

	public void setSizeZh(String sizeZh) {
		this.sizeZh = sizeZh;
	}

	public String getSizeSc() {
		return sizeSc;
	}

	public void setSizeSc(String sizeSc) {
		this.sizeSc = sizeSc;
	}

	public String getSizeEn() {
		return sizeEn;
	}

	public void setSizeEn(String sizeEn) {
		this.sizeEn = sizeEn;
	}

	public String getIngredientZh() {
		return ingredientZh;
	}

	public void setIngredientZh(String ingredientZh) {
		this.ingredientZh = ingredientZh;
	}

	public String getIngredientSc() {
		return ingredientSc;
	}

	public void setIngredientSc(String ingredientSc) {
		this.ingredientSc = ingredientSc;
	}

	public String getIngredientEn() {
		return ingredientEn;
	}

	public void setIngredientEn(String ingredientEn) {
		this.ingredientEn = ingredientEn;
	}

	public String getHowToUseZh() {
		return howToUseZh;
	}

	public void setHowToUseZh(String howToUseZh) {
		this.howToUseZh = howToUseZh;
	}

	public String getHowToUseSc() {
		return howToUseSc;
	}

	public void setHowToUseSc(String howToUseSc) {
		this.howToUseSc = howToUseSc;
	}

	public String getHowToUseEn() {
		return howToUseEn;
	}

	public void setHowToUseEn(String howToUseEn) {
		this.howToUseEn = howToUseEn;
	}

	public String getVolumnZh() {
		return volumnZh;
	}

	public void setVolumnZh(String volumnZh) {
		this.volumnZh = volumnZh;
	}

	public String getVolumnSc() {
		return volumnSc;
	}

	public void setVolumnSc(String volumnSc) {
		this.volumnSc = volumnSc;
	}

	public String getVolumnEn() {
		return volumnEn;
	}

	public void setVolumnEn(String volumnEn) {
		this.volumnEn = volumnEn;
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

	public String getIsSeason() {
		return isSeason;
	}

	public void setIsSeason(String isSeason) {
		this.isSeason = isSeason;
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
		return "Product [seriesId=" + seriesId + ", objectId=" + objectId + ", thumbnail=" + thumbnail + ", image=" + image + ", titleZh=" + titleZh + ", titleSc=" + titleSc + ", titleEn=" + titleEn + ", benefitZh=" + benefitZh + ", benefitSc=" + benefitSc + ", benefitEn=" + benefitEn + ", sizeZh=" + sizeZh + ", sizeSc=" + sizeSc + ", sizeEn=" + sizeEn + ", ingredientZh=" + ingredientZh + ", ingredientSc=" + ingredientSc + ", ingredientEn=" + ingredientEn + ", howToUseZh=" + howToUseZh + ", howToUseSc=" + howToUseSc + ", howToUseEn=" + howToUseEn + ", volumnZh=" + volumnZh + ", volumnSc=" + volumnSc + ", volumnEn=" + volumnEn + ", publishStatus=" + publishStatus + ", isNew=" + isNew + ", isSeason=" + isSeason + ", isRead=" + isRead + ", createDatetime=" + createDatetime + "]";
	}


}
