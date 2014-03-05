package com.gt.gtsnssharinglibrarydemo;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.gt.snssharinglibrary.Config;
import com.gt.snssharinglibrary.Config.SHARING_TYPE;
import com.gt.snssharinglibrary.pojo.SNSShareDetail;
import com.gt.snssharinglibrary.service.SNSService;
import com.gt.snssharinglibrary.service.SNSServiceCallback;
import com.gt.snssharinglibrary.service.SNSServiceImpl.SNS_LOGIN_TYPE;
import com.gt.snssharinglibrary.service.facebook_2_0.FacebookSessionStore;
import com.gt.snssharinglibrary.service.impl.FacebookService30Impl;
import com.gt.snssharinglibrary.util.LogController;

public class Facebook30TestingActivity extends Activity implements
		SNSServiceCallback {

	private Handler handler = new Handler();

	private SNSService facebookServiceImpl;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (facebookServiceImpl != null)
		{
			facebookServiceImpl.onActivityResult(this, requestCode, resultCode, data);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setTitle("Facebook30TestingActivity");

		Config.facebook30SharingType = SHARING_TYPE.WEB_DIALOG;
		Config.FACEBOOK_PERMISSIONS = new String[] { "email", "user_birthday", "read_friendlists", "user_likes" };

		facebookServiceImpl = new FacebookService30Impl();
		facebookServiceImpl.setSNSServiceCallback(Facebook30TestingActivity.this);
		facebookServiceImpl.onCreate(this, savedInstanceState);

		Button loginBtn = (Button) findViewById(R.id.login_btn);
		loginBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				facebookServiceImpl.login(Facebook30TestingActivity.this, handler, SNS_LOGIN_TYPE.LOGIN_TYPE_NORMAL_LOGIN);
			}
		});

		Button logoutBtn = (Button) findViewById(R.id.logout_btn);
		logoutBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				facebookServiceImpl.logout(Facebook30TestingActivity.this, handler);
			}
		});

		Button postFeedBtn = (Button) findViewById(R.id.post_feed_btn);
		postFeedBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				SNSShareDetail snsShareDetail = null;

				if (Config.facebook30SharingType.equals(Config.SHARING_TYPE.WEB_DIALOG))
				{
					String title = "ABC title";
					String caption = "XYZ caption";
					String description = "123 description " + (new Date()).toString();
					String link = "http://www.google.com.hk";
					String pictureLink = null;
					String friendId = "1105617012";
					Bitmap bitmap = null;
					String picSavePath = null;

					snsShareDetail = new SNSShareDetail(title, caption, description, link, pictureLink, friendId, bitmap, picSavePath);
				}
				else if (Config.facebook30SharingType.equals(Config.SHARING_TYPE.PLAIN_TEXT))
				{
					String message = "Facebook Plain Text Message Testing. " + (new Date()).toString();
					snsShareDetail = new SNSShareDetail(message);
				}

				facebookServiceImpl.post(Facebook30TestingActivity.this, snsShareDetail);
			}
		});

		Button getFriendBtn = (Button) this.findViewById(R.id.get_friend_btn);
		getFriendBtn.setVisibility(View.VISIBLE);
		getFriendBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				facebookServiceImpl.getFriends(200, 200);
			}
		});

		Button getFeedsBtn = (Button) this.findViewById(R.id.get_feeds_btn);
		getFeedsBtn.setVisibility(View.VISIBLE);
		getFeedsBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				Config.FACEBOOK_FEEDER_ID = "606823526";
				facebookServiceImpl.getFeeds();
			}
		});
	}

	@Override
	public void onStart() {
		if (facebookServiceImpl != null)
		{
			facebookServiceImpl.onStart();
		}

		super.onStart();
	}

	@Override
	public void onStop() {
		if (facebookServiceImpl != null)
		{
			facebookServiceImpl.onStop();
		}

		super.onStop();
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
		String accessToken = "";

		Session session = Session.getActiveSession();
		if (session.isOpened())
		{
			accessToken = session.getAccessToken();
		}

		LogController.log("logginStatus >> " + snsCode + " " + isSuccessLogin + " " + accessToken);
	}

	@Override
	public void getProfileStatus(int snsCode, boolean isSuccessGetProfile,
			Object errorObject) {
		LogController.log("getProfileStatus >> " + snsCode + " " + isSuccessGetProfile);

		LogController.log("getProfileStatus >> " + FacebookSessionStore.restoreUsername(this));
		LogController.log("getProfileStatus >> " + FacebookSessionStore.restoreFacebookId(this));
		LogController.log("getProfileStatus >> " + FacebookSessionStore.restoreFacebookEmail(this));
	}

	@Override
	public void loggoutStatus(int snsCode, boolean isSuccessLogout,
			Object errorObject) {
		LogController.log("loggoutStatus >>  " + snsCode + " " + isSuccessLogout);
	}

	@Override
	public void postStatus(int snsCode, boolean isSuccessPost,
			Object errorObject) {
		LogController.log("postStatus >>  " + snsCode + " " + isSuccessPost);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void getFriendsStatus(boolean arg0, Object arg1, Object arg2) {
		if (arg0)
		{
			if (arg1 != null)
			{
				if (arg1 instanceof List<?>)
				{
					List<GraphUser> users = (List<GraphUser>) arg1;
					GraphUser user = null;

					for (int i = 0; i < users.size(); i++)
					{
						user = users.get(i);
						if (user != null)
						{
							try
							{
								LogController.log("Facebook User " + " " + i + " " + user.getId() + " " + user.getProperty("name") + " " + user.getProperty("installed"));
							}
							catch (Exception e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		else
		{
			LogController.log("getFriendsStatus >>  " + arg0 + " ");
		}

		Toast.makeText(this, "Get Friend Finished.", Toast.LENGTH_LONG).show();
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
