package com.fancl.iloyalty.asynctask;

import android.os.AsyncTask;

import com.fancl.iloyalty.asynctask.callback.CardReplacementAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.responseimpl.TOSResult;
import com.fancl.iloyalty.service.AccountService;
import com.fancl.iloyalty.util.LogController;

public class CardReplacementAsyncTask extends AsyncTask<String, Void, TOSResult>{
	private AccountService accountService;
	private CardReplacementAsyncTaskCallback cardReplacementAsyncTaskCallback;

	public CardReplacementAsyncTask(CardReplacementAsyncTaskCallback cardReplacementAsyncTaskCallback)
	{
		this.accountService = CustomServiceFactory.getAccountService();
		this.cardReplacementAsyncTaskCallback = cardReplacementAsyncTaskCallback;
	}

	@Override
	protected void onPreExecute () {
		super.onPreExecute();
		//process of thread before start(UI Thread)
	}

	@Override
	protected TOSResult doInBackground(String... params) {
		//process of thread(background thread)

		LogController.log("CardReplacementAsyncTask doInBackground >>> " + params[0]);

		try
		{
			return accountService.newUserCardReplaceWithOldMemberId(params[0]);
		}
		catch (FanclException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute (TOSResult results) {
		super.onPostExecute(results);
		//process of thread ended(UI Thread)

		if(cardReplacementAsyncTaskCallback != null)
		{
			cardReplacementAsyncTaskCallback.onPostExecuteCallback(results);
		}
	}
}
