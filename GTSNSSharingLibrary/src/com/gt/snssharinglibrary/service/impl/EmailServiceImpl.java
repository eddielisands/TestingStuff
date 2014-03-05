package com.gt.snssharinglibrary.service.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.gt.snssharinglibrary.Config;
import com.gt.snssharinglibrary.pojo.SNSShareDetail;
import com.gt.snssharinglibrary.service.SNSService;
import com.gt.snssharinglibrary.service.SNSServiceCallback;
import com.gt.snssharinglibrary.service.SNSServiceImpl.SNS_LOGIN_TYPE;
import com.gt.snssharinglibrary.util.StringUtil;

public class EmailServiceImpl extends SNSServiceSuperImpl implements SNSService {

	@Override
	public void loadConfig() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSNSServiceCallback(SNSServiceCallback snsServiceCallback) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLogged(Context context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void login(Activity activity, Handler handler,
			SNS_LOGIN_TYPE snsLoginType) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logout(Activity activity, Handler handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logginStatus(Context context, boolean isSuccessLogin,
			Object errorObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loggoutStatus(Context context, boolean isSuccessLogout,
			Object errorObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void post(Context context, SNSShareDetail snsShareDetail) {
		if (snsShareDetail != null && context instanceof Activity)
		{
			String subject = snsShareDetail.getTitle();
			String message = snsShareDetail.getLink();

			Intent emailIntent = new Intent(android.content.Intent.ACTION_SENDTO);

			if (subject != null)
			{
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
			}

			if (message != null)
			{
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
			}

			emailIntent.setType("text/plain");
			emailIntent.setData(Uri.parse("mailto:")); // or just "mailto:" for
														// blank
			emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(Intent.createChooser(emailIntent, Config.EMAIL_SHARE_LABEL));
		}
	}

	@Override
	public void postStatus(boolean isSuccessPost, Object errorObject) {
		// TODO Auto-generated method stub

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
