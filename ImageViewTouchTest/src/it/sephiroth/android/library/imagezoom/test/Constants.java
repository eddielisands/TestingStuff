package it.sephiroth.android.library.imagezoom.test;

public class Constants {
	
	public static final boolean isProductionLogEnable = true;
	
	public static final boolean isDebug = true;
	
	public static final String LOG_TAG = "Image";
	
	public static final int HTTP_TIMEOUT 			= 30000;
	
	public static final int HTTP_SOCKET_TIMEOUT 	= 30000;
	
	public static final String GERENAL_STATUS_FAIL_LOCAL_FAIL = "-3";
	
	public static final int IMAGE_THREAD_POOL_MAX_SIZE = 5;
	
	public static final int GENERAL_HTTP_REQUEST_TIMEOUT = 30000;
	
	public static final String noOfFeeds = "10";
	
	/**
	 * Offer Grade
	 */
	public static final String GRADE_PLATINUM = "CCP";

	public static final String GRADE_GOLD = "CCG";
	
	public static final String GRADE_SILVER = "CCS";
	
	public static final String GRADE_NON_MEMBER = "NON-CC";
	
	/**
	 * General Status Code
	 */
	public static final String STATUS_CODE_SUCCESS = "0";

	public static final String STATUS_CODE_FAIL = "1";
	
	/**
	 * save path (private folder for this application)
	 * /data/data/[your package path]/files
	 */
	public static final String SAVE_PATH = "/data/data/it.sephiroth.android.library.imagezoom.test/files/";
	public static final String IMAGE_FOLDER = "/data/data/it.sephiroth.android.library.imagezoom.test/files/images/";
	public static final String DATABASE_FOLDER = "/data/data/it.sephiroth.android.library.imagezoom.test/files/database/";
	public static final String[] FILE_STRUCTURE_ARRAY = { SAVE_PATH, IMAGE_FOLDER, DATABASE_FOLDER };
	
	/**
	 * file save name
	 */
	public static final String DATABASE_FILE_NAME = "citychain.db";
	
	/**
	 * Shared Preference Key
	 */
	public static final String SHARED_PREFERENCE_APPLICATION_KEY = "shared_preference_application_key";
	public static final String SHARED_PREFERENCE_LANGUAGE_SETTING_KEY = "shared_preference_language_setting_key";
	public static final String SHARED_PREFERENCE_UUID_SAVE_KEY = "shared_preference_uuid_save_key";
	public static final String SHARED_PREFERENCE_NOTIFICATION_REGISTRATION_ID_KEY = "shared_preference_notification_registration_id_key";
	public static final String SHARED_PREFERENCE_GEO_FENCE_KEY = "shared_preference_geo_fence_key";
	public static final String SHARED_PREFERENCE_CHECKED_OUT_KEY = "shared_preference_checked_out_key";

	public static final String SHARED_PREFERENCE_CURRENT_DB_ISSUE_KEY = "shared_preference_current_db_issue_key";

	public static final String SHARED_PREFERENCE_CURRENT_DB_VERSION_KEY = "shared_preference_current_db_version_key";
	
	public static final String SHARED_PREFERENCE_FIRST_INITIALIZATION_KEY = "first_initialization_key";
	
	public static final String SHARED_PREFERENCE_LOCATION_SERVICE_SETTING_KEY = "location_service_setting_key";
	
	public static final String SHARED_PREFERENCE_IS_SIGNED_IN_KEY = "is_signed_in_key";
			
	public static final String SHARED_PREFERENCE_SIGN_IN_CODE = "sign_in_code";
	
	public static final String SHARED_PREFERENCE_SIGN_IN_PASSWORD = "sign_in_password";
	
	public static final String SHARED_PREFERENCE_CHECKED_OUT_DAY_KEY = "checked_out_day_key";
	
	/**
	 * GCM
	 */
	public static final String GCM_PROJECT_ID = "130613707534";
	public static final String GCM_PUSH_NOTIFICATION_MESSAGE = "gcm_push_notification_message";
	public static final String GCM_MESSAGE_RECEIVE_KEY = "message";
	public static final String GCM_PUSH_TYPE_RECEIVE_KEY = "type";
	public static final String GCM_PUSH_ID_RECEIVE_KEY = "item";
	public static boolean isPushAlertDialogDisplaying = false;
}
