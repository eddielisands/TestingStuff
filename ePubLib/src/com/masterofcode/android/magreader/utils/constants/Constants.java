package com.masterofcode.android.magreader.utils.constants;

import com.masterofcode.android.magreader.application.JtjApplication;
import com.sandsmedia.apps.mobile.android.epub.lib.R;

public class Constants {
	
	public static final String DATABASE_NAME = "magazinedatabase.db";
	public static final int DATABASE_VERSION = 1008;

	public static final String BOOKMARKS_DATABASE_NAME = "bookmarks.db";
	public static final int BOOKMARKS_DATABASE_VERSION = 1005;

	public static final String LIBRARY_DATABASE_NAME = "library.db";
	public static final int LIBRARY_DATABASE_VERSION = 1001;
	public static final String LIBRARY_DIR = "library";

	
	public static final String PURCHASE_DATABASE_NAME = "purchase.db";
	public static final int PURCHASE_DATABASE_VERSION = 1000;

	public final static boolean Debug = false; //Allow debug log
	public static final String UPDATE_FEEDS = "com.masterofcode.android.magreader.utils.constants.Constants.FEEDS_TO_UPDATE";
	public static final long DEFAULT_TIME_TO_UPDATE = 3600000;
	
	public static final int REQUEST_CODE_AUTHENTICATE = 8000;
    public static final int REQUEST_SELECT_CATEGORY = REQUEST_CODE_AUTHENTICATE + 1;
    public static final int REQUEST_CODE_ADD_FRIEND = REQUEST_SELECT_CATEGORY + 1;
    public static final int REQUEST_CATEGORY_SELECTED = REQUEST_CODE_ADD_FRIEND + 1;
    public static final int REQUEST_ITEM_UPDATED = REQUEST_CATEGORY_SELECTED + 1;
    public static final int REQUEST_CODE_SELECT_MEDIA_TYPE = REQUEST_ITEM_UPDATED + 1;    
    public static final int REQUEST_CODE_SELECTED_MEDIA_TYPE = REQUEST_CODE_SELECT_MEDIA_TYPE + 1;
    public static final int REQUEST_SHOW_SEARCH_ITEM = REQUEST_CODE_SELECTED_MEDIA_TYPE +1;
    public static final int REQUEST_CODE_VIEW_IN_BROWSER = REQUEST_SHOW_SEARCH_ITEM +1;
	public static final int AUTO_UPDATE_REQUEST_CODE = 	REQUEST_SHOW_SEARCH_ITEM+1;
	
	public static final String CONTENT_TYPE_TEXT_HTML = "text/html";
    public static final String CHAR_ENCODING_UTF8 = "UTF-8";
    
    public static final String THUMBNAILS_URL_SCRIPT = "http://android-app-backend.sandsmedia.com/image/thumbnail?width=122&height=121&imageurl=";
    
    public static final String LOG_BS = "EPUB";
    public static final String FEED_ALL_NAME = "All-inclusive";
    public static final String FEED_NEWS_NAME = "News";
    public static final String FEED_VIDEOS_NAME = "Videos";
    
	public static final String KEY_PREF = "preferences";
	public static final String DATE_FEEDS_UPDATE = "dateFeedsUpdate";
	
//	public static final String APP_ID = "223252187774878"; //Facebook JTJ constants

	//public static final String BACKEND_BASE_URL = "https://ipad.sandsmedia.com:8443/ipad/javatechjournal/";
//	public static final String BACKEND_BASE_URL = "https://ipad.sandsmedia.com:8443/ipad/ecm/";
	//public static final String BACKEND_BASE_URL = "http://192.168.3.165:8280/ipad/ecm/";
	//public static final String BACKEND_BASE_URL = "http://192.168.3.165:8280/ipad/javatechjournal/";
//	public static final String MAGAZINE_SERIES_PREFIX = "Eclipse Magazin ";
	
	// number of constant (unremovable) feeds in application
	public static final int CONSTANT_FEEDS_COUNT = 5;
	
	
	public static final String BACKEND_AUTH_URL = JtjApplication.getContext().getString(R.string.backend_base_url) + "authcheck";
	public static final String ISSUE_DETAILS_URL = JtjApplication.getContext().getString(R.string.backend_base_url) + "issues";
	
	public static final long DEFAULT_TIME_TO_UPDATE_ISSUES = 2678400000L; //31 day
	public static final String TECH_USER_LOGIN = "jtjinternaluser20110221-hgtao";
	public static final String TECH_USER_PASSWORD = "gt56apy4786";
	
	public static final String UPDATE_ISSUE = "com.masterofcode.android.magreader.utils.constants.Constants.UPDATE_ISSUE";
	public static final String SUBSCRIPTION_PASSWORD_IS_INCORRECT = "com.masterofcode.android.magreader.utils.constants.Constants.SUBSCRIPTION_PASSWORD_IS_INCORRECT";
	public static final String UPDATING_ISSUES_IO_ERROR = "com.masterofcode.android.magreader.utils.constants.Constants.UPDATING_ISSUES_IO_ERROR";
	
	// show or not subscription item in settings menu
	public static final boolean SUBSCRIPTION_ENABLED = true;
	
	final static public int BOOKMARKS_ITEM_TYPE_NONE = 0;
	final static public int BOOKMARKS_ITEM_TYPE_FEED = 1;
	final static public int BOOKMARKS_ITEM_TYPE_MAGAZINE = 2;

	final static public int MAGAZINE_TYPE_NONE = 0;
	final static public int MAGAZINE_TYPE_NORMAL = 1;
	final static public int MAGAZINE_TYPE_FROM_RESOURCES = 2;

	final static public String MAGAZINE_COPIED_MARK_SUFFIX = ".copied";
	final static public String MAGAZINE_COPIED_ALL_RESOURCES_FILE_NAME = "resources.copied";
	final static public String MAGAZINE_COVER_SUFFIX = ".cover.png";
	final static public String MAGAZINE_JSON_SUFFIX = ".json";
	
	final static public String ISSUE_METADATA_ATTRIBUTE_ID_ID = "id";
	final static public String ISSUE_METADATA_ATTRIBUTE_TITLE_ID = "title";
	
	public static final String BUNDLE_KEY_EPUB_FILE_PATH = "epub_fname";
	public static final String BUNDLE_KEY_EPUB_COVER_FILE_PATH = "epub_cover_file_path";
	public static final String BUNDLE_KEY_EPUB_TOPIC_INDEX = "epub_topic_index";
	public static final String BUNDLE_KEY_EPUB_HIGHLIGHTING_MODE = "epub_highlighting_mode";
	public static final String BUNDLE_KEY_EPUB_HIGHLIGHTING_KEYWORD = "epub_highlighting_keyword";
	public static final String BUNDLE_KEY_EPUB_HIGHLIGHTING_TOPICS = "epub_highlighting_topics";
	public static final String BUNDLE_KEY_EPUB_TOPIC_CONTENT_OFFSET = "epub_topic_content_offset";

	public static final String BUNDLE_KEY_SEARCH_KEYWORD = "search_keyword";
	public static final String BUNDLE_KEY_SEARCH_TYPE = "search_type";

	public static final String BUNDLE_KEY_SETTINGS_SELECT_FEEDS = "select_feeds";

	public static final int SEARCH_TYPE_NONE = 0;
	public static final int SEARCH_TYPE_FEEDS = 1;
	public static final int SEARCH_TYPE_LIBRARY = 2;
	public static final int SEARCH_TYPE_EVERYWHERE = 3;
	
	// if false, every topic always show separately, even if all topics contains keyword
	public static final boolean SEARCH_IN_LIBRARY_WITH_THRESHOLD = true;
	
	// if previous is true, than if percent of topics that have keyword bigger than value below
	// all of them showed in search result as single (whole book) element
	public static final float SEARCH_IN_LIBRARY_ALL_BOOK_THRESHOLD = 0.3f;
	
	// if true - trim searchable lines in topics
	public static final boolean SEARCH_IN_LIBRARY_IS_SEARCHABLE_TEXT_TRIMMED = true;

	public static final String PREFERENCE_UPDATE_FEEDS_TIME = "TIME_TO_UPDATE";
	public static final String PREFERENCE_SELECTED_ITEM_TIME = "SELECTED_TIME_UPDATE_ITEM";
	public static final int DEFAULT_SELECTED_ITEM = 1;
	
	public static final int SETTINGS_ACTIVITY = 9901;
	public static final String FEEDS_TO_UPDATE = "feeds_to_update";
	
	public static final String PREFERENCES_PROPERTY_SHOW_FEEDS_HINT_DISABLED = "show_feeds_hint_disable";
	
//	private static final String API_BASE_URL = "http://ecm-android-reader-api.sandsmedia.com/";
	public static final String CHECK_FEEDS_AVAILABLE_URL = JtjApplication.getContext().getString(R.string.api_base_url) + "check_inet.json";
	public static final String FEEDS_LIST_URL = JtjApplication.getContext().getString(R.string.api_base_url) + "getjson.json";

	// subscription preferences and other
	public static final String PREFERENCE_SUBSCRIPTION_ENABLED = "subscription_enabled";
	public static final String PREFERENCE_SUBSCRIPTION_NAME = "subscription_name";
	public static final String PREFERENCE_SUBSCRIPTION_PASSWORD = "subscription_password";
	
	public static final int REUQEST_CODE_SUBSCRIPTION_ACTIVITY = 9902;
	
	public static final String BUNDLE_KEY_SUBSCRIPTION_IS_LOGGED_IN = "bk_subscription_logged_in";
	public static final String BUNDLE_KEY_SUBSCRIPTION_ACTION = "bk_subscription_action";
	
	public static final String FEEDS_JSON_ATTRIBUTES_DEFAULT = "default";
}
