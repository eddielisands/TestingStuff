package com.fancl.iloyalty.pojo;

public class MagazineImage {
	private String objectId;
	private String magazineId;
	private String imageZh;
	private String imageSc;
	private String imageEn;
	private String thumbnail_Zh;
	private String thumbnail_Sc;
	private String thumbnail_En;
	
	public MagazineImage(String objectId, String magazineId, String imageZh,
			String imageSc, String imageEn, String thumbnail_Zh, String thumbnail_Sc,
			String thumbnail_En) {
		super();
		this.objectId = objectId;
		this.magazineId = magazineId;
		
		this.imageZh = imageZh;
		this.imageSc = imageSc;
		this.imageEn = imageEn;
		
		this.thumbnail_Zh = thumbnail_Zh;
		this.thumbnail_Sc = thumbnail_Sc;
		this.thumbnail_En = thumbnail_En;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getMagazineId() {
		return magazineId;
	}

	public void setMagazineId(String magazineId) {
		this.magazineId = magazineId;
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

	public String getThumbnail_Zh() {
		return thumbnail_Zh;
	}

	public void setThumbnail_Zh(String thumbnail_Zh) {
		this.thumbnail_Zh = thumbnail_Zh;
	}

	public String getThumbnail_Sc() {
		return thumbnail_Sc;
	}

	public void setThumbnail_Sc(String thumbnail_Sc) {
		this.thumbnail_Sc = thumbnail_Sc;
	}

	public String getThumbnail_En() {
		return thumbnail_En;
	}

	public void setThumbnail_En(String thumbnail_En) {
		this.thumbnail_En = thumbnail_En;
	}
}
