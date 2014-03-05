package com.fancl.iloyalty.asynctask;

import android.os.AsyncTask;

import com.fancl.iloyalty.asynctask.callback.GetUserProfileAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.pojo.User;
import com.fancl.iloyalty.util.LogController;

public class GetUserProfileAsyncTask extends AsyncTask<String, Void, User>{

private GetUserProfileAsyncTaskCallback getUserProfileAsyncTaskCallback;
	
	public GetUserProfileAsyncTask(GetUserProfileAsyncTaskCallback getUserProfileAsyncTaskCallback)
	{
		this.getUserProfileAsyncTaskCallback = getUserProfileAsyncTaskCallback;
	}
	
	@Override
	protected void onPreExecute () {
		super.onPreExecute();
		//process of thread before start(UI Thread)
	}
	
	@Override
	protected User doInBackground(String... params) {
		//process of thread(background thread)
		
		LogController.log("GetUserProfileAsyncTask doInBackground >>> ");
		
		try
		{
			return CustomServiceFactory.getAccountService().getUserProfile();
		}
		catch (FanclException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected void onPostExecute (User results) {
		super.onPostExecute(results);
		//process of thread ended(UI Thread)
		
		if(getUserProfileAsyncTaskCallback != null)
		{
			getUserProfileAsyncTaskCallback.onPostExecuteCallback(results);
		}
	}
}
