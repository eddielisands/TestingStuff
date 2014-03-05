package com.fancl.iloyalty.activity.detail;

import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fancl.iloyalty.AndroidProjectApplication;
import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.R;
import com.fancl.iloyalty.asynctask.EarnCreditAsyncTask;
import com.fancl.iloyalty.asynctask.callback.EarnCreditAsyncTaskCallback;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.factory.CustomServiceFactory;
import com.fancl.iloyalty.factory.GeneralServiceFactory;
import com.fancl.iloyalty.pojo.Event;
import com.fancl.iloyalty.pojo.IchannelMagazine;
import com.fancl.iloyalty.responseimpl.FanclGeneralResult;
import com.fancl.iloyalty.service.ActivityMonitorService;
import com.fancl.iloyalty.service.LocaleService;
import com.fancl.iloyalty.service.callback.ActivityMonitorServiceCallback;
import com.fancl.iloyalty.util.DataUtil;
import com.fancl.iloyalty.util.LogController;
import com.google.android.gms.internal.ap;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubeVideoActivity extends YouTubeFailureRecoveryActivity implements PlaybackEventListener, ActivityMonitorServiceCallback, EarnCreditAsyncTaskCallback {
	protected AndroidProjectApplication application;
	protected Handler handler;
	protected ActivityMonitorService activityMonitorService;
	
	private boolean withYoutubeLinkOnly;
	private String pageTitle;
	private IchannelMagazine magazine;
	private String videoId;
	
	private RelativeLayout backButton;
	private TextView titleTextView;
	private YouTubePlayerView playerView;

	private LocaleService localeService;
	
	private boolean isPlaying = false;
	private int playedTime = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        application = (AndroidProjectApplication)this.getApplication();
        application.firstCallDbChecking = true;
        handler = new Handler();
        
        activityMonitorService = GeneralServiceFactory.getActivityMonitorService();
        activityMonitorService.addCallbackListener(this);
        
        application.addActiveActivity(this);

		localeService = GeneralServiceFactory.getLocaleService();
	}
	
	@Override
    protected void onResume () 
    {
    	if(activityMonitorService != null)
    	{
    		activityMonitorService.activityOnResumed(this);
    	}
    	
    	Intent intent = getIntent();
		withYoutubeLinkOnly = intent.getBooleanExtra(Constants.YOUTUBE_LINK_ONLY_KEY, true);
		pageTitle = intent.getStringExtra(Constants.PAGE_TITLE_KEY);
		
		String videoPath;
		if (withYoutubeLinkOnly) {
			videoPath = intent.getStringExtra(Constants.YOUTUBE_LINK_KEY);
		}
		else {
			magazine = (IchannelMagazine) intent.getExtras().getSerializable(Constants.BEAUTY_ICNANNEL_MAGAZINE_ITEM_KEY);
			videoPath = localeService.textByLangaugeChooser(this, magazine.getVideoLinkEn(), magazine.getVideoLinkZh(), magazine.getVideoLinkSc());
		}
		videoId = DataUtil.getVideoIdFromLink(videoPath);

		setContentView(R.layout.activity_youtube_video_layout);	
		
		backButton = (RelativeLayout) findViewById(R.id.header_bar_left_btn_title);
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});

		titleTextView = (TextView) findViewById(R.id.header_navigation_title);
		titleTextView.setText(pageTitle);
		
		playerView = (YouTubePlayerView) findViewById(R.id.youtube_video_view);
		playerView.initialize(Constants.YOUTUBE_API_KEY, this);
    	
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
	public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
			boolean wasRestored) {
//		player.setFullscreen(true);

//		int controlFlags = player.getFullscreenControlFlags();
//		controlFlags &= ~YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
//		player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
//		player.setFullscreenControlFlags(controlFlags);
		
		player.setPlaybackEventListener(this);

		if (!wasRestored) {
			player.cueVideo(videoId);
		}
	}

	@Override
	protected YouTubePlayer.Provider getYouTubePlayerProvider() {
		return (YouTubePlayerView) findViewById(R.id.youtube_video_view);
	}

	@Override
	public void onBuffering(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPaused() {
		// TODO Auto-generated method stub
		LogController.log("onPaused");
		isPlaying = false;
	}

	@Override
	public void onPlaying() {
		// TODO Auto-generated method stub
		LogController.log("onPlaying");
		isPlaying = true;
		updateTimer();
	}

	@Override
	public void onSeekTo(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopped() {
		// TODO Auto-generated method stub
		LogController.log("onStopped");
		isPlaying = false;
	}
	
	@Override
	public void onBackPressed() {
		// call earn credit api
		if (!withYoutubeLinkOnly) {
			String eventId = null;
			try {
				List<Event> events = CustomServiceFactory.getPromotionService().getEventItemListWithItemId(magazine.getObjectId());
				for (int i = 0; i < events.size(); i++) {
					Event event = events.get(i);
					if (event.getEventType().equals("video")) {
						eventId = event.getEventId();
						break;
					}
				}
			} catch (FanclException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			if (playedTime > 0 && eventId != null) {
				int checkTime = playedTime - Integer.valueOf(magazine.getVideoDuration()) + 5;
				if (checkTime > 0) {
					EarnCreditAsyncTask earnCreditAsyncTask = new EarnCreditAsyncTask(this);
					earnCreditAsyncTask.execute(eventId);
					return;
				}
			}
		}
		application.firstCallDbChecking = false;
		super.onBackPressed();
	}
	
	private void updateTimer() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (isPlaying) {
					playedTime += 1;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	@Override
	public void applicationGoingToBackground() {
		LogController.log("AndroidProjectFrameworkActivity applicationGoingToBackground");
	}

	@Override
	public void applicationGoingToForeground() {
		LogController.log("AndroidProjectFrameworkActivity applicationGoingToForeground");
	}

	@Override
	public void onPostExecuteCallback(Object results) {
		// TODO Auto-generated method stub
		if (results instanceof String) {
			GeneralServiceFactory.getAlertDialogService().makeNativeDialog(this, "", 
					("eraned credit: " + ((String)results)),
					getString(R.string.ok_btn_title),
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			},
			"", null, false, false);
		}
		else {
			GeneralServiceFactory.getAlertDialogService().makeNativeDialog(this, "", 
					GeneralServiceFactory.getLocaleService().textByLangaugeChooser(this, ((FanclGeneralResult) results).getErrMsgEn(), ((FanclGeneralResult) results).getErrMsgZh(), ((FanclGeneralResult) results).getErrMsgSc()),
					getString(R.string.ok_btn_title),
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			},
			"", null, false, false);
		}
		super.onBackPressed();
	}
}
