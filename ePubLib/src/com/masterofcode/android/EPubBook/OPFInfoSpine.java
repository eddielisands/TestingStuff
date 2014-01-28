package com.masterofcode.android.EPubBook;

import java.util.ArrayList;

import org.xml.sax.SAXException;

import android.util.Log;

public class OPFInfoSpine {
	private String						tocId = null;
	private	ArrayList<OPFInfoSpineItem>	items = null;
	
	public void addItem(String idRef, String lanscapeShotImagePath, String portraitShotImagePath, boolean fullScreenFlag, boolean coverPageFlag)
	{
		if(items == null)items = new ArrayList<OPFInfoSpineItem>();
		OPFInfoSpineItem		newItem = new OPFInfoSpineItem(idRef, lanscapeShotImagePath, portraitShotImagePath, fullScreenFlag, coverPageFlag);
		items.add(newItem);
	}

	public String getTocId()
	{
		return tocId;
	}

	public void setTocId(String tocId)
	{
		this.tocId = tocId;
	}

	public OPFInfoManifestItem getReferencedManifestItemByIndex(OPFInfoManifest manifest, int index)
	{
		if(index>=0 && index<items.size())
		{
			return manifest.getItemById(items.get(index).getIdRef());
		}
		return null;
	}

	public boolean getTopicFullscreenFlag(int index)
	{
		if(!itemIndexIsValid(index)) return false;
		OPFInfoSpineItem			item = items.get(index);
		return item.isFullScreen();
	}

	public void checkReferencedMediaTypes(OPFInfoManifest manifest)
	{
		ArrayList<String>		invalidSpineItems = new ArrayList<String>();
		
		for(int i=0; i<items.size(); i++)
		{
			OPFInfoManifestItem		cur = manifest.getItemById(items.get(i).getIdRef());
			String					mediaType = cur.getMediaType();
			
			if(!mediaType.equalsIgnoreCase("application/xhtml+xml"))
			{
				if(!mediaType.equalsIgnoreCase("application/x-dtbook+xml"))
				{
					if(!mediaType.equalsIgnoreCase("text/x-oeb1-document"))
					{
						invalidSpineItems.add(items.get(i).getIdRef());
					}
					
				}
			}
		}
		
		if(invalidSpineItems.size()>0)
		{
//			Log.i("!!!", "BAD SPINES");
			for(int i=0; i<invalidSpineItems.size(); i++)
			{
				items.remove(invalidSpineItems.get(i));
			}
			
		}
	}
	
	public void checkToc(OPFInfoManifest manifest) throws SAXException
	{
		String					errMsg = "EPub OPF has invalid 'toc' in spine section";
		
		if (tocId==null) throw new SAXException(errMsg);

		OPFInfoManifestItem		tocItem = manifest.getItemById(tocId);
		
		if (tocItem==null) throw new SAXException(errMsg);
		
//		Log.i("!!!", tocItem.getHref());
	}
	
	public void strictCheck(OPFInfoManifest manifest) throws Exception
	{
	}
	
	public OPFInfoSpineItem getItemWithIndex(int index)
	{
		if(index>=0 && index<items.size()) return items.get(index);
		return null;
	}

	public int itemsSize()
	{
		return items.size();
	}
	
	public boolean itemIndexIsValid(int index)
	{
		if(items==null) return false;
		if(index<0 || index>=items.size())return false;
		return true;
	}
}
