package com.fancl.iloyalty.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.fancl.iloyalty.AndroidProjectApplication;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.advertisement.AdvertisementActivity;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.item.LoadingDialog;
import com.fancl.iloyalty.pojo.AdBanner;
import com.fancl.iloyalty.service.ActivityMonitorService;
import com.fancl.iloyalty.service.LocaleService;
import com.fancl.iloyalty.service.callback.ActivityMonitorServiceCallback;
import com.fancl.iloyalty.util.LogController;

public class AndroidProjectFrameworkActivity extends Activity implements ActivityMonitorServiceCallback {
	
	protected AndroidProjectApplication application;
	protected Handler handler;
	protected ActivityMonitorService activityMonitorService;
	
	public LoadingDialog loadingDialog;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	LocaleService localeService = GeneralServiceFactory.getLocaleService();
		localeService.resetLanguage(AndroidProjectFrameworkActivity.this);
		loadingDialog = new LoadingDialog(AndroidProjectFrameworkActivity.this);
        super.onCreate(savedInstanceState);
                
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        application = (AndroidProjectApplication)this.getApplication();
        handler = new Handler();
        
        activityMonitorService = GeneralServiceFactory.getActivityMonitorService();
        activityMonitorService.addCallbackListener(this);
        
        application.addActiveActivity(this);
    }
    
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {
    	super.onActivityResult(requestCode, resultCode, data);
    }
    
    @Override
    protected void onResume () 
    {
    	if(activityMonitorService != null)
    	{
    		activityMonitorService.activityOnResumed(this);
    	}
    	
    	super.onResume();
    }
    
    @Override
    protected void onPause () 
    {
    	super.onPause();
    }
    
    @Override
    protected void onDestroy () 
    {
    	activityMonitorService.removeCallbackListener(this);
		application.removeActiveActivity(this);
    	super.onDestroy();
    }
    
    @Override
    protected void onStart () 
    {
    	super.onStart();
    }
    
    @Override
    protected void onStop () 
    {
    	if(activityMonitorService != null)
    	{
    		activityMonitorService.activityOnStopped(this);
    	}
    	
    	super.onStop();
    }
    
    @Override
    public void onBackPressed () 
    {
    	if (application.getActiveListCount() == 1) {
    		GeneralServiceFactory.getAlertDialogService().makeNativeDialog(this, "", 
    				"Do you wanna exist?",
    				getString(R.string.ok_btn_title),
    				new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog,int id) {
    						existApp();
    					}
    				},
    				getString(R.string.cancel_btn_title), 
    				new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog,int id) {
    						dialog.cancel();
    					}
    				}, false, false);
    		return;
		}
    	super.onBackPressed();
    }
    
    @Override
    public void onWindowFocusChanged (boolean hasFocus) 
    {
    	super.onWindowFocusChanged(hasFocus);
    }
    
    @Override
	public void applicationGoingToBackground() {
		LogController.log("AndroidProjectFrameworkActivity applicationGoingToBackground");
	}

	@Override
	public void applicationGoingToForeground() {
		LogController.log("AndroidProjectFrameworkActivity applicationGoingToForeground");
		GeneralServiceFactory.getThreadService().startImageExecutor();
		application.resetLoadingDialog();
		application.databaseVersionChecking();
	}
	
	public void callToOpenAd() {
		List<AdBanner> bannerList = new ArrayList<AdBanner>(); 
		try {
			bannerList = CustomServiceFactory.getAboutFanclService().getFrontAdObjects();
		} catch (FanclException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LogController.log("bannerList size:"+ bannerList.size());
		if(bannerList.size()>0){
			if(CustomServiceFactory.getAccountService().isLogin()){
				Intent intent = new Intent(AndroidProjectFrameworkActivity.this, AdvertisementActivity.class);
				startActivity(intent);
				//		overridePendingTransition( R.drawable.slide_in_up, R.drawable.slide_out_up );
			}
		}
	}
	
	private void existApp() {
		application.firstCallDbChecking = true;
		super.onBackPressed();
	}
	
	public boolean checkLoadingDialog() {
		if (loadingDialog == null) {
			loadingDialog = new LoadingDialog(AndroidProjectFrameworkActivity.this);
			

		}
		return true;
	}
	

}