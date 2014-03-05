package com.fancl.iloyalty.asynctask;

import android.os.AsyncTask;

import com.fancl.iloyalty.asynctask.callback.RegistrationAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.pojo.UserRegistrationParam;
import com.fancl.iloyalty.responseimpl.FanclGeneralResult;
import com.fancl.iloyalty.service.AccountService;
import com.fancl.iloyalty.util.LogController;

public class RegistrationAsyncTask extends AsyncTask<UserRegistrationParam, Void, FanclGeneralResult>{
	private AccountService accountService;
	private RegistrationAsyncTaskCallback registrationAsyncTaskCallback;

	public RegistrationAsyncTask(RegistrationAsyncTaskCallback registrationAsyncTaskCallback)
	{
		this.accountService = CustomServiceFactory.getAccountService();
		this.registrationAsyncTaskCallback = registrationAsyncTaskCallback;
	}

	@Override
	protected void onPreExecute () {
		super.onPreExecute();
		//process of thread before start(UI Thread)
	}

	@Override
	protected FanclGeneralResult doInBackground(UserRegistrationParam... params) {
		//process of thread(background thread)

		LogController.log("RegistrationAsyncTask doInBackground >>> " + params[0]);

		try
		{
			return accountService.registerUserWithMemberId(params[0]);
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

		if(registrationAsyncTaskCallback != null)
		{
			registrationAsyncTaskCallback.onPostExecuteCallback(results);
		}
	}
}
