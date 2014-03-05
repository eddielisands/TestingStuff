package com.fancl.iloyalty.pojo;

public class ProductSeries {
	private String id;;
	private String titleZh;
	private String titleSc;
	private String titleEn;
	
	public ProductSeries(String id, String titleZh, String titleSc, String titleEn)
	{
		this.id = id;
		this.titleZh = titleZh;
		this.titleSc = titleSc;
		this.titleEn = titleEn;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
		return "ProductSeries [titleZh=" + titleZh + ", titleSc=" + titleSc + ", titleEn=" + titleEn +", id=" + id + "]";
	}
	
	
}
