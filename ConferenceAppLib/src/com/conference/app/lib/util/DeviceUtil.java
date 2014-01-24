package com.conference.app.lib.util;

import java.security.MessageDigest;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class DeviceUtil {
	public static String uuid = null;

	public static boolean isStringEmpty(String string) {
		if (string != null) {
			if (string.trim().length() > 0) {
				return false;
			}
		}
		return true;
	}

	public static String getDeviceUUID(Context context)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences("shared_preference_application_key", Context.MODE_PRIVATE);
		uuid = sharedPreferences.getString("shared_preference_uuid_save_key", null);

		try
		{
			if(isStringEmpty(uuid))
			{
				//get device IMEI first
				TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
				uuid =  telephonyManager.getDeviceId();

				//if cannot get IMEI on device
				//get WiFi MAC address to produce UUID
				if(isStringEmpty(uuid))
				{
					WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
					WifiInfo wifiInf = wifiMan.getConnectionInfo();
					String wiFiMacAddress = wifiInf.getMacAddress();

					if(!isStringEmpty(wiFiMacAddress))
					{
						uuid = DeviceUtil.encrypt(wiFiMacAddress);
					}
				}

				sharedPreferences.edit().putString("shared_preference_uuid_save_key", uuid).commit();
			}
		}
		catch (Exception e) {

		}

		return uuid;
	}

	private static String encrypt(String str) {
		String result = "";
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			result = toHexString(md.digest());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	private static String toHexString(byte[] in) {
		StringBuilder hexString = new StringBuilder();
		for (int i = 0; i < in.length; i++)
		{
			String hex = Integer.toHexString(0xFF & in[i]);
			if (hex.length() == 1)
			{
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}

	public static int getDeviceWidth(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		Point size = new Point();
        display.getSize(size);
        int width = size.x;
		return width;
	}

	public static int getDeviceHeight(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		Point size = new Point();
        display.getSize(size);
        int height = size.y;
		return height;
	}

	public static float getDeviceDenstity(Context context) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return metrics.density;
	}

	public static int px2dip(Context context, int px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) ((px - 0.5f) / scale);
	}

	public static int dip2px(Context context, int dip) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}

	public static Point scaleWithScreenWidth(Context context, Point dip) {
		Point scaleDip = new Point(0, 0);
		scaleDip.x = Math.round((float) dip.x / (float) 320F * (float) DeviceUtil.getDeviceWidth(context));
		scaleDip.y = Math.round((float) scaleDip.x / (float) dip.x * (float) dip.y);
		return scaleDip;
	}

	public static Point scaleWithScreenHeight(Context context, Point dip) {
		Point scaleDip = new Point(0, 0);
		scaleDip.y = Math.round((float) dip.y / (float) 320F * (float) DeviceUtil.getDeviceHeight(context));
		scaleDip.x = Math.round((float) scaleDip.y / (float) dip.y * (float) dip.x);
		return scaleDip;
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
}
