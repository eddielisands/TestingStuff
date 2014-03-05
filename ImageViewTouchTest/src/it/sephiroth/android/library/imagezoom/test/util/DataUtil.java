package it.sephiroth.android.library.imagezoom.test.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.graphics.BitmapFactory;

public class DataUtil {

	public static float px2dip(Context context, int px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (px - 0.5f) / scale;
	}

	public static float dip2px(Context context, int dip) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (dip * scale + 0.5f);
	}
	
	public static int computeSampleSize(String pathName, int maxW, int maxH) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		double w = options.outWidth;
		double h = options.outHeight;
		int sampleSize = (int) Math.ceil(Math.max(w / maxW, h / maxH));
		return sampleSize;
	}
	
	public static boolean checkNeedToUpdateVersion(String oldVersionNumber,
			String[] networkVerList) throws Exception {
		try
		{
			LogController.log("localeVersion " + oldVersionNumber);

			if (networkVerList != null)
			{
				String[] appVerList = oldVersionNumber.split("\\.");

				LogController.log("appVerList.length : " + appVerList.length);
				LogController.log("networkVerList.length : " + networkVerList.length);

				int i;

				int maxSize = appVerList.length > networkVerList.length ? appVerList.length : networkVerList.length;

				if(networkVerList.length > appVerList.length)
				{
					return true;
				}
				else
				{
					int currentAppCompare = 0;
					int currentNetworkCompare = 0;
					
					for (i = 0; i < maxSize; i++)
					{
						currentAppCompare = 0;
						currentNetworkCompare= 0;
						
						if (i < appVerList.length)
						{
							currentAppCompare = Integer.parseInt(appVerList[i]);
						}
						
						if (i < networkVerList.length)
						{
							currentNetworkCompare = Integer.parseInt(networkVerList[i]);
						}
						
						if(currentNetworkCompare > currentAppCompare)
						{
							return true;
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			LogController.log("appCurrentStr Exception " + e.getMessage());
		}

		LogController.log("no need to download database or need to update");
		return false;
	}
	
	public static String dateFormatterConvertion(String datetime) {
		LogController.log("datetime: " + datetime);
		SimpleDateFormat inFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyy/MM/dd");

		try {
			String newDatetime = outFormat.format(inFormat.parse(datetime));
			LogController.log("newDatetime: " + newDatetime);
			return newDatetime;

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String timeFormatterConvertion(String datetime) {
		SimpleDateFormat inFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		SimpleDateFormat outFormat = new SimpleDateFormat("HH:mm");

		try {
			String newDatetime = outFormat.format(inFormat.parse(datetime));
			return newDatetime;

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
