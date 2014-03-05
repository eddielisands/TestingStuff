package com.fancl.iloyalty.service;

import android.app.Activity;

import com.fancl.iloyalty.service.callback.ActivityMonitorServiceCallback;

public interface ActivityMonitorService {
	public Activity getActiveActivity();
	
	public void activityOnResumed(Activity activity);
	
	public void activityOnStopped(Activity activity);
	
	public boolean isInBackground();
	
	public void addCallbackListener(ActivityMonitorServiceCallback activityMonitorServiceCallback);
	
	public void removeCallbackListener(ActivityMonitorServiceCallback activityMonitorServiceCallback);
}
