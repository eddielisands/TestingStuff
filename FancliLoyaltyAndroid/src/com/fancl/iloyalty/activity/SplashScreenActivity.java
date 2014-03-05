package com.fancl.iloyalty.activity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

import com.fancl.iloyalty.R;
import com.fancl.iloyalty.activity.login.WelcomeActivity;
import com.fancl.iloyalty.activity.whatshot.WhatsHotHomeActivity;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.service.impl.LocaleServiceImpl.LANGUAGE_TYPE;
import com.fancl.iloyalty.util.DeviceUtil;
import com.fancl.iloyalty.util.LogController;

public class SplashScreenActivity extends AndroidProjectFrameworkActivity implements SurfaceHolder.Callback, OnCompletionListener, OnErrorListener, OnInfoListener, OnPreparedListener, OnSeekCompleteListener, OnVideoSizeChangedListener {
	
	Display currentDisplay;
	
	RelativeLayout spaceLayout;
	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;

	MediaPlayer mediaPlayer;

	int videoWidth = 0,videoHeight = 0;

	boolean stopLoadHome = false;
	
	public final static String LOGTAG = "CUSTOM_VIDEO_PLAYER";
	
	Handler handler = new Handler();
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		application.applicationStartup();
		
		setContentView(R.layout.activity_splash_screen);
		
		spaceLayout = (RelativeLayout) this.findViewById(R.id.splash_space_layout);	
		
		surfaceView = (SurfaceView) this.findViewById(R.id.SurfaceView);
	    surfaceHolder = surfaceView.getHolder();
	    surfaceHolder.addCallback(this);
	    surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

	    mediaPlayer = new MediaPlayer();
	    mediaPlayer.setOnCompletionListener(this);
	    mediaPlayer.setOnErrorListener(this);
	    mediaPlayer.setOnInfoListener(this);
	    mediaPlayer.setOnPreparedListener(this);
	    mediaPlayer.setOnSeekCompleteListener(this);
	    mediaPlayer.setOnVideoSizeChangedListener(this);
	    
	    try {
	    	AssetFileDescriptor afd = null;
	    	LogController.log("screen size: " + DeviceUtil.getDeviceDenstity(this));
			if (DeviceUtil.getDeviceDenstity(this) > 1.5) {
				if (GeneralServiceFactory.getLocaleService().getCurrentLanguageType() == LANGUAGE_TYPE.EN) {
					afd = getAssets().openFd("splash_en@2x.mp4");
				}
				else {
					afd = getAssets().openFd("splash_tc@2x.mp4");
				}
			}
			else {
				if (GeneralServiceFactory.getLocaleService().getCurrentLanguageType() == LANGUAGE_TYPE.EN) {
					afd = getAssets().openFd("splash_en.mp4");
				}
				else {
					afd = getAssets().openFd("splash_tc.mp4");
				}
			}

			mediaPlayer.setDataSource(afd.getFileDescriptor(),
			        afd.getStartOffset(), afd.getLength());
		    afd.close();
		} catch (Exception e1) {
			LogController.log("Error Log: " + e1);
			toHomePage();
		}
	    currentDisplay = getWindowManager().getDefaultDisplay();
	}
	
	@Override
	protected void onDestroy()
	{
		mediaPlayer.release();
		super.onDestroy();
	}
	
	private void toHomePage()
	{
		if (stopLoadHome) {
			return;
		}
		stopLoadHome = true;
		
		Intent intent;
		if (CustomServiceFactory.getAccountService().isLogin()) {
			intent = new Intent(SplashScreenActivity.this, WhatsHotHomeActivity.class);
		}
		else {
			intent = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
//			intent = new Intent(SplashScreenActivity.this, WhatsHotHomeActivity.class);                                                                                                                                                                                                                                                                                                                 
		}
		startActivity(intent);		
		
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				application.firstCallDbChecking = false;
				application.databaseVersionChecking();
			}
		}, 50);
		
		finish();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mediaPlayer.setDisplay(holder);

	    try {
	      mediaPlayer.prepare();
	    } catch (Exception e) {
			LogController.log("Error Log: " + e);
			toHomePage();
	    }
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	
	public void onCompletion(MediaPlayer mp) {
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				toHomePage();
			}
		}, 500);
		
	  }

	  public boolean onError(MediaPlayer mp, int whatError, int extra) {
		  LogController.log("MediaPlayer Error: " + whatError + " , " + extra);
	    return false;
	  }

	  public boolean onInfo(MediaPlayer mp, int whatInfo, int extra) {
	    return false;
	  }

	  public void onPrepared(MediaPlayer mp) {
		  videoWidth = mp.getVideoWidth();
		  videoHeight = mp.getVideoHeight();
		  DisplayMetrics metrics = new DisplayMetrics();
		  getWindowManager().getDefaultDisplay().getMetrics(metrics);
		  LogController.log("Size: " + metrics.widthPixels + " , " + metrics.heightPixels);
		  surfaceView.setLayoutParams(new RelativeLayout.LayoutParams(metrics.widthPixels,
				  metrics.heightPixels));
		  mp.start();
	  }

	  public void onSeekComplete(MediaPlayer mp) {
	  }

	  public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
	  }
}
