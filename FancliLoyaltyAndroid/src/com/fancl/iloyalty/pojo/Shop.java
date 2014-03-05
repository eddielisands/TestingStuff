package com.fancl.iloyalty.pojo;

import java.io.Serializable;

public class Shop implements Serializable {
	private static final long serialVersionUID = 460096002830880618L;
	private String objectId;
	private String code;
	private String regionId;
	private String type;
	private String image;
	private String titleZh;
	private String titleSc;
	private String titleEn;
	private String addressZh;
	private String addressSc;
	private String addressEn;
	private String phoneNumber;
	private String officeHourZh;
	private String officeHourSc;
	private String officeHourEn;
	private String latitude;
	private String longitude;
	private String publishStatus;
	private String isNew;
	private String createDatetime;
	
	public Shop(String objectId, String code, String regionId, String type, String image,
			String titleZh, String titleSc, String titleEn, String addressZh,
			String addressSc, String addressEn, String phoneNumber,
			String officeHourZh, String officeHourSc, String officeHourEn,
			String latitude, String longitude, String publishStatus,
			String isNew, String createDatetime)
	{
		this.objectId = objectId;
		this.code = code;
		this.regionId = regionId;
		this.type = type;
		this.image = image;
		this.titleZh = titleZh;
		this.titleSc = titleSc;
		this.titleEn = titleEn;
		this.addressZh = addressZh;
		this.addressSc = addressSc;
		this.addressEn = addressEn;
		this.phoneNumber = phoneNumber;
		this.officeHourZh = officeHourZh;
		this.officeHourSc = officeHourSc;
		this.officeHourEn = officeHourEn;
		this.latitude = latitude;
		this.longitude = longitude;
		this.publishStatus = publishStatus;
		this.isNew = isNew;
		this.createDatetime = createDatetime;
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

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public void setTitleEn(String titelEn) {
		this.titleEn = titelEn;
	}

	public String getAddressZh() {
		return addressZh;
	}

	public void setAddressZh(String addressZh) {
		this.addressZh = addressZh;
	}

	public String getAddressSc() {
		return addressSc;
	}

	public void setAddressSc(String addressSc) {
		this.addressSc = addressSc;
	}

	public String getAddressEn() {
		return addressEn;
	}

	public void setAddressEn(String addressEn) {
		this.addressEn = addressEn;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getOfficeHourZh() {
		return officeHourZh;
	}

	public void setOfficeHourZh(String officeHourZh) {
		this.officeHourZh = officeHourZh;
	}

	public String getOfficeHourSc() {
		return officeHourSc;
	}

	public void setOfficeHourSc(String officeHourSc) {
		this.officeHourSc = officeHourSc;
	}

	public String getOfficeHourEn() {
		return officeHourEn;
	}

	public void setOfficeHourEn(String officeHourEn) {
		this.officeHourEn = officeHourEn;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
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

	public String getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
	}

	@Override
	public String toString() {
		return "Shop [code=" + code + ", regionId=" + regionId + ", type=" + type + ", image=" + image + ", titleZh=" + titleZh + ", titleSc=" + titleSc + ", titleEn=" + titleEn + ", addressZh=" + addressZh + ", addressSc=" + addressSc + ", addressEn=" + addressEn + ", phoneNumber=" + phoneNumber + ", officeHourZh=" + officeHourZh + ", officeHourSc=" + officeHourSc + ", officeHourEn=" + officeHourEn + ", latitude=" + latitude + ", longitude=" + longitude + ", publishStatus=" + publishStatus + ", isNew=" + isNew + ", createDatetime=" + createDatetime + ", objectId=" + objectId + "]";
	}
	
	
}
