package com.fancl.iloyalty.pojo;

public class GPRewardHistoryItem {
	private String nameZh;
	private String nameSc;
	private String nameEn;
	private String thumbnail;
	private String image;
	private String descriptionZh;
	private String descriptionSc;
	private String descriptionEn;
	private int pointNeed;
	private String shopName;
	private int lineNo;
	private int itemQuantity;
	private float gpAmount;
	
	public GPRewardHistoryItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getNameZh() {
		return nameZh;
	}

	public void setNameZh(String nameZh) {
		this.nameZh = nameZh;
	}

	public String getNameSc() {
		return nameSc;
	}

	public void setNameSc(String nameSc) {
		this.nameSc = nameSc;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
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

	public int getPointNeed() {
		return pointNeed;
	}

	public void setPointNeed(int pointNeed) {
		this.pointNeed = pointNeed;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public int getLineNo() {
		return lineNo;
	}

	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}

	public int getItemQuantity() {
		return itemQuantity;
	}

	public void setItemQuantity(int itemQuantity) {
		this.itemQuantity = itemQuantity;
	}

	public float getGpAmount() {
		return gpAmount;
	}

	public void setGpAmount(float gpAmount) {
		this.gpAmount = gpAmount;
	}

	@Override
	public String toString() {
		return "GPRewardHistoryItem [nameZh=" + nameZh + ", nameSc=" + nameSc
				+ ", nameEn=" + nameEn + ", thumbnail=" + thumbnail
				+ ", image=" + image + ", descriptionZh=" + descriptionZh
				+ ", descriptionSc=" + descriptionSc + ", descriptionEn="
				+ descriptionEn + ", pointNeed=" + pointNeed + ", shopName="
				+ shopName + ", lineNo=" + lineNo + ", itemQuantity="
				+ itemQuantity + ", gpAmount=" + gpAmount + "]";
	}
}
