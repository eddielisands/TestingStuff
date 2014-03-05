package com.gt.gtsnssharinglibrarydemo;

import com.gt.gtsnssharinglibrarydemo.R;
import com.gt.snssharinglibrary.Config;
import com.gt.snssharinglibrary.pojo.SNSShareDetail;
import com.gt.snssharinglibrary.service.SNSService;
import com.gt.snssharinglibrary.service.SNSServiceCallback;
import com.gt.snssharinglibrary.service.SNSServiceImpl.SNS_LOGIN_TYPE;
import com.gt.snssharinglibrary.service.facebook_2_0.FacebookSessionStore;
import com.gt.snssharinglibrary.service.impl.FacebookService20Impl;
import com.gt.snssharinglibrary.util.LogController;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class Facebook20TestingActivity extends Activity implements SNSServiceCallback{
	
	private Handler handler = new Handler();
	
	private SNSService facebookServiceImpl;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(facebookServiceImpl != null)
		{
			facebookServiceImpl.onActivityResult(this, requestCode, resultCode, data);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setTitle("Facebook20TestingActivity");
		
		Config.FACEBOOK_2_0_APP_ID = "118100164920425";
		Config.FACEBOOK_2_0_PERMISSIONS = new String[] { "publish_stream", "photo_upload", "email" };
		Config.FACEBOOK_2_0_DIALOG_CLOSE_BTN_RESOURCES_ID = R.drawable.ic_launcher;
		
		facebookServiceImpl = new FacebookService20Impl(getResources());
		facebookServiceImpl.setSNSServiceCallback(Facebook20TestingActivity.this);
		
		Button loginBtn = (Button)findViewById(R.id.login_btn);
		loginBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				facebookServiceImpl.login(Facebook20TestingActivity.this, handler, SNS_LOGIN_TYPE.LOGIN_TYPE_NORMAL_LOGIN);
			}
		});
		
		Button logoutBtn = (Button)findViewById(R.id.logout_btn);
		logoutBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				facebookServiceImpl.logout(Facebook20TestingActivity.this, handler);
			}
		});
		
		Button postFeedBtn = (Button)findViewById(R.id.post_feed_btn);
		postFeedBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				String title = "ABC title";
				String caption = "XYZ caption";
				String description = "123 description";
				String link = "http://www.google.com.hk";
				String pictureLink = null;
				String friendId = null;
				Bitmap bitmap = null;
				String picSavePath = null;
				
				SNSShareDetail snsShareDetail = new SNSShareDetail(title, caption, description, link, pictureLink, friendId, bitmap, picSavePath);
				facebookServiceImpl.post(Facebook20TestingActivity.this, snsShareDetail);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void logginStatus(int snsCode, boolean isSuccessLogin,
			Object errorObject) {
		LogController.log("logginStatus >> "+ snsCode + " " + isSuccessLogin);
	}

	@Override
	public void getProfileStatus(int snsCode, boolean isSuccessGetProfile,
			Object errorObject) {
		LogController.log("getProfileStatus >> "+ snsCode + " " + isSuccessGetProfile);
		
		LogController.log("getProfileStatus >> "+ FacebookSessionStore.restoreUsername(this));
		LogController.log("getProfileStatus >> "+ FacebookSessionStore.restoreFacebookId(this));
		LogController.log("getProfileStatus >> "+ FacebookSessionStore.restoreFacebookEmail(this));
	}

	@Override
	public void loggoutStatus(int snsCode, boolean isSuccessLogout,
			Object errorObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postStatus(int snsCode, boolean isSuccessPost,
			Object errorObject) {
		LogController.log("postStatus >>  "+ snsCode + " " + isSuccessPost);
	}

	@Override
	public void getFriendsStatus(boolean arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getFeedsStatus(boolean isSuccess, Object feedsResultObject,
			Object errorObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void likeFeedStatus(boolean isSuccess, Object feedsResultObject,
			Object errorObject) {
		// TODO Auto-generated method stub
		
	}

}
