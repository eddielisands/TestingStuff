package com.fancl.iloyalty.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.fancl.iloyalty.AndroidProjectApplication;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.item.LoadingDialog;
import com.fancl.iloyalty.service.ActivityMonitorService;
import com.fancl.iloyalty.service.LocaleService;
import com.fancl.iloyalty.service.callback.ActivityMonitorServiceCallback;
import com.fancl.iloyalty.util.LogController;

public class AndroidProjectFrameworkFragmentActivity  extends FragmentActivity implements ActivityMonitorServiceCallback {
	protected AndroidProjectApplication application;
	protected Handler handler;
	protected ActivityMonitorService activityMonitorService;
	
	public LoadingDialog loadingDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		LocaleService localeService = GeneralServiceFactory.getLocaleService();
		localeService.resetLanguage(AndroidProjectFrameworkFragmentActivity.this);
		loadingDialog = new LoadingDialog(AndroidProjectFrameworkFragmentActivity.this);
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

	private void existApp() {
		application.firstCallDbChecking = true;
		super.onBackPressed();
	}
	
	public boolean checkLoadingDialog() {
		if (loadingDialog == null) {
			loadingDialog = new LoadingDialog(AndroidProjectFrameworkFragmentActivity.this);
		}
		return true;
	}
}
