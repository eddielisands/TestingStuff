package com.fancl.iloyalty.asynctask;

import android.os.AsyncTask;

import com.fancl.iloyalty.asynctask.callback.RedeemCouponAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.responseimpl.FanclGeneralResult;
import com.fancl.iloyalty.util.LogController;

public class RedeemCouponAsyncTask extends AsyncTask<String, Void, FanclGeneralResult>{

private RedeemCouponAsyncTaskCallback redeemCouponAsyncTaskCallback;
	
	public RedeemCouponAsyncTask(RedeemCouponAsyncTaskCallback redeemCouponAsyncTaskCallback)
	{
		this.redeemCouponAsyncTaskCallback = redeemCouponAsyncTaskCallback;
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
			return CustomServiceFactory.getPromotionService().redeemICouponWithCode(params[0], params[1]);
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
		
		if(redeemCouponAsyncTaskCallback != null)
		{
			redeemCouponAsyncTaskCallback.onPostExecuteCallback(results);
		}
	}
}
