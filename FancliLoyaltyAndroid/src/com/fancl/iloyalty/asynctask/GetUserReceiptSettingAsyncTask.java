package com.fancl.iloyalty.asynctask;

import android.os.AsyncTask;

import com.fancl.iloyalty.asynctask.callback.GetUserReceiptSettingAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.util.LogController;

public class GetUserReceiptSettingAsyncTask extends AsyncTask<Void, Void, Object>{

private GetUserReceiptSettingAsyncTaskCallback getUserReceiptSettingAsyncTaskCallback;
	
	public GetUserReceiptSettingAsyncTask(GetUserReceiptSettingAsyncTaskCallback getUserReceiptSettingAsyncTaskCallback)
	{
		this.getUserReceiptSettingAsyncTaskCallback = getUserReceiptSettingAsyncTaskCallback;
	}
	
	@Override
	protected void onPreExecute () {
		super.onPreExecute();
		//process of thread before start(UI Thread)
	}
	
	@Override
	protected Object doInBackground(Void... params) {
		//process of thread(background thread)
		
		LogController.log("ExampleAsyncTask doInBackground >>> ");
		
		try
		{
			return CustomServiceFactory.getSettingService().getUserReceiptSetting();
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
		
		if(getUserReceiptSettingAsyncTaskCallback != null)
		{
			getUserReceiptSettingAsyncTaskCallback.onPostExecuteCallback(results);
		}
	}
}
