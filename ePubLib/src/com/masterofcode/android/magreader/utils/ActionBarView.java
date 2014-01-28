package com.masterofcode.android.magreader.utils;

import android.app.ActionBar;
import android.content.Context;

import com.masterofcode.android.magreader.utils.constants.Constants;
import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class ActionBarView {
	
	public static void setActionBarTabView(ActionBar actionBar, Context context) {
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_USE_LOGO & ActionBar.DISPLAY_SHOW_TITLE);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.actionbar_background));
		actionBar.setTitle("");
		actionBar.setSubtitle("Last Update: " + ApplicationUtils.getPreferences(context).getString(Constants.DATE_FEEDS_UPDATE, null));
	}
	
	public static void setActionBarListView(ActionBar actionBar, Context context) {
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_USE_LOGO & ActionBar.DISPLAY_SHOW_TITLE);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.actionbar_background));
		actionBar.setTitle("");
		actionBar.setSubtitle("Last Update: " + ApplicationUtils.getPreferences(context).getString(Constants.DATE_FEEDS_UPDATE, null));
	}
	
	public static void setActionBarStandartView(ActionBar actionBar, Context context) {
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_USE_LOGO & ActionBar.DISPLAY_SHOW_TITLE);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.actionbar_background));
		actionBar.setTitle("");
		actionBar.setSubtitle("Last Update: " + ApplicationUtils.getPreferences(context).getString(Constants.DATE_FEEDS_UPDATE, null));
	}
}
