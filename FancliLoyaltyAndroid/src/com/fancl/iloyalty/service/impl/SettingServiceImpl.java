package com.fancl.iloyalty.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.text.format.DateFormat;

import com.fancl.iloyalty.AndroidProjectApplication;
import com.fancl.iloyalty.ApiConstant;
import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.Constants.HTTP_CALLING_METHOD;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.exception.GeneralException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.parser.FanclResultParser;
import com.fancl.iloyalty.pojo.ReceiptSetting;
import com.fancl.iloyalty.pojo.Version;
import com.fancl.iloyalty.responseimpl.FanclGeneralResult;
import com.fancl.iloyalty.service.AccountService;
import com.fancl.iloyalty.service.HttpConnectionService;
import com.fancl.iloyalty.service.SettingService;
import com.fancl.iloyalty.util.HttpUtil;
import com.fancl.iloyalty.util.LogController;
import com.longevitysoft.android.xml.plist.domain.PList;

public class SettingServiceImpl implements SettingService {

	@Override
	public Object getUserReceiptSetting()
			throws FanclException {
		// TODO Auto-generated method stub
		String url = ApiConstant.getAPI(ApiConstant.USER_GET_RECEIPT_API);

		String[] keys = new String[] { "fanclMemberId", "language", "systemType" };
		String[] values = new String[] { CustomServiceFactory.getAccountService().currentMemberId(), CustomServiceFactory.getAccountService().currentLanguage(), "A" };

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
			Object object = fanclResultParser.parseGetReceiptResult(plist);
			if (object instanceof ReceiptSetting) {
				return (ReceiptSetting) object;
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

	@Override
	public FanclGeneralResult setUserReceiptSetting(String printBool,
			String emailBool) throws FanclException {
		// TODO Auto-generated method stub
		String url = ApiConstant.getAPI(ApiConstant.USER_SET_RECEIPT_API);

		String[] keys = new String[] { "fanclMemberId", "printReceipt", "emailReceipt", "language", "systemType" };
		String[] values = new String[] { CustomServiceFactory.getAccountService().currentMemberId(), "N", emailBool, CustomServiceFactory.getAccountService().currentLanguage(), "A" };

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
	public Version currentDatabaseVersion() throws FanclException {
		// TODO Auto-generated method stub
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{
				String table = "version";
				String[] columns = {};

				String selection = "";
				String[] selectionArgs = {};
				String groupBy = "";
				String having = "";
				String orderBy = "id";

				c = dB.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

				String version_major = "";
				String version_minor = "";
				String version_revision = "";
				String issue = "";
				String create_datetime = "";

				c.moveToFirst();
				while (!c.isAfterLast()) {
					version_major = c.getString(c.getColumnIndex("version_major"));
					version_minor = c.getString(c.getColumnIndex("version_minor"));
					version_revision = c.getString(c.getColumnIndex("version_revision"));
					issue = c.getString(c.getColumnIndex("issue"));
					create_datetime = c.getString(c.getColumnIndex("create_datetime"));

					c.moveToNext();
				}

				Version version = new Version(version_major, version_minor, version_revision, issue, create_datetime);

				c.close();
				c = null;

				return version;
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
	public void addUserLogWithSection(String aSection, String aSectionSubCat,
			String aType, String aContentId, String aContentName, String aEvent, String aDetail) throws FanclException {
		// TODO Auto-generated method stub
		Exception exception = null;
		SQLiteDatabase dB = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getUserLogDatabase();
			if (dB != null)
			{
				String section = "";
				if (aSection != null) {
					section = aSection;
				}
				String sectionSubCat = "";
				if (aSectionSubCat != null) {
					sectionSubCat = aSectionSubCat;
				}
				String type = "";
				if (aType != null) {
					type = aType;
				}
				String contentId = "";
				if (aContentId != null) {
					contentId = aContentId;
				}
				String contentName = "";
				if (aContentName != null) {
					contentName = aContentName;
				}
				String event = "";
				if (aEvent != null) {
					event = aEvent;
				}
				String detail = "";
				if (aDetail != null) {
					detail = aDetail;
				}

				LogController.log("dB != null");
				Date now = new Date();
				String nowFullStr = DateFormat.format("yyyy-MM-dd hh:mm:ss", now).toString();

				String tmpContentName = contentName.replace("'", " ");

				String insertValue = "('" + section + "', '" + sectionSubCat + "', '" + type + "', '" + contentId + "', '" + tmpContentName + "', '" + event + "', '" + detail + "', '" + nowFullStr + "', '" + CustomServiceFactory.getAccountService().currentMemberId() + "')";
				String sql = "";
				sql = "INSERT INTO userLog (section, section_sub_cat, type, content_id, content_name, event, detail, log_datetime, member_sk) VALUES " + insertValue + ";";

				dB.execSQL(sql);
				LogController.log("userlog added-"+"Section:"+aSection+",SectionSubCat:"+aSectionSubCat+",Type:"+aType+",ContentId:"+aContentId+",ContentName:"+aContentName+",Event:"+aEvent+",Detail:"+aDetail);
				
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
			LogController.log("userlog cannot add");
		}

		if(exception != null)
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, exception.getMessage());
		}
	}

	@Override
	public void clearUserLog() throws FanclException {
		// TODO Auto-generated method stub
		Exception exception = null;
		SQLiteDatabase dB = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getUserLogDatabase();
			if (dB != null)
			{
				LogController.log("dB != null");
				String sql = "DELETE from userLog;";
				dB.execSQL(sql);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
		}

		if(exception != null)
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, exception.getMessage());
		}
	}

	@Override
	public void submitUserLog()
			throws FanclException {
		String url = ApiConstant.getAPI(ApiConstant.DATA_UPLOAD_API);
		AccountService accountService = CustomServiceFactory.getAccountService();

		String[] keyList = new String[]{"fanclMemberId", "language", "systemType"};
		List<String> keys = new ArrayList<String>();
		for (int i = 0; i < keyList.length; i++) {
			keys.add(keyList[i]);
		}
		String[] valueList = new String[]{accountService.currentMemberId(), accountService.currentLanguage(), "A"};
		List<String> values = new ArrayList<String>();
		for (int i = 0; i < valueList.length; i++) {
			values.add(valueList[i]);
		}
		String[] fileKeyList = new String[]{"file"};
		List<String> fileKeys = new ArrayList<String>();
		for (int i = 0; i < fileKeyList.length; i++) {
			fileKeys.add(fileKeyList[i]);
		}
		File sqlFile = new File(Constants.DATABASE_FOLDER + Constants.LOG_DATABASE_FILE_NAME);
		List<File> files = new ArrayList<File>();
		files.add(sqlFile);

		try {
			String isUploaded = HttpUtil.uploadFileWithParams(url, keys, values, fileKeys, files);
			LogController.log("isUploaded: " + isUploaded);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void countAdHitRateWithBannerId(String bannerId) throws FanclException {
		String url = ApiConstant.getAPI(ApiConstant.AD_HIT_RATE_API);

		String[] keys = new String[]{"id", "systemType"};
		String[] values = new String[]{bannerId, "A"};

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

		}
		else
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, Constants.DOWNLOAD_RETURN_NULL_GERERAL_MESSAGE);
		}
	}

	@Override
	public void updateReadContentToDatabase(String type, String itemId)
			throws FanclException {
		SharedPreferences sharedPreferences = AndroidProjectApplication.application.getSharedPreferences(
				Constants.SHARED_PREFERENCE_APPLICATION_KEY,
				Context.MODE_PRIVATE);
		if (type.equals("hot_item")) {
			String hotItemArrayString = sharedPreferences.getString(Constants.HOT_ITEM_READ_ARRAY_KEY,
					null);
			if (hotItemArrayString != null) {
				boolean isFound = false;
				String[] itemIdList = hotItemArrayString.split(",");
				List<String> idList = new ArrayList<String>();
				for (int i = 0; i < itemIdList.length; i++) {
					idList.add(itemIdList[i]);
				}
				for (int i = 0; i < idList.size(); i++) {
					String id = idList.get(i);
					if (itemId.equals(id)) {
						isFound = true;
						break;
					}
				}
				if (!isFound) {
					idList.add(itemId);
				}
				
				String tmpHotItemArrayString = idList.get(0);
				if (idList.size() > 1) {
					for (int i = 1; i < idList.size(); i++) {
						tmpHotItemArrayString = tmpHotItemArrayString + "," + idList.get(i);
					}
				}
				hotItemArrayString = tmpHotItemArrayString;
			}
			else {
				hotItemArrayString = itemId;
			}
			sharedPreferences.edit().putString(Constants.HOT_ITEM_READ_ARRAY_KEY, hotItemArrayString).commit();
		}
		else if (type.equals("promotion")) {
			String promotionArrayString = sharedPreferences.getString(Constants.PROMOTION_READ_ARRAY_KEY,
					null);
			if (promotionArrayString != null) {
				boolean isFound = false;
				String[] itemIdList = promotionArrayString.split(",");
				List<String> idList = new ArrayList<String>();
				for (int i = 0; i < itemIdList.length; i++) {
					idList.add(itemIdList[i]);
				}
				for (int i = 0; i < idList.size(); i++) {
					String id = idList.get(i);
					if (itemId.equals(id)) {
						isFound = true;
						break;
					}
				}
				if (!isFound) {
					idList.add(itemId);
				}
				
				String tmpPromotionArrayString = idList.get(0);
				if (idList.size() > 1) {
					for (int i = 1; i < idList.size(); i++) {
						tmpPromotionArrayString = tmpPromotionArrayString + "," + idList.get(i);
					}
				}
				promotionArrayString = tmpPromotionArrayString;
			}
			else {
				promotionArrayString = itemId;
			}
			sharedPreferences.edit().putString(Constants.HOT_ITEM_READ_ARRAY_KEY, promotionArrayString).commit();
		}
		else if (type.equals("ichannel_magazine")) {
			String ichannelArrayString = sharedPreferences.getString(Constants.ICHANNEL_READ_ARRAY_KEY,
					null);
			if (ichannelArrayString != null) {
				boolean isFound = false;
				String[] itemIdList = ichannelArrayString.split(",");
				List<String> idList = new ArrayList<String>();
				for (int i = 0; i < itemIdList.length; i++) {
					idList.add(itemIdList[i]);
				}
				for (int i = 0; i < idList.size(); i++) {
					String id = idList.get(i);
					if (itemId.equals(id)) {
						isFound = true;
						break;
					}
				}
				if (!isFound) {
					idList.add(itemId);
				}
				
				String tmpIchannelArrayString = idList.get(0);
				if (idList.size() > 1) {
					for (int i = 1; i < idList.size(); i++) {
						tmpIchannelArrayString = tmpIchannelArrayString + "," + idList.get(i);
					}
				}
				ichannelArrayString = tmpIchannelArrayString;
			}
			else {
				ichannelArrayString = itemId;
			}
			sharedPreferences.edit().putString(Constants.HOT_ITEM_READ_ARRAY_KEY, ichannelArrayString).commit();
		}

		Exception exception = null;
		SQLiteDatabase dB = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{
				LogController.log("dB != null");
				String sql = "UPDATE " + type + " SET is_read = 'Y' where id = '" + itemId + "';";
				LogController.log("updateChatLogWithItemId:" +  sql);
				dB.execSQL(sql);

				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						AndroidProjectApplication.application.reloadAllActivityTabBar();
					}
				}, 500);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
		}

		if(exception != null)
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, exception.getMessage());
		}
	}
	
	@Override
	public void updateDatabaseContentAfterUpdating(String type, String itemId) throws FanclException {
		Exception exception = null;
		SQLiteDatabase dB = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getSQLiteDatabase();
			if (dB != null)
			{
				LogController.log("dB != null");
				String sql = "UPDATE " + type + " SET is_read = 'Y' where id = '" + itemId + "';";
				LogController.log("updateChatLogWithItemId:" +  sql);
				dB.execSQL(sql);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exception = e;
		}

		if(exception != null)
		{
			throw new FanclException(Constants.STATUS_CODE_FAIL, exception.getMessage());
		}
	}

	@Override
	public Version currentTillIdDatabaseVersion() throws FanclException {
		// TODO Auto-generated method stub
		Exception exception = null;
		SQLiteDatabase dB = null;
		Cursor c = null;

		try {
			dB = GeneralServiceFactory.getSQLiteDatabaseService().getTillIdDatabase();
			if (dB != null)
			{
				String table = "version";
				String[] columns = {};

				String selection = "";
				String[] selectionArgs = {};
				String groupBy = "";
				String having = "";
				String orderBy = "id";

				c = dB.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

				String version_major = "";
				String version_minor = "";
				String version_revision = "";
				String issue = "";
				String create_datetime = "";

				c.moveToFirst();
				while (!c.isAfterLast()) {
					version_major = c.getString(c.getColumnIndex("version_major"));
					version_minor = c.getString(c.getColumnIndex("version_minor"));
					version_revision = c.getString(c.getColumnIndex("version_revision"));
					issue = c.getString(c.getColumnIndex("issue"));
					create_datetime = c.getString(c.getColumnIndex("create_datetime"));

					c.moveToNext();
				}

				Version version = new Version(version_major, version_minor, version_revision, issue, create_datetime);

				c.close();
				c = null;

				return version;
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
}
