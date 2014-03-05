package com.fancl.iloyalty;

public class Constants {
	
	public static final boolean isProductionLogEnable = true;
	
	public static final boolean isDebug = true;
	
	public static final String LOG_TAG = "FancliLoyaltyAndroid";
	
	public static final int HTTP_TIMEOUT 			= 30000;
	
	public static final int HTTP_SOCKET_TIMEOUT 	= 30000;
	
	public static final String GERENAL_STATUS_FAIL_LOCAL_FAIL = "-3";
	
	public static final int IMAGE_THREAD_POOL_MAX_SIZE = 5;
	
	public static final int GENERAL_HTTP_REQUEST_TIMEOUT = 30000;
	
	public static final int LOADING_DIALOG_TIMEOUT = 40000;
	
	public static final int CUSTOM_SPINNER_INTENT = 98765;
	
	public static final int CUSTOM_SPINNER_SIGN_UP_YEAR = 87655;
	public static final int CUSTOM_SPINNER_SIGN_UP_MONTH = 87654;
	public static final int CUSTOM_SPINNER_SIGN_UP_GENDER = 87653;
	public static final int CUSTOM_SPINNER_SIGN_UP_SKIN = 87652;
	public static final int CUSTOM_SPINNER_SIGN_UP_COUNTRY = 87651;	
	public static final int CUSTOM_SPINNER_PRODUCT = 87655;
	
	public static final int PRODUCT_QNA = 90000;
	
	public static final int PROMOTION_LUCKY_DRAW = 92000;
	
	public static final int PURCHASE_QRSCAN = 91000;
	
	public static final int SIGN_UP_BAR_CODE_INTENT	= 43210;
	
	public static final int SIGN_UP_ENTER_MEMBER_ID_INTENT = 123456;
	
	public static final String YOUTUBE_API_KEY = "AIzaSyB0u8LLQwh6tFD9q8fn34aIKjwpoCAKPQY";
	
	public static final int SHARING_PANEL_INTENT = 98887;
	public static final int SHARING_PANEL_RETURN_FB = 98888;
	public static final int SHARING_PANEL_RETURN_TW = 98889;
	public static final int SHARING_PANEL_RETURN_EMAIL = 98890;
	public static final int SHARING_PANEL_RETURN_FAVOURITE = 98891;
	
	/**
	 * Key
	 */
	public static final String CUSTOM_SPINNER_STRING_ARRAY = "customSpinnerStringArray";
	public static final String CUSTOM_SPINNER_RETURN_KEY = "customSpinnerReturn";
	
	public static final String WHATS_HOT_HOT_ITEM_KEY = "hotItem";
	public static final String WHATS_HOT_HOT_ITEM_TYPE_KEY = "hotItemType";
	public static final String BEAUTY_ICNANNEL_MAGAZINE_ITEM_KEY = "ichannelMagazine";
	public static final String SELECTED_SHOP_ITEM_KEY = "selectedShop";
	public static final String PRODUCT_ITEM_KEY = "prouct";
	public static final String PURCHASE_ITEM_KEY = "purchaseHistory";
	public static final String IRECEIPT_FROM_PUSH_ITEM_KEY = "ireceiptFromPush";
	public static final String IRECEIPT_BONUS_POINT_RECORD_ITEM_KEY = "ireceiptBonusPointRecord";
	public static final String PRODUCT_ID_RELATED_KEY = "prouctIdRelated";
	public static final String PROMOTION_ID_RELATED_KEY = "promotionIdRelated";
	public static final String ICHANNEL_ID_RELATED_KEY = "ichannelIdRelated";
	
	public static final String CAPTURE_ACTIVITY_TYPE_KEY = "CaptureActivityType";
	
	public static final String OVERLAY_PURCHASE_TYPE = "purchase";
	public static final String OVERLAY_SIGN_UP_TYPE = "signUp";
	
	public static final String SIGN_UP_BAR_CODE_RETURN_KEY = "signUpBarCodeReturn";
	public static final String SIGN_UP_ENTER_MEMBER_ID_RETURN_KEY = "signUpEnterMemberIdReturn";
	
	public static final String LOGIN_FORM_MEMBER_ID_KEY = "loginFormMemberId";
	
	public static final String QNA_ANSWER_ID = "qnaAnswerId";
	public static final String QNA_ANSWER_CODE = "qnaAnswerCode";
	
	public static final String DETAIL_CONTENT_KEY = "DetailContent";
	public static final String BOTTOM_TAB_INDEX_KEY = "bottom_tab_index";
	public static final String YOUTUBE_LINK_ONLY_KEY = "youtube_link_only";
	public static final String YOUTUBE_LINK_KEY	= "youtube_link";
	public static final String PAGE_TITLE_KEY = "page_title";
	public static final String NO_MORE_RELATED_KEY = "no_more_related";
	
	public static final String MY_FAVOURITE_TYPE_KEY = "myfavouriteType";
	public static final String MY_FAVOURITE_ID_KEY = "myfavouriteId";
	
	public static final String QR_FAVOURITE_TYPE_KEY = "qrfavouriteType";
	public static final String QR_FAVOURITE_ID_KEY = "qrfavouriteId";
	
	public static final String SHARING_PANEL_RETURN_KEY = "sharing_panel_return_key";
	public static final String POS_CODE_KEY	= "pos_code_key";
	
	public static final String SHARING_FORMAT_KEY = "sharing_format_key";
	public static final String SHARING_CONTENT_KEY = "sharing_content_key";
	
	public static final String HOT_ITEM_READ_ARRAY_KEY = "hot_item_read_array_key";
	public static final String PROMOTION_READ_ARRAY_KEY = "promotion_read_array_key";
	public static final String ICHANNEL_READ_ARRAY_KEY = "ichannel_read_array_key";
	
	/**
	 * General Status Code
	 */
	public static final String STATUS_CODE_SUCCESS = "0";

	public static final String STATUS_CODE_FAIL = "1";
	
	/**
	 * Http Connection General Message
	 */
	public static final String DOWNLOAD_RETURN_NULL_GERERAL_MESSAGE = "download_return_null_gereral_message";
	public static final String PARSER_ERROR_GERERAL_MESSAGE = "parser_error_gereral_message";
	
	/**
	 * save path (private folder for this application)
	 * /data/data/[your package path]/files
	 */
	public static final String SAVE_PATH = "/data/data/com.fancl.iloyalty/files/";
	public static final String IMAGE_FOLDER = "/data/data/com.fancl.iloyalty/files/images/";
	public static final String DATABASE_FOLDER = "/data/data/com.fancl.iloyalty/files/database/";
	public static final String WHATS_HOT_HOME_IMAGE_FOLDER = "/data/data/com.fancl.iloyalty/files/images/whats_hot_home/";
	public static final String[] FILE_STRUCTURE_ARRAY = { SAVE_PATH, IMAGE_FOLDER, DATABASE_FOLDER, WHATS_HOT_HOME_IMAGE_FOLDER };
	
	/**
	 * file save name
	 */
	public static final String DATABASE_FILE_NAME = "iloyalty.db";
	public static final String LOG_DATABASE_FILE_NAME = "uselog.db";
	public static final String TILL_ID_DATABASE_FILE_NAME = "tillid.db";
	
	/**
	 * Shared Preference Key
	 */
	public static final String SHARED_PREFERENCE_APPLICATION_KEY = "shared_preference_application_key";
	public static final String SHARED_PREFERENCE_LANGUAGE_SETTING_KEY = "shared_preference_language_setting_key";
	public static final String SHARED_PREFERENCE_UUID_SAVE_KEY = "shared_preference_uuid_save_key";
	public static final String SHARED_PREFERENCE_NOTIFICATION_REGISTRATION_ID_KEY = "shared_preference_notification_registration_id_key";
	public static final String SHARED_PREFERENCE_MEMBER_ID_KEY = "shared_preference_member_id_key";

	
	/**
	 * Http Calling Method ENUM
	 */
	public enum HTTP_CALLING_METHOD
	{
		POST,
		GET
	}
		
	/**
	 * Shared Preference Key
	 */
	public static final String SHARED_PREFERENCE_FIRST_INITIALIZATION_KEY = "first_initialization_key";
	
	/**
	 * Domain and API
	 */
//	public static final String ILOYALTY_SERVER_DOMAIN = "http://202.130.80.205:49777/iloyalty_uat/";			// uat domain
//	public static final String ILOYALTY_SERVER_DOMAIN = "http://202.130.80.205:49888/iloyalty/";				// production domain
	
	/**
	 * User session API
	 */
//	public static final String API_USER_LOGIN = "api/member/login.do";
//	public static final String API_USER_VALIDATE_USER = "api/member/validate.do";
//	public static final String API_USER_REGISTER = "api/member/register.do";
	
	
	/**
	 * GCM
	 */
	public static final String GCM_PROJECT_ID = "187241667025";
	public static final String GCM_SERVER_URL = "http://192.168.1.12:8092/testing";
	public static final String GCM_API_REGISTER = "/api/addNUpdateUser.do";
	public static final String GCM_API_UNREGISTER = "";// "/unregister"
	public static final String GCM_PUSH_NOTIFICATION_MESSAGE = "gcm_push_notification_message";
	public static final String GCM_MESSAGE_RECEIVE_KEY = "message";
	public static final String GCM_PUSH_TYPE_RECEIVE_KEY = "type";
	public static final String GCM_PUSH_ID_RECEIVE_KEY = "id";
	public static boolean isPushAlertDialogDisplaying = false;
}
