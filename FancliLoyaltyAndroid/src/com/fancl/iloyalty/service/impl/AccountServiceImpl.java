package com.fancl.iloyalty.service.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fancl.iloyalty.AndroidProjectApplication;
import com.fancl.iloyalty.ApiConstant;
import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.Constants.HTTP_CALLING_METHOD;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.exception.GeneralException;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.parser.FanclResultParser;
import com.fancl.iloyalty.pojo.FormContent;
import com.fancl.iloyalty.pojo.GPReward;
import com.fancl.iloyalty.pojo.GPRewardHistoryItem;
import com.fancl.iloyalty.pojo.Notification;
import com.fancl.iloyalty.pojo.PurchaseHistory;
import com.fancl.iloyalty.pojo.PurchaseHistoryReceipt;
import com.fancl.iloyalty.pojo.User;
import com.fancl.iloyalty.pojo.UserRegistrationParam;
import com.fancl.iloyalty.pojo.ValidateUserParam;
import com.fancl.iloyalty.responseimpl.FanclGeneralResult;
import com.fancl.iloyalty.responseimpl.TOSResult;
import com.fancl.iloyalty.responseimpl.ValidationResult;
import com.fancl.iloyalty.service.AccountService;
import com.fancl.iloyalty.service.HttpConnectionService;
import com.fancl.iloyalty.util.DeviceUtil;
import com.fancl.iloyalty.util.LogController;
import com.longevitysoft.android.xml.plist.domain.PList;

public class AccountServiceImpl implements AccountService{

	@Override
	public boolean isLogin() {
		// TODO Auto-generated method stub
		SharedPreferences sharedPreferences = AndroidProjectApplication.application.getSharedPreferences(
				Constants.SHARED_PREFERENCE_APPLICATION_KEY,
				Context.MODE_PRIVATE);
		String memberId = sharedPreferences.getString(Constants.SHARED_PREFERENCE_MEMBER_ID_KEY, "");
		boolean isLogin = false;
		if (memberId.length() > 0) {
			isLogin = true;
		}
		return isLogin;
	}

	@Override
	public String currentMemberId() {
		// TODO Auto-generated method stub
		SharedPreferences sharedPreferences = AndroidProjectApplication.application.getSharedPreferences(
				Constants.SHARED_PREFERENCE_APPLICATION_KEY,
				Context.MODE_PRIVATE);
		String memberId = sharedPreferences.getString(Constants.SHARED_PREFERENCE_MEMBER_ID_KEY, "");
		return memberId;
	}

	@Override
	public String currentDeviceUUID() {
		// TODO Auto-generated method stub
		String uuid = DeviceUtil.getDeviceUUID(AndroidProjectApplication.application);
		return uuid;
	}

	@Override
	public String currentUserToken() {
		// TODO Auto-generated method stub
		SharedPreferences sharedPreferences = AndroidProjectApplication.application.getSharedPreferences(
				Constants.SHARED_PREFERENCE_APPLICATION_KEY,
				Context.MODE_PRIVATE);
		String userToken = sharedPreferences.getString(Constants.SHARED_PREFERENCE_NOTIFICATION_REGISTRATION_ID_KEY, "");
		return userToken;
	}

	@Override
	public List<FormContent> getFormContentWithType(String type)
			throws FanclException {
		// TODO Auto-generated method stub

		LogController.log("getFormContentWithType " + type);
		//		NSString* sqlStatement = @"select * from hot_item where is_highlight = 'Y' order by sequence;";

		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{	
				//				String sql = "select * from content_selection where type = '%@' order by sequence;";
				String sql = "SELECT * FROM content_selection WHERE type = '" + type + "' ORDER BY sequence;";

				c = dB.rawQuery(sql, null);

				List<FormContent> formContentList = new ArrayList<FormContent>();
				FormContent formContent = null;

				String objectId = "";
				String code = "";
				String titleZh = "";
				String titleSc = "";
				String titleEn = "";

				c.moveToFirst();
				while (!c.isAfterLast()) {
					objectId = c.getString(c.getColumnIndex("id"));
					code = c.getString(c.getColumnIndex("code"));
					titleZh = c.getString(c.getColumnIndex("title_zh"));
					titleSc = c.getString(c.getColumnIndex("title_sc"));
					titleEn = c.getString(c.getColumnIndex("title_en"));

					formContent = new FormContent(code, titleZh, titleSc, titleEn);

					formContentList.add(formContent);

					c.moveToNext();
				}

				c.close();
				c = null;

				return formContentList;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
		}
		finally
		{
			if (c != null)
			{
				c.close();
				c = null;
			}
			if (dB != null)
			{

			}
		}

		if(exception != null)
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, exception.getMessage());
		}

		return null;
	}

	@Override
	public ValidationResult validateUserWithMemberId(ValidateUserParam validateUserParam) throws FanclException {
		String url = ApiConstant.getAPI(ApiConstant.USER_VALIDATE_USER_API);

		String[] keys;
		String[] values;

		keys = new String[]{"fanclMemberId", "uuid", "userToken", "language", "location", "systemType"};
		values = new String[]{validateUserParam.getFanclMemberId(), currentDeviceUUID(), currentUserToken(),
				currentLanguage(), "hk", "A"};

		HttpConnectionService httpConnectionService = GeneralServiceFactory.getHttpConnectionService(AndroidProjectApplication.application);
		PList plist = null;
		try {
			LogController.log("url " + url);

			for (int i = 0; i < keys.length; i++) {
				LogController.log("key " + i + " : " + keys[i]);
			}
			for (int i = 0; i < values.length; i++) {
				LogController.log("value " + i + " : " + values[i]);
			}

			plist = httpConnectionService.downloadPList(url, keys, values, HTTP_CALLING_METHOD.GET);
		} catch (GeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (plist != null)
		{
			LogController.log("validateUserWithMemberId 03");
			FanclResultParser fanclResultParser = new FanclResultParser();
			return fanclResultParser.parseValidationResult(plist);
		}		
		else
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, Constants.DOWNLOAD_RETURN_NULL_GERERAL_MESSAGE);
		}
	}

	@Override
	public TOSResult newUserCardReplaceWithOldMemberId(String oldMemberId)
			throws FanclException {
		// TODO Auto-generated method stub
		String url = ApiConstant.getAPI(ApiConstant.USER_CARD_REPLACEMENT_API);

		String[] keys = new String[] { "fanclMemberId", "uuid", "userToken", "language", "location", "systemType" };
		String[] values = new String[] { oldMemberId, currentDeviceUUID(), currentUserToken(), currentLanguage(), "hk", "A" };

		HttpConnectionService httpConnectionService = GeneralServiceFactory.getHttpConnectionService(AndroidProjectApplication.application);
		PList plist = null;
		try {
			plist = httpConnectionService.downloadPList(url, keys, values, HTTP_CALLING_METHOD.POST);
		} catch (GeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (plist != null)
		{
			FanclResultParser fanclResultParser = new FanclResultParser();
			return fanclResultParser.parseTOSResult(plist);
		}
		else
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, Constants.DOWNLOAD_RETURN_NULL_GERERAL_MESSAGE);
		}
	}

	@Override
	public FanclGeneralResult registerUserWithMemberId(UserRegistrationParam userRegistrationParam)
			throws FanclException {
		// TODO Auto-generated method stub
		String url = ApiConstant.getAPI(ApiConstant.USER_REGISTER_API);

		String[] keys = new String[] { "fanclMemberId", "lastName", "firstName", "mobile", "email", "retypeEmail",
				"gender", "password", "retypePassword", "uuid", "userToken", "userLanguage",
				"skinType", "address1", "address2", "address3", "country" , "city" ,
				"monthOfBirth", "yearOfBirth", "language", "location", "systemType" };
		String[] values = new String[] { userRegistrationParam.getId(), userRegistrationParam.getSurname(), userRegistrationParam.getName(), 
				userRegistrationParam.getMobile(), userRegistrationParam.getEmail(), userRegistrationParam.getRetypeEmail(),
				userRegistrationParam.getGender(), userRegistrationParam.getPassword(), userRegistrationParam.getRetypePassword(), 
				currentDeviceUUID(), currentUserToken(), currentUserLanguage(), userRegistrationParam.getSkinType(), userRegistrationParam.getAddress1(), 
				userRegistrationParam.getAddress2(), userRegistrationParam.getAddress3(), userRegistrationParam.getCountry(), 
				userRegistrationParam.getCity(), userRegistrationParam.getMonthOfBirth(), userRegistrationParam.getYearOfBirth(), 
				currentLanguage(), "hk", "A" };

		HttpConnectionService httpConnectionService = GeneralServiceFactory.getHttpConnectionService(AndroidProjectApplication.application);
		PList plist = null;
		try {
			plist = httpConnectionService.downloadPList(url, keys, values, HTTP_CALLING_METHOD.POST);
		} catch (GeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (plist != null)
		{
			FanclResultParser fanclResultParser = new FanclResultParser();
			return fanclResultParser.parseGeneralResult(plist);
		}
		else
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, Constants.DOWNLOAD_RETURN_NULL_GERERAL_MESSAGE);
		}
	}

	@Override
	public FanclGeneralResult loginWithEmail(String loginName, String password)
			throws FanclException {
		// TODO Auto-generated method stub
		String url = ApiConstant.getAPI(ApiConstant.USER_LOGIN_API);

		String[] keys = new String[] { "loginName", "password", "uuid", "userToken", "language", "location", "systemType" };
		String[] values = new String[] { loginName, password, currentDeviceUUID(), currentUserToken(), currentLanguage(), "hk", "A" };

		HttpConnectionService httpConnectionService = GeneralServiceFactory.getHttpConnectionService(AndroidProjectApplication.application);
		PList plist = null;
		try {
			plist = httpConnectionService.downloadPList(url, keys, values, HTTP_CALLING_METHOD.POST);
		} catch (GeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (plist != null)
		{
			FanclResultParser fanclResultParser = new FanclResultParser();
			return fanclResultParser.parseGeneralResult(plist);
		}
		else
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, Constants.DOWNLOAD_RETURN_NULL_GERERAL_MESSAGE);
		}		
	}

	@Override
	public FanclGeneralResult forgetPasswordWithMobile(String mobile, String email)
			throws FanclException {
		// TODO Auto-generated method stub
		String url = ApiConstant.getAPI(ApiConstant.USER_FORGET_PASSWORD_API);

		String[] keys = new String[] { "identifier", "email", "language", "systemType" };
		String[] values = new String[] { mobile, email, currentLanguage(), "A" };

		HttpConnectionService httpConnectionService = GeneralServiceFactory.getHttpConnectionService(AndroidProjectApplication.application);
		PList plist = null;
		try {
			plist = httpConnectionService.downloadPList(url, keys, values, HTTP_CALLING_METHOD.POST);
		} catch (GeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (plist != null)
		{
			FanclResultParser fanclResultParser = new FanclResultParser();
			return fanclResultParser.parseGeneralResult(plist);
		}
		else
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, Constants.DOWNLOAD_RETURN_NULL_GERERAL_MESSAGE);
		}
	}

	@Override
	public User getUserProfile()
			throws FanclException {
		// TODO Auto-generated method stub
		String url = ApiConstant.getAPI(ApiConstant.USER_MEMBER_PROFILE_API);

		String[] keys = new String[] { "fanclMemberId", "language", "systemType" };
		String[] values = new String[] { currentMemberId(), currentLanguage(), "A" };

		HttpConnectionService httpConnectionService = GeneralServiceFactory.getHttpConnectionService(AndroidProjectApplication.application);
		PList plist = null;
		try {
			plist = httpConnectionService.downloadPList(url, keys, values, HTTP_CALLING_METHOD.POST);
		} catch (GeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (plist != null)
		{
			FanclResultParser fanclResultParser = new FanclResultParser();
			return fanclResultParser.parseMemberProfile(plist);
		}
		else
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, Constants.DOWNLOAD_RETURN_NULL_GERERAL_MESSAGE);
		}
	}

	@Override
	public FanclGeneralResult updateUserProfile(User userParam)
			throws FanclException {
		// TODO Auto-generated method stub
		String url = ApiConstant.getAPI(ApiConstant.USER_UPDATE_MEMBER_PROFILE_API);
		String firstName = userParam.getFirstName();
		if (firstName == null) {
			firstName = "";
		}
		String lastName = userParam.getLastName();
		if (lastName == null) {
			lastName = "";
		}
		String mobile = userParam.getMobile();
		if (mobile == null) {
			mobile = "";
		}
		String email = userParam.getEmail();
		if (email == null) {
			email = "";
		}
		String skinType = userParam.getSkinType();
		if (skinType == null) {
			skinType = "";
		}
		String address1 = userParam.getAddress1();
		if (address1 == null) {
			address1 = "";
		}
		String address2 = userParam.getAddress2();
		if (address2 == null) {
			address2 = "";
		}
		String address3 = userParam.getAddress3();
		if (address3 == null) {
			address3 = "";
		}
		String gender = userParam.getGender();
		if (gender == null) {
			gender = "";
		}

		String[] keys = new String[] { "fanclMemberId", "firstName", "lastName", "mobile", "email", "skinType", "address1", "address2", "address3", "gender", "language", "systemType" };
		String[] values = new String[] { currentMemberId(), firstName, lastName, mobile, email, skinType, address1, address2, address3, gender, currentLanguage(), "A" };

		HttpConnectionService httpConnectionService = GeneralServiceFactory.getHttpConnectionService(AndroidProjectApplication.application);
		PList plist = null;
		try {
			plist = httpConnectionService.downloadPList(url, keys, values, HTTP_CALLING_METHOD.POST);
		} catch (GeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (plist != null)
		{
			FanclResultParser fanclResultParser = new FanclResultParser();
			return fanclResultParser.parseGeneralResult(plist);
		}
		else
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, Constants.DOWNLOAD_RETURN_NULL_GERERAL_MESSAGE);
		}
	}

	@Override
	public FanclGeneralResult updateUserPassword(
			String oldPassword, String newPassword,
			String retypeNewPassword) throws FanclException {
		// TODO Auto-generated method stub
		String url = ApiConstant.getAPI(ApiConstant.USER_CHANGE_PASSWORD_API);

		String[] keys = new String[] { "fanclMemberId", "oldPassword", "newPassword", "retypeNewPassword", "language", "systemType" };
		String[] values = new String[] { currentMemberId(), oldPassword, newPassword, retypeNewPassword, currentLanguage(), "A" };

		HttpConnectionService httpConnectionService = GeneralServiceFactory.getHttpConnectionService(AndroidProjectApplication.application);
		PList plist = null;
		try {
			plist = httpConnectionService.downloadPList(url, keys, values, HTTP_CALLING_METHOD.POST);
		} catch (GeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (plist != null)
		{
			FanclResultParser fanclResultParser = new FanclResultParser();
			return fanclResultParser.parseGeneralResult(plist);
		}
		else
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, Constants.DOWNLOAD_RETURN_NULL_GERERAL_MESSAGE);
		}
	}

	@Override
	public Object getPurchaseHistory()
			throws FanclException {
		// TODO Auto-generated method stub
		String url = ApiConstant.getAPI(ApiConstant.USER_PURCHASE_HISTORY_API);

		String[] keys = new String[] { "fanclMemberId", "language", "systemType" };
		String[] values = new String[] { currentMemberId(), currentLanguage(), "A" };

		HttpConnectionService httpConnectionService = GeneralServiceFactory.getHttpConnectionService(AndroidProjectApplication.application);
		PList plist = null;
		try {
			plist = httpConnectionService.downloadPList(url, keys, values, HTTP_CALLING_METHOD.POST);
		} catch (GeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (plist != null)
		{
			FanclResultParser fanclResultParser = new FanclResultParser();
			Object object = fanclResultParser.parsePurchaseHistoryListResult(plist);
			if (object instanceof List) {
				if (((List<?>) object).size() > 0) {
					Object tmpObj = ((List<?>) object).get(0);
					if (tmpObj instanceof PurchaseHistory) {
						return ((List<PurchaseHistory>) object);
					}
				}
				return null;
			}
			else {
				return null;
			}
		}
		else
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, Constants.DOWNLOAD_RETURN_NULL_GERERAL_MESSAGE);
		}
	}

	@Override
	public Object getPurchaseHistoryReceipt(
			String purchaseDatetime, String memo,
			String shopCode) throws FanclException {
		// TODO Auto-generated method stub
		String url = ApiConstant.getAPI(ApiConstant.USER_PURCHASE_HISTORY_RECEIPT_API);

		String[] keys = new String[] { "fanclMemberId", "purchaseDatetime", "salesMemo", "shopCode", "language", "systemType" };
		String[] values = new String[] { currentMemberId(), purchaseDatetime, memo, shopCode, currentLanguage(), "A" };

		HttpConnectionService httpConnectionService = GeneralServiceFactory.getHttpConnectionService(AndroidProjectApplication.application);
		PList plist = null;
		try {
			plist = httpConnectionService.downloadPList(url, keys, values, HTTP_CALLING_METHOD.POST);
		} catch (GeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (plist != null)
		{
			FanclResultParser fanclResultParser = new FanclResultParser();
			Object object = fanclResultParser.parsePurchaseHistoryReceipt(plist);
			if (object instanceof List) {
				if (((List<?>) object).size() > 0) {
					Object tmpObj = ((List<?>) object).get(0);
					if (tmpObj instanceof PurchaseHistoryReceipt) {
						return ((List<PurchaseHistoryReceipt>) object);
					}
				}
				return null;
			}
			else {
				return null;
			}
		}
		else
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, Constants.DOWNLOAD_RETURN_NULL_GERERAL_MESSAGE);
		}
	}

	@Override
	public Object getGPRewards() throws FanclException {
		// TODO Auto-generated method stub
		String url = ApiConstant.getAPI(ApiConstant.USER_GP_REWARD_API);

		String[] keys = new String[] { "fanclMemberId", "language", "systemType" };
		String[] values = new String[] { currentMemberId(), currentLanguage(), "A" };


		HttpConnectionService httpConnectionService = GeneralServiceFactory.getHttpConnectionService(AndroidProjectApplication.application);
		PList plist = null;
		try {
			plist = httpConnectionService.downloadPList(url, keys, values, HTTP_CALLING_METHOD.POST);
		} catch (GeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (plist != null)
		{
			FanclResultParser fanclResultParser = new FanclResultParser();
			Object object = fanclResultParser.parseGPRewardListResult(plist);
			if (object instanceof GPReward) {
				//				if (((List<?>) object).size() > 0) {
				//					Object tmpObj = ((List<?>) object).get(0);
				//					if (tmpObj instanceof GPReward) {
				//						return ((List<GPReward>) object);
				//					}
				//				}
				//				return (FanclGeneralResult) object;
				return object;
			}
			else {
				return (FanclGeneralResult) object;
			}
		}
		else
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, Constants.DOWNLOAD_RETURN_NULL_GERERAL_MESSAGE);
		}
	}

	public GPRewardHistoryItem getGPRewardsHistoryItem(String purchaseDatetime,String salesMemo,String shopCode,String itemCode) throws FanclException{
		String url = ApiConstant.getAPI(ApiConstant.USER_GP_REWARD_ITEM_API);

		String[] keys = new String[] { "fanclMemberId", "purchaseDatetime" ,"salesMemo", "shopCode", "itemCode", "language", "systemType" };
		String[] values = new String[] { currentMemberId(), purchaseDatetime ,salesMemo, shopCode, itemCode, currentLanguage(), "A" };

		HttpConnectionService httpConnectionService = GeneralServiceFactory.getHttpConnectionService(AndroidProjectApplication.application);
		PList plist = null;
		try {
			plist = httpConnectionService.downloadPList(url, keys, values, HTTP_CALLING_METHOD.POST);
		} catch (GeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (plist != null)
		{
			FanclResultParser fanclResultParser = new FanclResultParser();
			Object object = fanclResultParser.parseGPRewardHistoryItem(plist);
			
			LogController.log("object gift:"+object);
			if (object instanceof GPRewardHistoryItem) {

				return (GPRewardHistoryItem) object;
			}
			else {
				return null;
			}
//			return (GPRewardHistoryItem) object;
		}
		else
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, Constants.DOWNLOAD_RETURN_NULL_GERERAL_MESSAGE);
		}

	}

	@Override
	public Object getNotificationList()
			throws FanclException {
		// TODO Auto-generated method stub
		String url = ApiConstant.getAPI(ApiConstant.USER_NOTIFICATION_API);

		LogController.log("uuid:"+currentDeviceUUID());

		String[] keys = new String[] { "uuid", "language", "systemType" };
		String[] values = new String[] { currentDeviceUUID(), currentLanguage(), "A" };

		HttpConnectionService httpConnectionService = GeneralServiceFactory.getHttpConnectionService(AndroidProjectApplication.application);
		PList plist = null;
		try {
			plist = httpConnectionService.downloadPList(url, keys, values, HTTP_CALLING_METHOD.POST);
		} catch (GeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (plist != null)
		{
			FanclResultParser fanclResultParser = new FanclResultParser();
			Object object = fanclResultParser.parseNotificationResultList(plist);
			if (object instanceof List) {
				if (((List<?>) object).size() > 0) {
					Object tmpObj = ((List<?>) object).get(0);
					if (tmpObj instanceof Notification) {
						return ((List<Notification>) object);
					}
				}
				return null;
			}
			else {
				return null;
			}
		}
		else
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, Constants.DOWNLOAD_RETURN_NULL_GERERAL_MESSAGE);
		}
	}

	@Override
	public FanclGeneralResult addAndUpdateUser() throws FanclException {
		// TODO Auto-generated method stub
		String url = ApiConstant.getAPI(ApiConstant.ADD_AND_UPDATE_USER_API);
//		String url = "http://61.239.248.194/iloyalty/api/addNUpdateUser.do";
		
		String[] keys = new String[]{"uuid", "userToken", "fanclMemberId", "language", "systemType"};
		String[] values = new String[]{currentDeviceUUID(), currentUserToken(), currentMemberId(), currentLanguage(), "A"};
//		String[] values = new String[]{currentDeviceUUID(), currentUserToken(), "123456", currentLanguage(), "A"};

		HttpConnectionService httpConnectionService = GeneralServiceFactory.getHttpConnectionService(AndroidProjectApplication.application);
		PList plist = null;
		try {
			plist = httpConnectionService.downloadPList(url, keys, values, HTTP_CALLING_METHOD.POST);
		} catch (GeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (plist != null)
		{
			FanclResultParser fanclResultParser = new FanclResultParser();
			return fanclResultParser.parseGeneralResult(plist);
		}
		else
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, Constants.DOWNLOAD_RETURN_NULL_GERERAL_MESSAGE);
		}
	}

	@Override
	public String currentLanguage() {
		String language = GeneralServiceFactory.getLocaleService().textByLangaugeChooser(AndroidProjectApplication.application, "EN", "TC", "SC");
		return language;
	}

	@Override
	public String currentUserLanguage() {
		String language = GeneralServiceFactory.getLocaleService().textByLangaugeChooser(AndroidProjectApplication.application, "en", "zh", "sc");
		return language;
	}

	public void saveFavouriteList (String itemType, String itemId , String favouriteType){
		SharedPreferences sharedPreferences = AndroidProjectApplication.application.getSharedPreferences(
				Constants.SHARED_PREFERENCE_APPLICATION_KEY,
				Context.MODE_PRIVATE);
		
		LogController.log("itemId:"+itemId);
		
		if(itemId==null || itemId.equals(""))
			return;


		if(favouriteType.equals("myFavourite")){
			String favouriteTypeStr = sharedPreferences.getString(Constants.MY_FAVOURITE_TYPE_KEY,
					null);
			String favouriteIdStr = sharedPreferences.getString(Constants.MY_FAVOURITE_ID_KEY,
					null);

			if(favouriteTypeStr != null && favouriteIdStr != null){
				favouriteTypeStr = favouriteTypeStr + "," + itemType;
				favouriteIdStr = favouriteIdStr + "," + itemId;
			}else{
				favouriteTypeStr = itemType;
				favouriteIdStr = itemId;
			}

			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString(Constants.MY_FAVOURITE_TYPE_KEY, favouriteTypeStr);
			editor.putString(Constants.MY_FAVOURITE_ID_KEY, favouriteIdStr);
			editor.commit();

		}else if(favouriteType.equals("qrScan")){
			String favouriteTypeStr = sharedPreferences.getString(Constants.QR_FAVOURITE_TYPE_KEY,
					null);
			String favouriteIdStr = sharedPreferences.getString(Constants.QR_FAVOURITE_ID_KEY,
					null);

			if(favouriteTypeStr != null && favouriteIdStr != null){
				favouriteTypeStr = favouriteTypeStr + "," + itemType;
				favouriteIdStr = favouriteIdStr + "," + itemId;
			}else{
				favouriteTypeStr = itemType;
				favouriteIdStr = itemId;
			}

			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString(Constants.QR_FAVOURITE_TYPE_KEY, favouriteTypeStr);
			editor.putString(Constants.QR_FAVOURITE_ID_KEY, favouriteIdStr);
			editor.commit();
		}

		LogController.log("after add myfavourite:"+ sharedPreferences.getString(Constants.MY_FAVOURITE_ID_KEY,
				null));
		LogController.log("after add qrScan:"+ sharedPreferences.getString(Constants.QR_FAVOURITE_ID_KEY,
				null));
	}

	@Override
	public Object earnCreditWithEventId(String eventId)
			throws FanclException {
		String url = ApiConstant.getAPI(ApiConstant.USER_ICREDIT_API);

		String[] keys = new String[] { "fanclMemberId", "eventId", "location", "language", "systemType" };
		String[] values = new String[] { currentMemberId(), eventId, "hk", currentLanguage(), "A" };

		HttpConnectionService httpConnectionService = GeneralServiceFactory.getHttpConnectionService(AndroidProjectApplication.application);
		PList plist = null;
		try {
			plist = httpConnectionService.downloadPList(url, keys, values, HTTP_CALLING_METHOD.POST);
		} catch (GeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (plist != null)
		{
			FanclResultParser fanclResultParser = new FanclResultParser();
			return fanclResultParser.parseGeneralResult(plist);
		}
		else
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, Constants.DOWNLOAD_RETURN_NULL_GERERAL_MESSAGE);
		}
	}



}
