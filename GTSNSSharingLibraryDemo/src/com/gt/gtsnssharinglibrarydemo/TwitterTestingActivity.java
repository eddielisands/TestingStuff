package com.gt.gtsnssharinglibrarydemo;

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.gt.snssharinglibrary.Config;
import com.gt.snssharinglibrary.pojo.SNSShareDetail;
import com.gt.snssharinglibrary.service.SNSService;
import com.gt.snssharinglibrary.service.SNSServiceCallback;
import com.gt.snssharinglibrary.service.SNSServiceImpl.SNS_LOGIN_TYPE;
import com.gt.snssharinglibrary.service.impl.TwitterServiceImpl;
import com.gt.snssharinglibrary.util.LogController;

public class TwitterTestingActivity extends Activity implements SNSServiceCallback{
	
	private Handler handler = new Handler();
	
	private SNSService twitterServiceImpl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setTitle("TwitterTestingActivity");
		
		Config.TWITTER_CONSUMER_KEY = "oWr7DmO7pSah6ap9sfaw";
		Config.TWITTER_CONSUMER_SECRET = "ktYAh1FPucx016zgX1Z4eztAR9Y8htnrztTY5bnyIyc";
		Config.TWITTER_DIALOG_CLOSE_BTN_RESOURCES_ID = R.drawable.ic_launcher;
		
		twitterServiceImpl = new TwitterServiceImpl(this);
		twitterServiceImpl.setSNSServiceCallback(TwitterTestingActivity.this);
		
		Button loginBtn = (Button)findViewById(R.id.login_btn);
		loginBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				twitterServiceImpl.login(TwitterTestingActivity.this, handler, SNS_LOGIN_TYPE.LOGIN_TYPE_NORMAL_LOGIN);
			}
		});
		
		Button logoutBtn = (Button)findViewById(R.id.logout_btn);
		logoutBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				twitterServiceImpl.logout(TwitterTestingActivity.this, handler);
			}
		});
		
		Button postFeedBtn = (Button)findViewById(R.id.post_feed_btn);
		postFeedBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				String message = "Twitter Plain Text Message Testing. " + (new Date()).toString();
				
				SNSShareDetail snsShareDetail = new SNSShareDetail(message);
				twitterServiceImpl.post(TwitterTestingActivity.this, snsShareDetail);
			}
		});
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
	}

	@Override
	public void loggoutStatus(int snsCode, boolean isSuccessLogout,
			Object errorObject) {
		LogController.log("loggoutStatus >> "+ snsCode + " " + isSuccessLogout);
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
