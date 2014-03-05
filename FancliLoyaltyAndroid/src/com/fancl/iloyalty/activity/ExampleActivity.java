package com.fancl.iloyalty.activity;

import java.util.List;

import android.os.Bundle;

import com.fancl.iloyalty.asynctask.ExampleAsyncTask;
import com.fancl.iloyalty.asynctask.callback.ExampleAsyncTaskCallback;
import com.fancl.iloyalty.util.DeviceUtil;
import com.fancl.iloyalty.util.LogController;

public class ExampleActivity extends AndroidProjectFrameworkActivity implements ExampleAsyncTaskCallback{

	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LogController.log("UUID >>> " + DeviceUtil.getDeviceUUID(this));
        
        testingCallExample();
    }
	
	private void testingCallExample()
	{
		ExampleAsyncTask exampleAsyncTask = new ExampleAsyncTask(this);
		exampleAsyncTask.execute("Testing 1...");
	}

	@Override
	public void onPostExecuteCallback(List<String> results) {
		LogController.log("ExampleAsyncTask >>> onPostExecuteCallback " + results);
	}
}
