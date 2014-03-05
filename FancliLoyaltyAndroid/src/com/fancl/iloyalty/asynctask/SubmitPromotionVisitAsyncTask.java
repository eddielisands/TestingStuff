package com.fancl.iloyalty.asynctask;

import android.os.AsyncTask;

import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.responseimpl.FanclGeneralResult;
import com.fancl.iloyalty.util.LogController;

public class SubmitPromotionVisitAsyncTask extends AsyncTask<String, Void, FanclGeneralResult>{

	
	public SubmitPromotionVisitAsyncTask()
	{
	}
	
	@Override
	protected void onPreExecute () {
		super.onPreExecute();
		//process of thread before start(UI Thread)
	}
	
	@Override
	protected FanclGeneralResult doInBackground(String... params) {
		//process of thread(background thread)
		
		LogController.log("ExampleAsyncTask doInBackground >>> " + params[0]);
		
		try
		{
			return CustomServiceFactory.getPromotionService().submitPromotionVisitWithCode(params[0]);
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
		
		if (results != null) {
			LogController.log("getStatus: " + results.getStatus());
		}
	}
}
