package com.fancl.iloyalty.pojo;

public class ProductChoice {
	private String objectId;
	private String productId;
	private String image;
	private String titleZh;
	private String titleSc;
	private String titleEn;
	
	public ProductChoice(String objectId, String productId, String image, String titleZh,
			String titleSc, String titleEn)
	{
		this.objectId = objectId;
		this.productId = productId;
		this.image = image;
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

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
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

	@Override
	public String toString() {
		return "ProductChoice [productId=" + productId + ", image=" + image + ", titleZh=" + titleZh + ", titleSc=" + titleSc + ", titleEn=" + titleEn + ", objectId=" + objectId + "]";
	}
	
	
}
