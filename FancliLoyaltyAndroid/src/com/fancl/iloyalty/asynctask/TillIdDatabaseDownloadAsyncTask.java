package com.fancl.iloyalty.asynctask;

import android.os.AsyncTask;

import com.fancl.iloyalty.AndroidProjectApplication;
import com.fancl.iloyalty.asynctask.callback.TillIdDatabaseDownloadAsyncTaskCallback;
import com.fancl.iloyalty.exception.GeneralException;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.pojo.DatabaseVersionCheckResult;
import com.fancl.iloyalty.service.DatabaseDownloadService;
import com.fancl.iloyalty.util.LogController;
import com.fancl.iloyalty.util.NetworkConnective;

public class TillIdDatabaseDownloadAsyncTask extends AsyncTask<Void, Void, Boolean>{

	private TillIdDatabaseDownloadAsyncTaskCallback callback;
	
	private DatabaseDownloadService databaseDownloadService;

	private String databaseVersionCheckingLink;
	private String savePath;
	
	private boolean isNoNeedDownloadDb = false;
	
	public TillIdDatabaseDownloadAsyncTask(String databaseVersionCheckingLink, String savePath, TillIdDatabaseDownloadAsyncTaskCallback callback)
	{
		this.databaseDownloadService = GeneralServiceFactory.getDatabaseDownloadService();
		
		this.databaseVersionCheckingLink = databaseVersionCheckingLink;
		this.savePath = savePath;
		this.callback = callback;
	}
	
	@Override
	protected void onPreExecute () {
		super.onPreExecute();
		//process of thread before start(UI Thread)
		if(NetworkConnective.checkNetwork(AndroidProjectApplication.application))
		{
			databaseDownloadService.addTillIdDatabaseDownloadServiceCallback(callback);
		}
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		//process of thread(background thread)
		
		LogController.log("TillIdDatabaseDownloadAsyncTask doInBackground >>> " + params[0]);
		
		if(NetworkConnective.checkNetwork(AndroidProjectApplication.application))
		{
			try
			{
				DatabaseVersionCheckResult databaseVersionCheckResult = databaseDownloadService.dbVersionChecking(databaseVersionCheckingLink);
				
				if(databaseVersionCheckResult != null)
				{
					if(databaseDownloadService.isNeedDownloadTillIdDb(databaseVersionCheckResult))
					{
						boolean successDownload = databaseDownloadService.tillIdDatabaseDownloadProcessing(databaseVersionCheckResult.getDbLink(), savePath);
					
						if(successDownload)
						{
							return Boolean.valueOf(true);
						}
						else
						{
							return Boolean.valueOf(false);
						}
					}
					else
					{
						isNoNeedDownloadDb = true;
						return Boolean.valueOf(false);
					}
				}
				else
				{
					return Boolean.valueOf(false);
				}
			}
			catch (GeneralException e)
			{
				return Boolean.valueOf(false);
			}
		}
		
		return Boolean.valueOf(false);
	}

	@Override
	protected void onPostExecute (Boolean isSuccess) {
		super.onPostExecute(isSuccess);
		//process of thread ended(UI Thread)
		
		if(callback != null)
		{
			if(isSuccess.booleanValue())
			{
				callback.tillIdDatabaseDownloadFinished();
			}
			else
			{
				if(isNoNeedDownloadDb)
				{
					callback.noNeedToDownloadNewTillIdDatabase();
				}
				else
				{
					callback.tillIdDatabaseDownloadFailed();
				}
			}
			
			databaseDownloadService.removeTillIdDatabaseDownloadServiceCallback(callback);
		}
	}
}
