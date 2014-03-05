package com.fancl.iloyalty.asynctask;

import android.os.AsyncTask;

import com.fancl.iloyalty.asynctask.callback.EarnCreditAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.service.AccountService;

public class EarnCreditAsyncTask extends AsyncTask<String, Void, Object>{
	private AccountService accountService;
	private EarnCreditAsyncTaskCallback earnCreditAsyncTaskCallback;

	public EarnCreditAsyncTask(EarnCreditAsyncTaskCallback earnCreditAsyncTaskCallback)
	{
		this.accountService = CustomServiceFactory.getAccountService();
		this.earnCreditAsyncTaskCallback = earnCreditAsyncTaskCallback;
	}

	@Override
	protected void onPreExecute () {
		super.onPreExecute();
		//process of thread before start(UI Thread)
	}

	@Override
	protected Object doInBackground(String... params) {
		//process of thread(background thread)

		try
		{
			return accountService.earnCreditWithEventId(params[0]);
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

		if(earnCreditAsyncTaskCallback != null)
		{
			earnCreditAsyncTaskCallback.onPostExecuteCallback(results);
		}
	}
}
