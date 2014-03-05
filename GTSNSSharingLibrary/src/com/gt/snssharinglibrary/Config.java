package com.gt.snssharinglibrary;

public class Config {

	// Config for show/not show debug log
	public static boolean DEBUG_LOGGER = true;

	// Config for show/not show production log
	public static boolean PRODUCTION_LOGGER = true;

	// Config for log cat tag
	public static String LOG_TAG = "GT-SNS-Sharing";

	// Config for show/not show the loading dialog
	public static boolean SHOW_LOADING_DIALOG = true;

	// Sharing
	public enum SHARING_TYPE
	{
		WEB_DIALOG, PLAIN_TEXT
	}

	/* Facebook 2.0 */
	public static String FACEBOOK_2_0_APP_ID = "";

	public static String[] FACEBOOK_2_0_PERMISSIONS = new String[] {};

	public static int FACEBOOK_2_0_DIALOG_CLOSE_BTN_RESOURCES_ID = 0;

	/* Facebook 3.0 */
	public static SHARING_TYPE facebook30SharingType = SHARING_TYPE.WEB_DIALOG;

	public static String FACEBOOK_FEEDER_ID = "";

	public static String[] FACEBOOK_PERMISSIONS = new String[] {};

	/* Weibo Sharing */
	public static SHARING_TYPE weiboSharingType = SHARING_TYPE.PLAIN_TEXT;

	public static String WEIBO_FEEDER_ID = "";

	public static String WEIBO_CONSUMER_KEY = "";

	public static String WEIBO_CONSUMER_SECRET = "";

	public static String WEIBO_REDIRECT_URL = "";

	/* Twitter Sharing */
	public static String TWITTER_CONSUMER_KEY = "";

	public static String TWITTER_CONSUMER_SECRET = "";

	public static int TWITTER_DIALOG_CLOSE_BTN_RESOURCES_ID = 0;

	public static boolean REQUEST_NEW_PREMISSION_AFTER_GET_PROFILE = false;

	/* Email Sharing */
	public static String EMAIL_SHARE_LABEL = "Email";

}
