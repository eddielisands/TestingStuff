package com.gt.snssharinglibrary.service.facebook_2_0;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import com.facebook_2_0.android.FacebookError;
import com.gt.snssharinglibrary.service.SNSService;

import android.content.Context;
import android.os.Handler;

public class LogoutRequestListener extends BaseRequestListener {
	
	private Context context;
	private SNSService snsService;
	private Handler mHandler;
	
	public LogoutRequestListener(Context context, SNSService snsService, Handler handler)
	{
		this.context = context;
		this.snsService = snsService;
		this.mHandler = handler;
	}
	
	@Override
    public void onComplete(String response, final Object state) {
        // callback should be run in the original thread, 
        // not the background thread
        mHandler.post(new Runnable() {
            public void run() {
                SessionEvents.onLogoutFinish();
                if(snsService != null)
                {
                	snsService.loggoutStatus(context, true, null);
                }
            }
        });
    }
	
	@Override
	public void onFacebookError(FacebookError e, final Object state) {	
		final String message = e.getMessage();
		
		mHandler.post(new Runnable() {
			public void run() {
				if(snsService != null)
                {
                	snsService.loggoutStatus(context, false, message);
                }
			}
		});
	}

	@Override
    public void onFileNotFoundException(FileNotFoundException e, final Object state) {
		final String message = e.getMessage();
		
		mHandler.post(new Runnable() {
			public void run() {
				if(snsService != null)
                {
                	snsService.loggoutStatus(context, false, message);
                }
			}
		});
    }

	@Override
    public void onIOException(IOException e, final Object state) {
		final String message = e.getMessage();
		
		mHandler.post(new Runnable() {
			public void run() {
				if(snsService != null)
                {
                	snsService.loggoutStatus(context, false, message);
                }
			}
		});
    }

	@Override
    public void onMalformedURLException(MalformedURLException e,
                                        final Object state) {
		final String message = e.getMessage();
		
		mHandler.post(new Runnable() {
			public void run() {
				if(snsService != null)
                {
                	snsService.loggoutStatus(context, false, message);
                }
			}
		});
    }
}
