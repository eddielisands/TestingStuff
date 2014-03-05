package com.fancl.iloyalty.asynctask;

import android.os.AsyncTask;

import com.fancl.iloyalty.asynctask.callback.GetGPRewardHistoryItemAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.pojo.GPRewardHistoryItem;
import com.fancl.iloyalty.util.LogController;

public class GetGPRewardHistoryItemAsyncTask extends AsyncTask<String, Void, GPRewardHistoryItem>{
	
	private GetGPRewardHistoryItemAsyncTaskCallback getGPRewardHistoryItemAsyncTaskCallback;
	
	public GetGPRewardHistoryItemAsyncTask(GetGPRewardHistoryItemAsyncTaskCallback getGPRewardHistoryItemAsyncTaskCallback)
	{
		this.getGPRewardHistoryItemAsyncTaskCallback = getGPRewardHistoryItemAsyncTaskCallback;
	}
	
	@Override
	protected void onPreExecute () {
		super.onPreExecute();
		//process of thread before start(UI Thread)
	}

	@Override
	protected GPRewardHistoryItem doInBackground(String... params) {
		// TODO Auto-generated method stub
		
         LogController.log("ExampleAsyncTask doInBackground >>> " + params[0]);
		
		try
		{
			return CustomServiceFactory.getAccountService().getGPRewardsHistoryItem(params[0], params[1], params[2], params[3]);
		}
		catch (FanclException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	protected void onPostExecute (GPRewardHistoryItem results) {
		super.onPostExecute(results);
		//process of thread ended(UI Thread)
		
		if(getGPRewardHistoryItemAsyncTaskCallback != null)
		{
			getGPRewardHistoryItemAsyncTaskCallback.onPostExecuteCallback(results);
		}
	}

}
