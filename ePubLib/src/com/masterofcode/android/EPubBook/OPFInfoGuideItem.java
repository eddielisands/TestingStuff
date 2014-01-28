package com.masterofcode.android.EPubBook;

public class OPFInfoGuideItem {
	private String		type = null;
	private String		title = null;
	private String		href = null;

	public OPFInfoGuideItem()
	{
	}

	public OPFInfoGuideItem(String type, String title, String href) {
		this.type = type;
		this.title = title;
		this.href = href;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getReference()
	{
		return href;
	}

	public void setReference(String href)
	{
		this.href = href;
	}

}
