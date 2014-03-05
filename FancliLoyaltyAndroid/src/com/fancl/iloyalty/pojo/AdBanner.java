package com.fancl.iloyalty.pojo;

public class AdBanner {
	private String objectId;
	private String itemId;
	private String itemType;
	private String imageZh;
	private String imageSc;
	private String imageEn;
	private String image5Zh;
	private String image5Sc;
	private String image5En;
	private String linkZh;
	private String linkSc;
	private String linkEn;
	private String hitRate;
	
	public AdBanner(String objectId, String itemId, String itemType,
			String imageZh, String imageSc, String imageEn, String image5Zh,
			String image5Sc, String image5En, String linkZh, String linkSc,
			String linkEn, String hitRate) {
		super();
		this.objectId = objectId;
		this.itemId = itemId;
		this.itemType = itemType;
		this.imageZh = imageZh;
		this.imageSc = imageSc;
		this.imageEn = imageEn;
		this.image5Zh = image5Zh;
		this.image5Sc = image5Sc;
		this.image5En = image5En;
		this.linkZh = linkZh;
		this.linkSc = linkSc;
		this.linkEn = linkEn;
		this.hitRate = hitRate;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getImageZh() {
		return imageZh;
	}

	public void setImageZh(String imageZh) {
		this.imageZh = imageZh;
	}

	public String getImageSc() {
		return imageSc;
	}

	public void setImageSc(String imageSc) {
		this.imageSc = imageSc;
	}

	public String getImageEn() {
		return imageEn;
	}

	public void setImageEn(String imageEn) {
		this.imageEn = imageEn;
	}

	public String getImage5Zh() {
		return image5Zh;
	}

	public void setImage5Zh(String image5Zh) {
		this.image5Zh = image5Zh;
	}

	public String getImage5Sc() {
		return image5Sc;
	}

	public void setImage5Sc(String image5Sc) {
		this.image5Sc = image5Sc;
	}

	public String getImage5En() {
		return image5En;
	}

	public void setImage5En(String image5En) {
		this.image5En = image5En;
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

	public String getHitRate() {
		return hitRate;
	}

	public void setHitRate(String hitRate) {
		this.hitRate = hitRate;
	}

	@Override
	public String toString() {
		return "AdBanner [objectId=" + objectId + ", itemId=" + itemId
				+ ", itemType=" + itemType + ", imageZh=" + imageZh
				+ ", imageSc=" + imageSc + ", imageEn=" + imageEn
				+ ", image5Zh=" + image5Zh + ", image5Sc=" + image5Sc
				+ ", image5En=" + image5En + ", linkZh=" + linkZh + ", linkSc="
				+ linkSc + ", linkEn=" + linkEn + ", hitRate=" + hitRate + "]";
	}

}
