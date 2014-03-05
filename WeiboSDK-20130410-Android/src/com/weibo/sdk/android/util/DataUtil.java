package com.weibo.sdk.android.util;

import android.content.Context;

public class DataUtil {
	public static float dip2px(Context context, int dip) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (dip * scale + 0.5f);
	}
}
