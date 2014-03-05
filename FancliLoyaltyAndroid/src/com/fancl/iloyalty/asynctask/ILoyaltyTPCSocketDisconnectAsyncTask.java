package com.fancl.iloyalty.asynctask;

import android.os.AsyncTask;

import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.util.LogController;

public class ILoyaltyTPCSocketDisconnectAsyncTask extends AsyncTask<String, Void, String>{


	public ILoyaltyTPCSocketDisconnectAsyncTask()
	{

	}

	@Override
	protected void onPreExecute () {
		super.onPreExecute();
		//process of thread before start(UI Thread)
	}

	@Override
	protected String doInBackground(String... params) {
		//process of thread(background thread)
		LogController.log("ILoyaltyTPCSocketDisconnectAsyncTask doInBackground");

		CustomServiceFactory.getILoyaltyTCPSocketService().socketDisconnectRequest();

		return null;
	}

	@Override
	protected void onPostExecute (String results) {
		super.onPostExecute(results);
		//process of thread ended(UI Thread)
	}
}
