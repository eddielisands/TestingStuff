package com.gt.snssharinglibrary.util;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

public class Util {
	public static void clearCookies(Context context)
	{
		@SuppressWarnings("unused")
		CookieSyncManager cookieSyncMngr =
	            CookieSyncManager.createInstance(context);
	        CookieManager cookieManager = CookieManager.getInstance();
	        cookieManager.removeAllCookie();
	}
}
