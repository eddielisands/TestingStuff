package com.fancl.iloyalty.asynctask;

import android.os.AsyncTask;

import com.fancl.iloyalty.exception.GeneralException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.responseimpl.FanclGeneralResult;
import com.fancl.iloyalty.util.LogController;

public class AddAndUpdateUserAsyncTask extends AsyncTask<Void, Void, FanclGeneralResult>{

	public AddAndUpdateUserAsyncTask()
	{
		
	}
	
	@Override
	protected void onPreExecute () {
		super.onPreExecute();
		//process of thread before start(UI Thread)
	}
	
	@Override
	protected FanclGeneralResult doInBackground(Void... params) {
		//process of thread(background thread)
		try
		{
			return CustomServiceFactory.getAccountService().addAndUpdateUser();
		}
		catch (GeneralException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute (FanclGeneralResult results) {
		super.onPostExecute(results);
		//process of thread ended(UI Thread)
		if(results != null)
		{
			LogController.log("addNUpdateUser success " + results.getStatus());	
		}
		else
		{
			LogController.log("addNUpdateUser failed null");	
		}
	}
}
