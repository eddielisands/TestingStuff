package com.fancl.iloyalty;

public class ApiConstant {

	public enum LINK_TYPE
	{
		LOCAL_UAT ,
		PRODUCTION
	}

	public enum API_KEY
	{
		VALIDATE_USER_API
	}

	/**
	 * Linking Type
	 */
	public static final LINK_TYPE currentLinkType = LINK_TYPE.LOCAL_UAT;
//	public static final LINK_TYPE currentLinkType = LINK_TYPE.PRODUCTION;

	//	private static final String LOCAL_UAT_DOMAIN	= "http://61.239.248.194/iloyalty/";
	private static final String LOCAL_UAT_DOMAIN	= "http://202.130.80.205:49777/iloyalty_uat/"; 

	private static final String PRODUCTION_DOMAIN	= "http://202.130.80.205:49888/iloyalty/";

	private static String apiDomain = null;

	/**
	 * Setting API
	 */
	public static final String DATABASE_API = "version/version.plist";
	public static final String TILL_ID_DATABASE_API = "version/version_tillid.plist";

	public static final String PROMOTION_LIST_API = "api/promotion/list.do";
	public static final String PROMOTION_VISIT_API = "api/promotion/visit.do";
	public static final String PROMOTION_QUESTION_API = "api/promotion/questionList.do";
	public static final String PROMOTION_ANSWER_API = "api/promotion/answer.do";
	public static final String PROMOTION_COUPON_SELECT_API = "api/promotion/couponSelect.do";
	public static final String PROMOTION_COUNT_API = "api/promotion/count.do";
	
	public static final String USER_LOGIN_API = "api/member/login.do";
	public static final String USER_VALIDATE_USER_API = "api/member/validate.do";
	public static final String USER_REGISTER_API = "api/member/register.do";
	public static final String USER_CARD_REPLACEMENT_API = "api/member/cardReplace.do";
	public static final String USER_MEMBER_PROFILE_API = "api/member/profile.do";
	public static final String USER_UPDATE_MEMBER_PROFILE_API = "api/member/updateProfile.do";
	public static final String USER_CHANGE_PASSWORD_API = "api/member/changePassword.do";
	public static final String USER_PURCHASE_HISTORY_API = "api/member/purchaseHistory.do";
	public static final String USER_PURCHASE_HISTORY_RECEIPT_API = "api/member/purchaseHistoryReceipt.do";
	public static final String USER_GP_REWARD_API = "api/member/gpReward.do";
	public static final String USER_GP_REWARD_ITEM_API = "api/member/gpRewardHistoryItem.do";
	public static final String USER_FORGET_PASSWORD_API = "api/member/resetPassword.do";
//	public static final String USER_ICREDIT_BALANCE_API = "api/member/iCreditBalance.do";
	public static final String USER_ICREDIT_API = "api/member/iCredit.do";
//	public static final String USER_ICREDIT_GP_CONVERSION_API = "api/member/iCreditGpConvert.do";
	public static final String USER_GET_RECEIPT_API = "api/member/getReceipt.do";
	public static final String USER_SET_RECEIPT_API = "api/member/setReceipt.do";
	public static final String ADD_AND_UPDATE_USER_API = "api/addNUpdateUser.do";
	public static final String DATA_UPLOAD_API = "api/dataUpload.do";
	public static final String AD_HIT_RATE_API = "api/countAdHitRate.do";
	public static final String USER_NOTIFICATION_API = "api/notification/list.do";
	
	// TCP Socket
	public static final String SOCKET_DOMAIN_NAME = "202.130.80.205";
	
	public static final int SOCKET_PORT = 30002;   //uat
//	public static final int SOCKET_PORT = 30001;   //live   

	
	// Client -> Server
	public static final String COMMAND_CONNECT_REQUEST = "00";
	public static final String COMMAND_DISCONNECT_REQUEST = "01";
	public static final String COMMAND_ACK_REQUEST = "02";
	public static final String COMMAND_TILL_ID_REQUEST = "03";
	public static final String COMMAND_PURHCASE_ACK_REQUEST = "04";
	public static final String COMMAND_CANCEL_PURCHASE_REQUEST = "05";

	public static final String COMMAND_CONNECT_RESPONSE = "80";
	public static final String COMMAND_DISCONNECT_RESPONSE = "81";
	public static final String COMMAND_ACK_RESPONSE = "82";
	public static final String COMMAND_TILL_ID_RESPONSE = "83";
	public static final String COMMAND_PURHCASE_ACK_RESPONSE = "84";
	public static final String COMMAND_CANCEL_PURCHASE_RESPONSE = "85";

	/**
	 * Images Prefix
	 */
	public static final String HOT_ITEM_IMAGE_PATH = "images/hotItem/";
	public static final String PROMOTION_IMAGE_PATH = "images/promotion/";
	public static final String PRODUCT_IMAGE_PATH = "images/product/";
	public static final String ICHANNEL_IMAGE_PATH = "images/magazine/";
	public static final String ABOUT_FANCL_IMAGE_PATH = "images/about/";
	public static final String SHOP_IMAGE_PATH = "images/shop/";
	public static final String AD_IMAGE_PATH = "images/ad/";
	public static final String GIFT_IMAGE_PATH = "images/gift/";


	public static String getDomain()
	{
		if(apiDomain == null)
		{
			switch(currentLinkType)
			{
			case LOCAL_UAT:
				apiDomain = LOCAL_UAT_DOMAIN;
				break;
			case PRODUCTION:
				apiDomain = PRODUCTION_DOMAIN;
				break;
			default:
				apiDomain = LOCAL_UAT_DOMAIN;
				break;
			}
		}

		return apiDomain;
	}	

	public static String getAPI(String apiKey)
	{
		if(apiKey != null)
		{
			if (apiKey.equals(PROMOTION_LIST_API)) {
				return getDomain() + apiKey;
			}
			else if (apiKey.equals(DATABASE_API)) {
				return getDomain() + apiKey;
			}
			else if (apiKey.equals(PROMOTION_VISIT_API)) {
				return getDomain() + apiKey;
			}
			else if (apiKey.equals(PROMOTION_QUESTION_API)) {
				return getDomain() + apiKey;
			}
			else if (apiKey.equals(PROMOTION_ANSWER_API)) {
				return getDomain() + apiKey;
			}

			return getDomain() + apiKey;
		}

		return "";
	}
}
