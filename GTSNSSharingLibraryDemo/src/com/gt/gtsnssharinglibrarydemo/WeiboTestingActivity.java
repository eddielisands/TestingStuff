package com.gt.gtsnssharinglibrarydemo;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.gt.snssharinglibrary.Config;
import com.gt.snssharinglibrary.pojo.SNSShareDetail;
import com.gt.snssharinglibrary.service.SNSService;
import com.gt.snssharinglibrary.service.SNSServiceCallback;
import com.gt.snssharinglibrary.service.SNSServiceImpl.SNS_LOGIN_TYPE;
import com.gt.snssharinglibrary.service.impl.WeiboServiceImpl;
import com.gt.snssharinglibrary.util.LogController;

public class WeiboTestingActivity extends Activity implements
		SNSServiceCallback {

	private Handler handler = new Handler();

	private SNSService weiboServerImpl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setTitle("WeiboTestingActivity");

		Config.WEIBO_CONSUMER_KEY = "3671389900";
		Config.WEIBO_CONSUMER_SECRET = "ea5a5f6a58fa6a3aa323446dfb5afaac";
		Config.WEIBO_REDIRECT_URL = "http://www.langhamplace.com.hk";

		weiboServerImpl = new WeiboServiceImpl(this);
		weiboServerImpl.setSNSServiceCallback(WeiboTestingActivity.this);

		Button loginBtn = (Button) findViewById(R.id.login_btn);
		loginBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				weiboServerImpl.login(WeiboTestingActivity.this, handler, SNS_LOGIN_TYPE.LOGIN_TYPE_NORMAL_LOGIN);
			}
		});

		Button logoutBtn = (Button) findViewById(R.id.logout_btn);
		logoutBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				weiboServerImpl.logout(WeiboTestingActivity.this, handler);
			}
		});

		Button postFeedBtn = (Button) findViewById(R.id.post_feed_btn);
		postFeedBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				String message = "Twitter Plain Text Message Testing. " + (new Date()).toString();

				SNSShareDetail snsShareDetail = new SNSShareDetail(message);
				weiboServerImpl.post(WeiboTestingActivity.this, snsShareDetail);
			}
		});

		Button getFeedsBtn = (Button) this.findViewById(R.id.get_feeds_btn);
		getFeedsBtn.setVisibility(View.VISIBLE);
		getFeedsBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				Config.WEIBO_FEEDER_ID = "1693484332";
				weiboServerImpl.getFeeds();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (weiboServerImpl != null)
		{
			weiboServerImpl.onActivityResult(this, requestCode, resultCode, data);
		}
	}

	@Override
	public void logginStatus(int snsCode, boolean isSuccessLogin,
			Object errorObject) {
		LogController.log("logginStatus >> " + snsCode + " " + isSuccessLogin);
	}

	@Override
	public void getProfileStatus(int snsCode, boolean isSuccessGetProfile,
			Object errorObject) {
		LogController.log("getProfileStatus >> " + snsCode + " " + isSuccessGetProfile);
	}

	@Override
	public void loggoutStatus(int snsCode, boolean isSuccessLogout,
			Object errorObject) {
		LogController.log("loggoutStatus >> " + snsCode + " " + isSuccessLogout);
	}

	@Override
	public void postStatus(int snsCode, boolean isSuccessPost,
			Object errorObject) {
		LogController.log("postStatus >>  " + snsCode + " " + isSuccessPost);
	}

	@Override
	public void getFriendsStatus(boolean arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getFeedsStatus(boolean isSuccess, Object feedsResultObject,
			Object errorObject) {
		if (feedsResultObject != null)
		{
			LogController.log(feedsResultObject.toString());
		}
	}

	@Override
	public void likeFeedStatus(boolean isSuccess, Object feedsResultObject,
			Object errorObject) {
		// TODO Auto-generated method stub

	}

}
