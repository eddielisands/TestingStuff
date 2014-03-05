package com.fancl.iloyalty.asynctask;

import android.os.AsyncTask;

import com.fancl.iloyalty.asynctask.callback.PromotionCountAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;

public class PromotionCountAsyncTask extends AsyncTask<String, Void, String>{

private PromotionCountAsyncTaskCallback promotionCountAsyncTaskCallback;
	
	public PromotionCountAsyncTask(PromotionCountAsyncTaskCallback promotionCountAsyncTaskCallback)
	{
		this.promotionCountAsyncTaskCallback = promotionCountAsyncTaskCallback;
	}
	
	@Override
	protected void onPreExecute () {
		super.onPreExecute();
		//process of thread before start(UI Thread)
	}
	
	@Override
	protected String doInBackground(String... params) {
		//process of thread(background thread)
		
		try
		{
			return CustomServiceFactory.getAboutFanclService().getPromotionCount();
		}
		catch (FanclException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected void onPostExecute (String results) {
		super.onPostExecute(results);
		//process of thread ended(UI Thread)
		
		if(promotionCountAsyncTaskCallback != null)
		{
			promotionCountAsyncTaskCallback.onPostExecuteCallback(results);
		}
	}
}
