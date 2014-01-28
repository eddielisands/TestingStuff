package com.masterofcode.android.EPubBook;

import java.util.HashMap;

public class OPFInfoManifest {
	private HashMap<String, OPFInfoManifestItem>	items = null;
	
	public void addItem(String id, String href, String mediaType)
	{
		if(items == null)items = new HashMap<String, OPFInfoManifestItem>(); 
		OPFInfoManifestItem		item = new OPFInfoManifestItem(id, href, mediaType);
		items.put(id, item);
	}

	public OPFInfoManifestItem getItemById(String id)
	{
		return items.get(id);
	}
}
