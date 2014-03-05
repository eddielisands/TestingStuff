package com.fancl.iloyalty.runnable;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.fancl.iloyalty.util.ImageUtil;
import com.fancl.iloyalty.util.LogController;

public class ImageReceiver implements Runnable
{
	protected Context context;
	protected String url;
	protected String savePath;
	protected ImageReceiverCallback imageReceiverCallback;
	protected int position;
	protected boolean saveImage;
	protected boolean isYoutubeThumbnail;

	public ImageReceiver(Context context, String url, String savePath, ImageReceiverCallback imageReceiverCallback, int position, boolean saveImage)
	{
		this.context = context;
		this.url = url;
		this.savePath = savePath;
		this.imageReceiverCallback = imageReceiverCallback;
		this.position = position;
		this.saveImage = saveImage;
		this.isYoutubeThumbnail = false;
	}
	
	public ImageReceiver(Context context, String url, String savePath, ImageReceiverCallback imageReceiverCallback, int position, boolean saveImage, boolean isYoutubeThumbnail)
	{
		this.context = context;
		this.url = url;
		this.savePath = savePath;
		this.imageReceiverCallback = imageReceiverCallback;
		this.position = position;
		this.saveImage = saveImage;
		this.isYoutubeThumbnail = isYoutubeThumbnail;
	}

	public void run()
	{		
		try
		{
			if (url == null)
			{
				this.updateToView(null, position, url);
			}	
			else if (url.equals(""))
			{
				this.updateToView(null, position, url);
			}
			else 
			{
				Bitmap bitmap = null;
				String fileName = url.substring(url.lastIndexOf("/")+1);
				
				if (isYoutubeThumbnail) {
					String[] urlStringList = url.split("/");
					String videoId = urlStringList[urlStringList.length-2];
					fileName = videoId + fileName;
				}
				
				String fileFullPath = savePath+fileName;
				
				LogController.log("fileFullPath >>> " + fileFullPath);
				
	        	File file = new File(fileFullPath);

	        	LogController.log("file.exists() >>> " + file.exists());
	        	
	        	if (file.exists() == false || saveImage == false) 
	        	{
	        		if (ImageUtil.getFile(context, url, savePath, fileName) == false)
	        		{
	        			this.updateToView(null, position, url);
	        		}
		        	else {
			        	bitmap = BitmapFactory.decodeFile(fileFullPath);
			        	this.updateToView(bitmap, position, url);
		        	}
	        	}	
	        	else {
		        	bitmap = BitmapFactory.decodeFile(fileFullPath);
					this.updateToView(bitmap, position, url);
	        	}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void updateToView(Bitmap bitmap, int position, String url)
	{
		if(imageReceiverCallback != null)
		{
			imageReceiverCallback.updateToView(bitmap, position, url);
		}
	}
}