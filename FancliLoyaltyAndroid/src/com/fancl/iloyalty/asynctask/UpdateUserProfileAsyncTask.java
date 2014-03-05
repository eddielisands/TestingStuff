package com.fancl.iloyalty.asynctask;

import android.os.AsyncTask;

import com.fancl.iloyalty.asynctask.callback.UpdateUserProfileAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.pojo.User;
import com.fancl.iloyalty.responseimpl.FanclGeneralResult;
import com.fancl.iloyalty.util.LogController;

public class UpdateUserProfileAsyncTask extends AsyncTask<User, Void, FanclGeneralResult>{

private UpdateUserProfileAsyncTaskCallback updateUserProfileAsyncTaskCallback;
	
	public UpdateUserProfileAsyncTask(UpdateUserProfileAsyncTaskCallback updateUserProfileAsyncTaskCallback)
	{
		this.updateUserProfileAsyncTaskCallback = updateUserProfileAsyncTaskCallback;
	}
	
	@Override
	protected void onPreExecute () {
		super.onPreExecute();
		//process of thread before start(UI Thread)
	}
	
	@Override
	protected FanclGeneralResult doInBackground(User... params) {
		//process of thread(background thread)
		
		LogController.log("ExampleAsyncTask doInBackground >>> " + params[0]);
		
		try
		{
			return CustomServiceFactory.getAccountService().updateUserProfile(params[0]);
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
		
		if(updateUserProfileAsyncTaskCallback != null)
		{
			updateUserProfileAsyncTaskCallback.onPostExecuteCallback(results);
		}
	}
}
