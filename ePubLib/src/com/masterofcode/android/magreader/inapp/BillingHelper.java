package com.masterofcode.android.magreader.inapp;

import android.content.Context;

import com.masterofcode.android.magreader.inapp.util.IabHelper;

public class BillingHelper {
	
	private static IabHelper mHelper;
	private static Context mContext;

	public static void instantiateHelper(Context context, IabHelper helper) {
		mHelper = helper;
		mContext = context;
	}
	
	public static IabHelper getIabHelper(){
		return mHelper;
	}

}
