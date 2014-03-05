package com.gt.snssharinglibrary.service.twitter;

import android.content.Context;
import android.os.Bundle;

import com.gt.snssharinglibrary.service.SNSService;
import com.gt.snssharinglibrary.util.LogController;
import com.sugree.twitter.DialogError;
import com.sugree.twitter.Twitter.DialogListener;
import com.sugree.twitter.TwitterError;

public class LoginDialogListener implements DialogListener {
	
	private Context context;
	private SNSService snsService;
	
	public LoginDialogListener(Context context, SNSService snsService)
	{
		this.snsService = snsService;
		this.context = context;
	}
	
    public void onComplete(Bundle values) {
    	if(snsService != null)
    	{
    		snsService.logginStatus(context, true, null);
    	}
    }

    public void onTwitterError(TwitterError error) {
    	LogController.log("Twitter LoginDialogListener onFacebookError");
    	if(snsService != null)
    	{
    		snsService.logginStatus(context, false, error);
    	}
    }
    
    public void onError(DialogError error) {
    	LogController.log("Twitter LoginDialogListener onError");
    	if(snsService != null)
    	{
    		snsService.logginStatus(context, false, error);
    	}
    }

    public void onCancel() {
    	LogController.log("Twitter LoginDialogListener onCancel");
    	if(snsService != null)
    	{
    		snsService.logginStatus(context, false, "Action Cancel");
    	}
    }
}
