package com.fancl.iloyalty.asynctask;

import android.os.AsyncTask;

import com.fancl.iloyalty.asynctask.callback.GetNotificationListAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.service.AccountService;
import com.fancl.iloyalty.util.LogController;

public class GetNotificationListAsyncTask extends AsyncTask<String, Void, Object>{
	private AccountService accountService;
	private GetNotificationListAsyncTaskCallback messageAsyncTaskCallback;

	public GetNotificationListAsyncTask(GetNotificationListAsyncTaskCallback messageAsyncTaskCallback)
	{
		this.accountService = CustomServiceFactory.getAccountService();
		this.messageAsyncTaskCallback = messageAsyncTaskCallback;
	}

	@Override
	protected void onPreExecute () {
		super.onPreExecute();
		//process of thread before start(UI Thread)
	}

	@Override
	protected Object doInBackground(String... params) {
		//process of thread(background thread)

		LogController.log("RegistrationAsyncTask doInBackground >>> ");

		try
		{
			return accountService.getNotificationList();
		}
		catch (FanclException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute (Object results) {
		super.onPostExecute(results);
		//process of thread ended(UI Thread)

		if(messageAsyncTaskCallback != null)
		{
			messageAsyncTaskCallback.onPostExecuteCallback(results);
		}
	}

}
