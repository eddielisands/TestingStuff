package com.fancl.iloyalty.asynctask;

import android.os.AsyncTask;

import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.util.LogController;

public class ILoyaltyTPCSocketTillIdAsyncTask extends AsyncTask<String, Void, String>{


	public ILoyaltyTPCSocketTillIdAsyncTask()
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
		LogController.log("ILoyaltyTPCSocketTillIdAsyncTask doInBackground");
		CustomServiceFactory.getILoyaltyTCPSocketService().socketTillIdRequest(params[0]);

		return null;
	}

	@Override
	protected void onPostExecute (String results) {
		super.onPostExecute(results);
		//process of thread ended(UI Thread)
	}
}
