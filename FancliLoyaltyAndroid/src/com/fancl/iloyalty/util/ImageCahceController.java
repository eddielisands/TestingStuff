package com.fancl.iloyalty.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;

public class ImageCahceController {

	private int maxThumbnailCacheSize = 60;
	
	private Map<String, Bitmap> thumbnailCacheList = new HashMap<String, Bitmap>();
	
	private List<String> thumbnailCacheKeyList = new ArrayList<String>();
	
	public ImageCahceController(int maxThumbnailCacheSize) {
		this.maxThumbnailCacheSize = maxThumbnailCacheSize;
	}
	
	public synchronized void thumbnailCacheController(String url, Bitmap bitmap)
	{
		synchronized (this.thumbnailCacheList) {
			
			this.thumbnailCacheList.put(url, bitmap);
			this.thumbnailCacheKeyList.add(url);
			
			if(this.thumbnailCacheList.size() > this.maxThumbnailCacheSize)
			{
				try
				{
					for(int i = 0 ; i < this.thumbnailCacheList.size()-this.maxThumbnailCacheSize ; i++)
					{
						String olderUrl = this.thumbnailCacheKeyList.get(i);
						this.thumbnailCacheList.remove(olderUrl);
						this.thumbnailCacheKeyList.remove(i);
					}
				}
				catch(Exception e)
				{
					
				}
			}
		}
	}
	
	public synchronized void clearThumbnailCache()
	{
		this.thumbnailCacheList.clear();
		this.thumbnailCacheKeyList.clear();
	}
	
	public boolean isThumbnailCache(String url)
	{
		return this.thumbnailCacheList.containsKey(url);
	}
	
	public Bitmap getThumbnailCache(String url)
	{
		return this.thumbnailCacheList.get(url);
	}
	
	public int getThumbnailSize()
	{
		return this.thumbnailCacheList.size();
	}
	
	public void setThumbnailCacheLimit(int limitSize)
	{
		if(limitSize > 0)
		{
			this.maxThumbnailCacheSize = limitSize+2;
		}
	}
	
}
