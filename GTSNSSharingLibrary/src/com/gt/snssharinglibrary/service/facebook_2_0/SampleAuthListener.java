package com.gt.snssharinglibrary.service.facebook_2_0;

import android.content.Context;

import com.gt.snssharinglibrary.service.SNSService;
import com.gt.snssharinglibrary.service.facebook_2_0.SessionEvents.AuthListener;

public class SampleAuthListener implements AuthListener {

	private Context context;
	private SNSService snsService;
	
	public SampleAuthListener(Context context, SNSService snsService)
	{
		this.context = context;
		this.snsService = snsService;
	}
	
	public void removeSNSservice()
	{
		this.snsService = null;
	}
	
    public void onAuthSucceed() {
    	if(snsService != null)
    	{
    		snsService.logginStatus(context, true, null);
    	}
    }

    public void onAuthFail(String error) {
    	if(snsService != null)
    	{
    		if(error != null)
    		{
    			snsService.logginStatus(context, false, error);
    		}
    	}
    }
}
