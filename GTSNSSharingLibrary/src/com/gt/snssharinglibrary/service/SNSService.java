package com.gt.snssharinglibrary.service;

import com.gt.snssharinglibrary.pojo.SNSShareDetail;
import com.gt.snssharinglibrary.service.SNSServiceImpl.SNS_LOGIN_TYPE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public interface SNSService {
	
	public void loadConfig();
	
	public void setSNSServiceCallback(SNSServiceCallback snsServiceCallback);
	
	public boolean isLogged(Context context);
	
	public void login(Activity activity, Handler handler, SNS_LOGIN_TYPE snsLoginType);
	
	public void logout(Activity activity, Handler handler);
	
	public void logginStatus(Context context, boolean isSuccessLogin, Object errorObject);
	
	public void loggoutStatus(Context context, boolean isSuccessLogout, Object errorObject);

	public void post(Context context, SNSShareDetail snsShareDetail);
	
	public void postStatus(boolean isSuccessPost, Object errorObject);
	
	public void getProfile(Context context);
	
	public void getProfileStatus(Context context, boolean isSuccessGetProfile, Object errorObject);
	
	public void onCreate(Activity activity, Bundle savedInstanceState);
	
	public void onStart();
	
	public void onStop();
	
	public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data);
	
	public void onSaveInstanceState(Bundle outState);
	
	public void getFriends(int photoWidth, int photoHeight);
	
	public void getFriendsStatus(boolean isSuccessGetFriends, Object friendsResultObject, Object errorObject);
	
	public void getFeeds();
	
	public void getFeedsStatus(boolean isSuccess, Object feedsResultObject, Object errorObject);

	public void likeFeed(String feedId);
	
	public void likeFeedStatus(boolean isSuccess, Object feedsResultObject, Object errorObject);

	void openPageByWebView(Activity activity, String link);
	
}
