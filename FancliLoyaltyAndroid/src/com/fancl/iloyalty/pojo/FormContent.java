package com.fancl.iloyalty.pojo;

public class FormContent {
	private String code;
	private String titleZh;
	private String titleSc;
	private String titleEn;
	
	public FormContent(String code, String titleZh, String titleSc,
			String titleEn)
	{
		this.code = code;
		this.titleZh = titleZh;
		this.titleSc = titleSc;
		this.titleEn = titleEn;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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
		return "FormContent [code=" + code + ", titleZh=" + titleZh + ", titleSc=" + titleSc + ", titleEn=" + titleEn + "]";
	}
}
