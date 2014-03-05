package com.fancl.iloyalty.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.fancl.iloyalty.AndroidProjectApplication;
import com.fancl.iloyalty.Constants.HTTP_CALLING_METHOD;
import com.fancl.iloyalty.asynctask.callback.DatabaseDownloadAsyncTaskCallback;
import com.fancl.iloyalty.asynctask.callback.TillIdDatabaseDownloadAsyncTaskCallback;
import com.fancl.iloyalty.exception.GeneralException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.parser.DatabaseCheckingParser;
import com.fancl.iloyalty.pojo.DatabaseVersionCheckResult;
import com.fancl.iloyalty.pojo.Version;
import com.fancl.iloyalty.service.DatabaseDownloadService;
import com.fancl.iloyalty.service.HttpConnectionService;
import com.fancl.iloyalty.service.callback.HttpDownloadFileCallback;
import com.fancl.iloyalty.util.HttpUtil;
import com.fancl.iloyalty.util.LogController;
import com.longevitysoft.android.xml.plist.domain.PList;

public class DatabaseDownloadServiceImpl implements DatabaseDownloadService, HttpDownloadFileCallback {
	/**
	 * Read Me:
	 * 	NEED TO IMPLEMENT classes/methods
	 * 		1.	com.[package].pojo.DatabaseVersionCheckResult - [parameters]
	 * 		2.	com.[package].service.impl.DatabaseDownloadServiceImpl - isNeedDownloadDb(DatabaseVersionCheckResult databaseVersionCheckResult)
	 * 		3.	com.[package].parser.DatabaseCheckingParser - parseDatabaseChecking(Document document)
	 */

	private List<DatabaseDownloadAsyncTaskCallback> callbackList = new ArrayList<DatabaseDownloadAsyncTaskCallback>();
	private List<TillIdDatabaseDownloadAsyncTaskCallback> tillIdcallbackList = new ArrayList<TillIdDatabaseDownloadAsyncTaskCallback>();


	@Override
	public DatabaseVersionCheckResult dbVersionChecking(String link) throws GeneralException {

		if(link != null)
		{
			String[] keys = new String[]{};
			String[] values = new String[]{};

			HttpConnectionService httpConnectionService = GeneralServiceFactory.getHttpConnectionService(AndroidProjectApplication.application);
			PList plist = httpConnectionService.downloadPList(link, keys, values, HTTP_CALLING_METHOD.POST);

			if(plist != null)
			{
				DatabaseCheckingParser databaseCheckingParser = new DatabaseCheckingParser();
				return databaseCheckingParser.parseDatabaseChecking(plist);
			}
			else
			{
				throw new GeneralException("Failed to parser database checking.");
			}
		}

		return null;
	}

	@Override
	public boolean isNeedDownloadDb(DatabaseVersionCheckResult databaseVersionCheckResult) {
		/**
		 * Need to implement the logic of checking download new database or not
		 */

		boolean updateDatabase = false;
		Version version = this.loadDatabaseVersionFromDatabase();
		if (version != null) {
			LogController.log("check-(device)version issue: " + version.getIssue());
			LogController.log("check-(device)version version: " + version.getVersionMajor() + "." + version.getVersionMinor() + "." + version.getVersionRevision());
			
			LogController.log("check-latest version issue: " + databaseVersionCheckResult.getIssue());
			LogController.log("check-latest version version: " + databaseVersionCheckResult.getVersion());

			if (!(databaseVersionCheckResult.getIssue().equals(version.getIssue()))) {
				updateDatabase = true;
			} else {
				String dbVersion = (version.getVersionMajor() + "." + version.getVersionMinor() + "." + version.getVersionRevision());
				if (!(databaseVersionCheckResult.getVersion().equals(dbVersion))) {
					updateDatabase = true;
				}
			}
		} else {
			LogController.log("0. version = null");
			updateDatabase = true;
		}

		return updateDatabase;
	}

	private Version loadDatabaseVersionFromDatabase() {
		Version version = null;
		try {
			version = CustomServiceFactory.getSettingService()
					.currentDatabaseVersion();
		} catch (GeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return version;
	}

	@Override
	public boolean databaseDownloadProcessing(String dbLink, String savePath) {

		//inform all callback download database is starting
		for(DatabaseDownloadAsyncTaskCallback callback : callbackList)
		{
			if(callback != null)
			{
				callback.databaseStartDownloading();
			}
		}

		boolean isSuccess = HttpUtil.downloadFile(dbLink, savePath, this);

		return isSuccess;
	}

	@Override
	public void addDatabaseDownloadServiceCallback(DatabaseDownloadAsyncTaskCallback callback) {
		if(callback != null)
		{
			callbackList.add(callback);
		}
	}

	@Override
	public void removeDatabaseDownloadServiceCallback(DatabaseDownloadAsyncTaskCallback callback) {
		if(callback != null)
		{
			callbackList.remove(callback);
		}
	}

	@Override
	public void currentProgress(int progress) {
		for(DatabaseDownloadAsyncTaskCallback callback : callbackList)
		{
			if(callback != null)
			{
				callback.databaseDownloadingProgress(progress);
			}
		}
		
		for(TillIdDatabaseDownloadAsyncTaskCallback callback : tillIdcallbackList)
		{
			if(callback != null)
			{
				callback.tillIdDatabaseDownloadingProgress(progress);
			}
		}
	}

	@Override
	public boolean isNeedDownloadTillIdDb(
			DatabaseVersionCheckResult databaseVersionCheckResult) {
		// TODO Auto-generated method stub
		boolean updateDatabase = false;
		Version version = this.loadTillIdDatabaseVersionFromDatabase();
		if (version != null) {
			LogController.log("0. version issue: " + version.getIssue());
			LogController.log("0. version version: " + version.getVersionMajor() + "." + version.getVersionMinor() + "." + version.getVersionRevision());

			if (!(databaseVersionCheckResult.getIssue().equals(version.getIssue()))) {
				updateDatabase = true;
			} else {
				String dbVersion = (version.getVersionMajor() + "." + version.getVersionMinor() + "." + version.getVersionRevision());
				if (!(databaseVersionCheckResult.getVersion().equals(dbVersion))) {
					updateDatabase = true;
				}
			}
		} else {
			LogController.log("0. version = null");
			updateDatabase = true;
		}

		return updateDatabase;
	}

	private Version loadTillIdDatabaseVersionFromDatabase() {
		Version version = null;
		try {
			version = CustomServiceFactory.getSettingService()
					.currentDatabaseVersion();
		} catch (GeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return version;
	}

	@Override
	public boolean tillIdDatabaseDownloadProcessing(String dbLink,
			String savePath) {
		//inform all callback download database is starting
		for(TillIdDatabaseDownloadAsyncTaskCallback callback : tillIdcallbackList)
		{
			if(callback != null)
			{
				callback.tillIdDatabaseStartDownloading();
			}
		}

		boolean isSuccess = HttpUtil.downloadFile(dbLink, savePath, this);

		return isSuccess;
	}

	@Override
	public void addTillIdDatabaseDownloadServiceCallback(
			TillIdDatabaseDownloadAsyncTaskCallback callback) {
		if(callback != null)
		{
			tillIdcallbackList.add(callback);
		}
	}

	@Override
	public void removeTillIdDatabaseDownloadServiceCallback(
			TillIdDatabaseDownloadAsyncTaskCallback callback) {
		if(callback != null)
		{
			tillIdcallbackList.remove(callback);
		}
	}

}
