package com.fancl.iloyalty.asynctask;

import java.util.List;

import android.os.AsyncTask;

import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;

public class SubmitUserLogAsyncTask extends AsyncTask<String, Void, List<String>>{

	public SubmitUserLogAsyncTask()
	{
		
	}
	
	@Override
	protected void onPreExecute () {
		super.onPreExecute();
		//process of thread before start(UI Thread)
	}
	
	@Override
	protected List<String> doInBackground(String... params) {
		//process of thread(background thread)
		
		
		try
		{
			CustomServiceFactory.getSettingService().submitUserLog();
		}
		catch (FanclException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected void onPostExecute (List<String> results) {
		super.onPostExecute(results);
		//process of thread ended(UI Thread)
		
	}
}
