package com.fancl.iloyalty.pojo;

public class PromotionCouponType {
	private String titleZh;
	private String titleSc;
	private String titleEn;
	
	public PromotionCouponType(String titleZh, String titleSc, String titleEn)
	{
		this.titleZh = titleZh;
		this.titleSc = titleSc;
		this.titleEn = titleEn;
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
		return "PromotionCouponType [titleZh=" + titleZh + ", titleSc=" + titleSc + ", titleEn=" + titleEn + "]";
	}
	
	
}
