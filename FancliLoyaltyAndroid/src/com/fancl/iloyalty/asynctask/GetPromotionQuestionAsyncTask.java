package com.fancl.iloyalty.asynctask;

import android.os.AsyncTask;

import com.fancl.iloyalty.asynctask.callback.GetPromotionQuestionAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.util.LogController;

public class GetPromotionQuestionAsyncTask extends AsyncTask<String, Void, Object>{

private GetPromotionQuestionAsyncTaskCallback getPromotionQuestionAsyncTaskCallback;
	
	public GetPromotionQuestionAsyncTask(GetPromotionQuestionAsyncTaskCallback getPromotionQuestionAsyncTaskCallback)
	{
		this.getPromotionQuestionAsyncTaskCallback = getPromotionQuestionAsyncTaskCallback;
	}
	
	@Override
	protected void onPreExecute () {
		super.onPreExecute();
		//process of thread before start(UI Thread)
	}
	
	@Override
	protected Object doInBackground(String... params) {
		//process of thread(background thread)
		
		LogController.log("ExampleAsyncTask doInBackground >>> " + params[0]);
		
		try
		{
			return CustomServiceFactory.getPromotionService().getPromotionQuestionWithPromotionId(params[0]);
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
		
		if(getPromotionQuestionAsyncTaskCallback != null)
		{
			getPromotionQuestionAsyncTaskCallback.onPostPromotionQuestionExecuteCallback(results);
		}
	}
}
