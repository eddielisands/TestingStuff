package com.gt.snssharinglibrary.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.gt.snssharinglibrary.Config;
import com.gt.snssharinglibrary.Constants;
import com.gt.snssharinglibrary.StringMapping;
import com.gt.snssharinglibrary.asynctask.TwitterPostTweetAsyncTask;
import com.gt.snssharinglibrary.pojo.SNSShareDetail;
import com.gt.snssharinglibrary.service.SNSService;
import com.gt.snssharinglibrary.service.SNSServiceCallback;
import com.gt.snssharinglibrary.service.SNSServiceImpl.SNS_LOGIN_TYPE;
import com.gt.snssharinglibrary.service.twitter.LoginDialogListener;
import com.gt.snssharinglibrary.service.twitter.TwitterSessionStore;
import com.gt.snssharinglibrary.util.LogController;
import com.gt.snssharinglibrary.util.StringUtil;
import com.sugree.twitter.DialogError;
import com.sugree.twitter.Twitter;
import com.sugree.twitter.TwitterError;
import com.sugree.twitter.Util;

public class TwitterServiceImpl extends SNSServiceSuperImpl implements
		SNSService {

	private Twitter twitter;

	private String CONSUMER_KEY;
	private String CONSUMER_SECRET;

	private SNSServiceCallback snsServiceCallback;

	private boolean duringShare = false;

	private int twitterDialogCloseBtnResId = 0;

	public TwitterServiceImpl(Context context)
	{
		loadConfig();

		this.twitter = new Twitter(context.getResources(), twitterDialogCloseBtnResId);
		TwitterSessionStore.restore(twitter, context);
	}

	@Override
	public void loadConfig() {
		CONSUMER_KEY = Config.TWITTER_CONSUMER_KEY;
		CONSUMER_SECRET = Config.TWITTER_CONSUMER_SECRET;
		twitterDialogCloseBtnResId = Config.TWITTER_DIALOG_CLOSE_BTN_RESOURCES_ID;
	}

	@Override
	public void setSNSServiceCallback(SNSServiceCallback snsServiceCallback) {
		this.snsServiceCallback = snsServiceCallback;
	}

	@Override
	public boolean isLogged(Context context) {
		if (twitter != null)
		{
			return this.twitter.isSessionValid();
		}

		return false;
	}

	@Override
	public void login(Activity activity, Handler handler,
			SNS_LOGIN_TYPE snsLoginType) {
		if (!this.isLogged(activity))
		{
			LoginDialogListener loginDialogListener = new LoginDialogListener(activity, this);
			this.twitter.authorize(activity, handler, this.CONSUMER_KEY, this.CONSUMER_SECRET, loginDialogListener);
		}
		else
		{
			this.logginStatus(activity, true, null);
		}
	}

	@Override
	public void logout(Activity activity, Handler handler) {
		if (this.twitter != null)
		{
			try
			{
				this.twitter.logout(activity);
				loggoutStatus(activity, true, null);
				return;
			}
			catch (MalformedURLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			loggoutStatus(activity, false, null);
		}
	}

	@Override
	public void logginStatus(Context context, boolean isSuccessLogin,
			Object errorObject) {
		if (errorObject != null)
		{
			if (errorObject instanceof String)
			{
				if (!((String) errorObject).equals("Action Cancel"))
				{
					super.showMessageDialog((Activity) context, StringMapping.TWITTER_LOGIN_DIALOG_TITLE_LABLE, StringMapping.TWITTER_LOGIN_DIALOG_FAILED_MESSAGE);
					LogController.log("Twitter logginStatus : " + isSuccessLogin + " " + (String) errorObject);
				}
			}
			else if (errorObject instanceof TwitterError)
			{
				LogController.log("no unknown error");
				super.showMessageDialog((Activity) context, StringMapping.TWITTER_LOGIN_DIALOG_TITLE_LABLE, ((TwitterError) errorObject).getMessage());
			}
			else if (errorObject instanceof DialogError)
			{
				DialogError dialogError = (DialogError) errorObject;
				LogController.log("no unknown error");
				super.showMessageDialog((Activity) context, StringMapping.TWITTER_LOGIN_DIALOG_TITLE_LABLE, "Network disconnected");
			}
			else
			{
				LogController.log("unknown error" + errorObject);
				super.showMessageDialog((Activity) context, StringMapping.TWITTER_LOGIN_DIALOG_TITLE_LABLE, "unknow error.");
			}
		}

		if (isSuccessLogin && twitter != null)
		{
			TwitterSessionStore.save(twitter, context);

			LogController.log("Twitter logginStatus : " + twitter.getAccessToken());
			LogController.log("Twitter logginStatus : " + twitter.getSecretToken());
		}

		if (this.snsServiceCallback != null)
		{
			this.snsServiceCallback.logginStatus(Constants.TWITTER_CODE, isSuccessLogin, errorObject);
		}
	}

	@Override
	public void loggoutStatus(Context context, boolean isSuccessLogout,
			Object errorObject) {
		if (isSuccessLogout)
		{
			if (twitter != null)
			{
				twitter.setAccessToken(null);
				twitter.setSecretToken(null);
			}

			TwitterSessionStore.clear(context);

			Util.clearCookies(context);
		}

		if (this.snsServiceCallback != null)
		{
			this.snsServiceCallback.loggoutStatus(Constants.TWITTER_CODE, isSuccessLogout, errorObject);
		}
	}

	@Override
	public void post(Context context, final SNSShareDetail snsShareDetail) {
		if (!this.duringShare && snsShareDetail != null && twitter != null)
		{
			this.duringShare = true;

			TwitterPostTweetAsyncTask twitterPostTweetAsyncTask = new TwitterPostTweetAsyncTask(context, this, twitter, snsShareDetail);
			twitterPostTweetAsyncTask.execute((Void) null);
		}
	}

	@Override
	public void postStatus(boolean isSuccessPost, Object errorObject) {
		if (snsServiceCallback != null)
		{
			snsServiceCallback.postStatus(Constants.TWITTER_CODE, isSuccessPost, errorObject);
		}

		this.duringShare = false;
	}

	@Override
	public void getProfile(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getProfileStatus(Context context, boolean isSuccessGetProfile,
			Object errorObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreate(Activity activity, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActivityResult(Activity activity, int requestCode,
			int resultCode, Intent data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getFriends(int photoWidth, int photoHeight) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getFriendsStatus(boolean isSuccessGetFriends,
			Object friendsResultObject, Object errorObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getFeeds() {
		// TODO Auto-generated method stub

	}

	@Override
	public void getFeedsStatus(boolean isSuccess, Object FeedsResultObject,
			Object errorObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void likeFeed(String feedId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void likeFeedStatus(boolean isSuccess, Object feedsResultObject,
			Object errorObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void openPageByWebView(Activity activity, String link) {
		if (activity != null && !StringUtil.isStringEmpty(link))
		{
			Uri uri = Uri.parse(link);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			activity.startActivity(intent);
		}
	}

}
