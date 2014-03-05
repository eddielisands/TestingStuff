package com.fancl.iloyalty.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;

import com.fancl.iloyalty.AndroidProjectApplication;

public class DataUtil {

	public static float px2dip(Context context, int px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (px - 0.5f) / scale;
	}

	public static float dip2px(Context context, int dip) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (dip * scale + 0.5f);
	}

	public static int px2integerDip (Context context, int px) {
		return Math.round(px2dip(context, px));
	}

	public static int dip2integerPx (Context context, int dip) {
		return Math.round(dip2px(context, dip));
	}

	public static String convertImageName(String image) {
		String imageName = image;
		if (imageName != null) {
			String[] tmpImageString = imageName.split("\\.");

			if (tmpImageString.length > 1) {
				if (DeviceUtil.getDeviceDenstity(AndroidProjectApplication.application) >= 1.5) {
					imageName = tmpImageString[0] + "@2x." + tmpImageString[1];
				}
				else {
					imageName = tmpImageString[0] + "." + tmpImageString[1];
				}
			}
		}

		return imageName;
	}
	
	public static String convertToThumbnailImageName(String image) {
		String imageName = image;
		if (imageName != null) {
			String[] tmpImageString = imageName.split("\\.");

			imageName = tmpImageString[0] + "_thumbnail." + tmpImageString[1];
		}

		return imageName;
	}
	
	public static String getVideoIdFromLink(String youtubeLink) {
		String[] videoPathList = youtubeLink.split("\\?");
		String videoLastPart = videoPathList[videoPathList.length-1];
		String[] videoIdPartList = videoLastPart.split("\\&");
		String videoIdPart = videoIdPartList[0];
		String[] videoIdList = videoIdPart.split("\\=");
		String tmpVideoId = videoIdList[videoIdList.length-1];
		return tmpVideoId;
	}
	
	public static String trimDateString(String fullDatetime) {
		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
	    Date date = new Date();
	    Calendar calendar;

	    try {
	    	date = form.parse(fullDatetime);
	    } catch (java.text.ParseException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }

	    calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    
	    String convertedDate = "";
	    convertedDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
	    return convertedDate;
		
	}
	
	public static String convertDateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = sdf.format(date);
		return dateString;
	}
	
	public static String convertDateToStringAddEight(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		date.setHours(date.getHours() + 8);
		String dateString = sdf.format(date);
		return dateString;
	}

}