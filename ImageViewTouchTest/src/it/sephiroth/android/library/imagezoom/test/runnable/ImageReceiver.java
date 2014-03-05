package it.sephiroth.android.library.imagezoom.test.runnable;


import it.sephiroth.android.library.imagezoom.test.util.ImageUtil;
import it.sephiroth.android.library.imagezoom.test.util.LogController;

import java.io.File;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageReceiver implements Runnable
{
	protected Context context;
	protected String url;
	protected String savePath;
	protected ImageReceiverCallback imageReceiverCallback;
	protected int position;
	protected boolean saveImage;

	public ImageReceiver(Context context, String url, String savePath, ImageReceiverCallback imageReceiverCallback, int position, boolean saveImage)
	{
		this.context = context;
		this.url = url;
		this.savePath = savePath;
		this.imageReceiverCallback = imageReceiverCallback;
		this.position = position;
		this.saveImage = saveImage;
	}

	@Override
	public void run()
	{		
		LogController.log("CALL IMAGE RUN with url " + url);
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
				
				String fileFullPath = savePath+fileName;
				
				LogController.log("fileFullPath >>> " + fileFullPath);
				
	        	File file = new File(fileFullPath);

	        	LogController.log("file.exists() >>> " + file.exists());
	        	
	        	if (file.exists() == false || saveImage == false) 
	        	{
	        		if (ImageUtil.getFile(context, url, savePath, fileName) == false)
	        		{
	    	        	LogController.log("ImageUtil.getFile == false");

	        			this.updateToView(null, position, url);
	        		}
		        	else {
	    	        	LogController.log("ImageUtil.getFile == true");

			        	bitmap = BitmapFactory.decodeFile(fileFullPath, ImageUtil.getBitmapOptions(1));
			        	this.updateToView(bitmap, position, url);
		        	}
	        	}	
	        	else {
		        	LogController.log("file.exists() == true");

	        		bitmap = BitmapFactory.decodeFile(fileFullPath, ImageUtil.getBitmapOptions(1));
//		        	bitmap = BitmapFactory.decodeFile(fileFullPath);
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