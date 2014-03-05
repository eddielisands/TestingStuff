package com.fancl.iloyalty.asynctask;

import android.os.AsyncTask;

import com.fancl.iloyalty.asynctask.callback.GetGPRewardAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.util.LogController;

public class GetGPRewardAsyncTask extends AsyncTask<String, Void, Object>{

private GetGPRewardAsyncTaskCallback getGPRewardAsyncTaskCallback;
	
	public GetGPRewardAsyncTask(GetGPRewardAsyncTaskCallback getGPRewardAsyncTaskCallback)
	{
		this.getGPRewardAsyncTaskCallback = getGPRewardAsyncTaskCallback;
	}
	
	@Override
	protected void onPreExecute () {
		super.onPreExecute();
		//process of thread before start(UI Thread)
	}
	
	@Override
	protected Object doInBackground(String... params) {
		//process of thread(background thread)
		
		LogController.log("ExampleAsyncTask doInBackground >>> ");
		
		try
		{
			return CustomServiceFactory.getAccountService().getGPRewards();
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
		
		if(getGPRewardAsyncTaskCallback != null)
		{
			getGPRewardAsyncTaskCallback.onPostExecuteCallback(results);
		}
	}
}
