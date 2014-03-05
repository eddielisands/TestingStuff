package com.fancl.iloyalty.asynctask;

import android.os.AsyncTask;

import com.fancl.iloyalty.asynctask.callback.ValidationAsyncTaskCallback;
import com.fancl.iloyalty.exception.GeneralException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.pojo.ValidateUserParam;
import com.fancl.iloyalty.responseimpl.ValidationResult;
import com.fancl.iloyalty.service.AccountService;
import com.fancl.iloyalty.util.LogController;

public class ValidationAsyncTask extends AsyncTask<ValidateUserParam, Void, ValidationResult> {
	
	private AccountService accountService;
	private ValidationAsyncTaskCallback accountServiceAsyncTaskCallback;

	public ValidationAsyncTask(ValidationAsyncTaskCallback accountServiceAsyncTaskCallback)
	{
		this.accountService = CustomServiceFactory.getAccountService();
		this.accountServiceAsyncTaskCallback = accountServiceAsyncTaskCallback;
	}
	
	@Override
	protected void onPreExecute () {
		super.onPreExecute();
		//process of thread before start(UI Thread)
	}
	
	@Override
	protected ValidationResult doInBackground(ValidateUserParam... params) {
		//process of thread(background thread)
		try
		{
			return accountService.validateUserWithMemberId(params[0]);
		}
		catch (GeneralException e)
		{
			LogController.log("return this.membershipAccountService.validateUserWithMemberId() error");
			e.printStackTrace();
			return null;
		}		
	}

	@Override
	protected void onPostExecute (ValidationResult validationResult) {
		super.onPostExecute(validationResult);
		//process of thread ended(UI Thread)
		LogController.log("Finish validateUserWithMemberId");
		if(accountServiceAsyncTaskCallback != null && validationResult != null)
		{
			LogController.log("userRegisterAsyncTaskCallback " + validationResult);
			accountServiceAsyncTaskCallback.onPostUserValidateUserAsyncTaskCallback(validationResult);
		}
		else {
			
		}
	}

}
