package it.sephiroth.android.library.imagezoom.test.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;

public class ImageUtil {
	
	private static final String SUB_TAG = "ImageUtil";
	
	private static void log(String message)
	{
//		LogController.log(SUB_TAG + " >>> " + message);
	}
	
	public static boolean getFile(Context context, String url, String path, String fileName) {
		boolean success = false;
		InputStream in = null;
		FileOutputStream fos = null;
		
		try {
			LogController.log("getFile Bitmap from: " + url);
			
			URL u = new URL(url);
			HttpURLConnection c = (HttpURLConnection) u.openConnection();
			c.setConnectTimeout(20000);
			c.setReadTimeout(20000);
	        c.setRequestMethod("GET");
	    	c.connect();
	    	in = c.getInputStream();	
	    	
	    	fos = new FileOutputStream(path+fileName);
	    	
	    	int length = 256;
	    	byte[] buffer = new byte[length];
	    	int size = in.read(buffer, 0, length);
	    	while (size != -1)
	    	{       	
	        	fos.write(buffer, 0, size);
	        	buffer = new byte[length];
	        	size = in.read(buffer, 0, length);
	    	}
	    	success = true;
		} catch (Exception e) {
			e.printStackTrace();
			LogController.log("Could not load Bitmap from: " + url);
	    }		
		
		if (fos != null)
		{
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
		if (in != null)
		{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}
		
		return success;
	}
	
	public static Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		} else if (drawable instanceof NinePatchDrawable) {
			Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
					drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
							: Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			drawable.draw(canvas);
			return bitmap;
		} else {
			return null;
		}
	}
	
	public static Bitmap drawableToBitmap(Drawable drawable, int width, int height) {
		Bitmap bitmap = Bitmap.createBitmap(width, height, 
				drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;
	}
	
	public static BitmapFactory.Options getBitmapOptions(int scale){
		   BitmapFactory.Options options = new BitmapFactory.Options();
		   options.inPurgeable = true;
		   options.inInputShareable = true;
		   options.inSampleSize = scale;
		   return options;
	}
	
	public static int computeSampleSize(Resources resource, int resourceId, int maxW, int maxH) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(resource, resourceId, options);
		double w = options.outWidth;
		double h = options.outHeight;
		int sampleSize = (int) Math.ceil(Math.max(w / maxW, h / maxH));
		return sampleSize;
	}
}
