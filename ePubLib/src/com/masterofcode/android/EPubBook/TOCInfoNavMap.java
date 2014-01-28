package com.masterofcode.android.EPubBook;

import java.util.ArrayList;

public class TOCInfoNavMap {
	private ArrayList<TOCInfoNavPoint>	navPoints = null;
	
	public TOCInfoNavMap() {
		navPoints = new ArrayList<TOCInfoNavPoint>();
	}

	public ArrayList<TOCInfoNavPoint> getNavPoints()
	{
		return navPoints;
	}
	
	public void addNavPoint(TOCInfoNavPoint child)
	{
		navPoints.add(child);
	}

}
