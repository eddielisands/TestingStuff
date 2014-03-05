package com.fancl.iloyalty.asynctask;

import java.util.List;

import android.os.AsyncTask;

import com.fancl.iloyalty.asynctask.callback.ExampleAsyncTaskCallback;
import com.fancl.iloyalty.util.LogController;

public class ExampleAsyncTask extends AsyncTask<String, Void, List<String>>{

private ExampleAsyncTaskCallback exampleAsyncTaskCallback;
	
	public ExampleAsyncTask(ExampleAsyncTaskCallback exampleAsyncTaskCallback)
	{
		this.exampleAsyncTaskCallback = exampleAsyncTaskCallback;
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
			Thread.sleep(10000);
		}
		catch (InterruptedException e)
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
		
		if(exampleAsyncTaskCallback != null)
		{
			exampleAsyncTaskCallback.onPostExecuteCallback(results);
		}
	}
}
