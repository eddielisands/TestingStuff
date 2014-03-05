package com.fancl.iloyalty.asynctask;

import java.util.List;

import android.os.AsyncTask;

import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.util.LogController;

public class CountAdHitAsyncTask extends AsyncTask<String, Void, List<String>>{

	public CountAdHitAsyncTask() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onPreExecute () {
		super.onPreExecute();
		//process of thread before start(UI Thread)
	}
	
	@Override
	protected List<String> doInBackground(String... params) {
		//process of thread(background thread)
		
		LogController.log("ExampleAsyncTask doInBackground >>> " + params[0]);
		
		try
		{
			CustomServiceFactory.getSettingService().countAdHitRateWithBannerId(params[0]);
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
