package com.fancl.iloyalty.asynctask;

import android.os.AsyncTask;

import com.fancl.iloyalty.asynctask.callback.SetUserReceiptSettingAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.responseimpl.FanclGeneralResult;
import com.fancl.iloyalty.util.LogController;

public class SetUserReceiptSettingAsyncTask extends AsyncTask<String, Void, FanclGeneralResult>{

private SetUserReceiptSettingAsyncTaskCallback setUserReceiptSettingAsyncTaskCallback;
	
	public SetUserReceiptSettingAsyncTask(SetUserReceiptSettingAsyncTaskCallback setUserReceiptSettingAsyncTaskCallback)
	{
		this.setUserReceiptSettingAsyncTaskCallback = setUserReceiptSettingAsyncTaskCallback;
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
			return CustomServiceFactory.getSettingService().setUserReceiptSetting(params[0], params[1]);
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
		
		if(setUserReceiptSettingAsyncTaskCallback != null)
		{
			setUserReceiptSettingAsyncTaskCallback.onPostExecuteCallback(results);
		}
	}
}
