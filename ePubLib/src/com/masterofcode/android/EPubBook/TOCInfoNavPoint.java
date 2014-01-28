package com.masterofcode.android.EPubBook;

import java.util.ArrayList;

public class TOCInfoNavPoint {
	private String						id = null;
	private int							playOrder = -1;
	private String						navLabel = null;
	private String						contentSrc = null;
	private ArrayList<TOCInfoNavPoint>	childs = null;
	private TOCInfoNavPoint				parent = null;
	
	public TOCInfoNavPoint() {
		childs = new ArrayList<TOCInfoNavPoint>();
	}

	public String getId()
	{
		return id;
	}
	
	public int getPlayOrder()
	{
		return playOrder;
	}
	
	public String getNavLabel()
	{
		return navLabel;
	}
	
	public String getContentSrc()
	{
		return contentSrc;
	}
	
	public ArrayList<TOCInfoNavPoint> getChilds()
	{
		return childs;
	}
	
	public TOCInfoNavPoint getParent()
	{
		return parent;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	public void setPlayOrder(int playOrder)
	{
		this.playOrder = playOrder;
	}
	
	public void setNavLabel(String navLabel)
	{
		this.navLabel = navLabel;
	}
	
	public void setContentSrc(String contentSrc)
	{
		this.contentSrc = contentSrc;
	}

	public void setParent(TOCInfoNavPoint parent)
	{
		this.parent = parent;
	}
	
	public void addChild(TOCInfoNavPoint child)
	{
		childs.add(child);
	}
}
