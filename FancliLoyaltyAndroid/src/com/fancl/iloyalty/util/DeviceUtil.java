package com.fancl.iloyalty.util;

import java.security.MessageDigest;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.fancl.iloyalty.Constants;

public class DeviceUtil {
	public static String uuid = null;

	public static String getDeviceUUID(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				Constants.SHARED_PREFERENCE_APPLICATION_KEY,
				Context.MODE_PRIVATE);
		uuid = sharedPreferences.getString(
				Constants.SHARED_PREFERENCE_UUID_SAVE_KEY, null);

		try {
			if (StringUtil.isStringEmpty(uuid)) {
				// get device IMEI first
				TelephonyManager telephonyManager = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				uuid = telephonyManager.getDeviceId();

				// if cannot get IMEI on device
				// get WiFi MAC address to produce UUID
				if (StringUtil.isStringEmpty(uuid)) {
					WifiManager wifiMan = (WifiManager) context
							.getSystemService(Context.WIFI_SERVICE);
					WifiInfo wifiInf = wifiMan.getConnectionInfo();
					String wiFiMacAddress = wifiInf.getMacAddress();

					if (!StringUtil.isStringEmpty(wiFiMacAddress)) {
						uuid = DeviceUtil.encrypt(wiFiMacAddress);
					}
				}

				sharedPreferences
						.edit()
						.putString(Constants.SHARED_PREFERENCE_UUID_SAVE_KEY,
								uuid).commit();
			}
		} catch (Exception e) {

		}

		return uuid;
	}

	private static String encrypt(String str) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			result = toHexString(md.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private static String toHexString(byte[] in) {
		StringBuilder hexString = new StringBuilder();
		for (int i = 0; i < in.length; i++) {
			String hex = Integer.toHexString(0xFF & in[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}

	public static int getDeviceWidth(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		return display.getWidth();
	}

	public static int getDeviceHeight(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		return display.getHeight();
	}
	
	public static float getDeviceDenstity(Context context) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return metrics.density;
	}
}
