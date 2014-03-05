package com.fancl.iloyalty.asynctask;

import android.os.AsyncTask;

import com.fancl.iloyalty.asynctask.callback.GetPurchaseHistoryReceiptAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.util.LogController;

public class GetPurchaseHistoryReceiptAsyncTask extends AsyncTask<String, Void, Object>{

private GetPurchaseHistoryReceiptAsyncTaskCallback getPurchaseHistoryReceiptAsyncTaskCallback;
	
	public GetPurchaseHistoryReceiptAsyncTask(GetPurchaseHistoryReceiptAsyncTaskCallback getPurchaseHistoryReceiptAsyncTaskCallback)
	{
		this.getPurchaseHistoryReceiptAsyncTaskCallback = getPurchaseHistoryReceiptAsyncTaskCallback;
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
			return CustomServiceFactory.getAccountService().getPurchaseHistoryReceipt(params[0], params[1], params[2]);
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
		
		if(getPurchaseHistoryReceiptAsyncTaskCallback != null)
		{
			getPurchaseHistoryReceiptAsyncTaskCallback.onPostExecuteCallback(results);
		}
	}
}
