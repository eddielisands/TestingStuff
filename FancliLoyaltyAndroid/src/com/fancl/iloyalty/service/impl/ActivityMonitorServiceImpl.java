package com.fancl.iloyalty.service.impl;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

import com.fancl.iloyalty.AndroidProjectApplication;
import com.fancl.iloyalty.asynctask.SubmitUserLogAsyncTask;
import com.fancl.iloyalty.service.ActivityMonitorService;
import com.fancl.iloyalty.service.callback.ActivityMonitorServiceCallback;
import com.fancl.iloyalty.util.LogController;

public class ActivityMonitorServiceImpl implements ActivityMonitorService{
	
	private Activity activeActivity;
	private boolean inBackground = true;
	private boolean lastCallbackListenerStatus = true;
	
	private List<ActivityMonitorServiceCallback> callbackList;
	
	public ActivityMonitorServiceImpl()
	{
		callbackList = new ArrayList<ActivityMonitorServiceCallback>();
	}
	
	@Override
	public Activity getActiveActivity()
	{
		return this.activeActivity;
	}
	
	@Override
	public void activityOnResumed(Activity activity)
	{
//		LogController.log("activityOnResumed >>>> activity " + activity);
//		LogController.log("activityOnResumed >>>> activeActivity " + activeActivity);
//		LogController.log("activityOnResumed >>>> inBackground " + inBackground);
		
		if(activeActivity != null)
		{
			if(inBackground)
			{
//				LogController.log("activityOnResumed >>>> activeActivity.equals(activity) " + activeActivity.equals(activity));
				
				if(activeActivity.equals(activity))
				{
					synchronized (this)
					{
						activeActivity = activity;
					}
				}
				else
				{
//					LogController.log("activityOnResumed >>>> activeActivity.isFinishing() " + activeActivity.isFinishing());
					
					if(activeActivity.isFinishing())
					{
						synchronized (this)
						{
							activeActivity = activity;
						}
					}
				}
			}
			
			synchronized (this)
			{
				activeActivity = activity;
			}
		}
		else
		{
			synchronized (this)
			{
				activeActivity = activity;
			}
		}
		
		setInBackground(false);
	}
	
	@Override
	public void activityOnStopped(Activity activity)
	{
//		LogController.log("ActivityMonitorService " + "activityOnStopped " + activeActivity);
		if(activeActivity != null)
		{
//			LogController.log("ActivityMonitorService " + "activityOnStopped " + activity.equals(activeActivity));
			if(activity.equals(activeActivity))
			{
				setInBackground(true);
			}
		}
	}
	
	private void setInBackground(boolean inBackground)
	{
		this.inBackground = inBackground;
		LogController.log("ActivityMonitorService " + "inBackground set to " + inBackground);
		
		if (inBackground && !AndroidProjectApplication.application.firstCallDbChecking) {
			SubmitUserLogAsyncTask submitUserLogAsyncTask = new SubmitUserLogAsyncTask();
			submitUserLogAsyncTask.execute();
		}
		
		callBackToListener(inBackground);
	}
	
	@Override
	public boolean isInBackground()
	{
		return this.inBackground;
	}
	
	@Override
	public void addCallbackListener(ActivityMonitorServiceCallback activityMonitorServiceCallback)
	{
		if(activityMonitorServiceCallback != null)
		{
			callbackList.add(activityMonitorServiceCallback);
		}
	}
	
	@Override
	public void removeCallbackListener(ActivityMonitorServiceCallback activityMonitorServiceCallback)
	{
		if(activityMonitorServiceCallback != null)
		{
			int i;
			ActivityMonitorServiceCallback callbackInList;
			
			for(i = 0 ; i < callbackList.size() ; i++)
			{
				callbackInList = callbackList.get(i);
				
				if(callbackInList != null)
				{
					if(callbackInList.equals(activityMonitorServiceCallback))
					{
						callbackList.remove(i);
						i--;
					}
				}
			}
		}
	}
	
	private void callBackToListener(boolean isInBackground)
	{
		for(ActivityMonitorServiceCallback activityMonitorServiceCallback : callbackList)
		{
			if(activityMonitorServiceCallback != null)
			{
				if(lastCallbackListenerStatus != isInBackground)
				{
					lastCallbackListenerStatus = isInBackground;
					if(isInBackground)
					{
						activityMonitorServiceCallback.applicationGoingToBackground();
					}
					else
					{
						activityMonitorServiceCallback.applicationGoingToForeground();
					}
				}
			}
		}
	}
}