package com.fancl.iloyalty.service;

import com.fancl.iloyalty.asynctask.callback.DatabaseDownloadAsyncTaskCallback;
import com.fancl.iloyalty.asynctask.callback.TillIdDatabaseDownloadAsyncTaskCallback;
import com.fancl.iloyalty.exception.GeneralException;
import com.fancl.iloyalty.pojo.DatabaseVersionCheckResult;

public interface DatabaseDownloadService {
	public DatabaseVersionCheckResult dbVersionChecking(String link) throws GeneralException;

	public boolean isNeedDownloadDb(DatabaseVersionCheckResult databaseVersionCheckResult);

	public boolean databaseDownloadProcessing(String dbLink, String savePath);

	public void addDatabaseDownloadServiceCallback(DatabaseDownloadAsyncTaskCallback callback);

	public void removeDatabaseDownloadServiceCallback(DatabaseDownloadAsyncTaskCallback callback);

	public boolean isNeedDownloadTillIdDb(DatabaseVersionCheckResult databaseVersionCheckResult);

	public boolean tillIdDatabaseDownloadProcessing(String dbLink, String savePath);

	public void addTillIdDatabaseDownloadServiceCallback(TillIdDatabaseDownloadAsyncTaskCallback callback);

	public void removeTillIdDatabaseDownloadServiceCallback(TillIdDatabaseDownloadAsyncTaskCallback callback);
}
