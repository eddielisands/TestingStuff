package com.masterofcode.android.EPubBook;

import java.util.ArrayList;

public class OPFInfoGuide {
	private	ArrayList<OPFInfoGuideItem>		items = null;

	public OPFInfoGuide()
	{
	}

	public void addItem(String type, String title, String href)
	{
		if(items == null)items = new ArrayList<OPFInfoGuideItem>();
		OPFInfoGuideItem	item = new OPFInfoGuideItem(type, title, href);
		items.add(item);
	}

}
