package com.fancl.iloyalty.asynctask;

import android.os.AsyncTask;

import com.fancl.iloyalty.asynctask.callback.LoginAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.responseimpl.FanclGeneralResult;
import com.fancl.iloyalty.service.AccountService;
import com.fancl.iloyalty.util.LogController;

public class LoginAsyncTask extends AsyncTask<String, Void, FanclGeneralResult>{
	private AccountService accountService;
	private LoginAsyncTaskCallback loginAsyncTaskCallback;

	public LoginAsyncTask(LoginAsyncTaskCallback loginAsyncTaskCallback)
	{
		this.accountService = CustomServiceFactory.getAccountService();
		this.loginAsyncTaskCallback = loginAsyncTaskCallback;
	}

	@Override
	protected void onPreExecute () {
		super.onPreExecute();
		//process of thread before start(UI Thread)
	}

	@Override
	protected FanclGeneralResult doInBackground(String... params) {
		//process of thread(background thread)

		LogController.log("LoginAsyncTask doInBackground >>> " + params[0]);

		try
		{
			return accountService.loginWithEmail(params[0], params[1]);
		}
		catch (FanclException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute (FanclGeneralResult results) {
		super.onPostExecute(results);
		//process of thread ended(UI Thread)

		if(loginAsyncTaskCallback != null)
		{
			loginAsyncTaskCallback.onPostExecuteCallback(results);
		}
	}
}
