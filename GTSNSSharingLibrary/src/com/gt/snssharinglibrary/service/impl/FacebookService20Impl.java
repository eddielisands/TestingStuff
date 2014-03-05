package com.gt.snssharinglibrary.service.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.facebook_2_0.android.AsyncFacebookRunner;
import com.facebook_2_0.android.Facebook;
import com.facebook_2_0.android.Util;
import com.gt.snssharinglibrary.Config;
import com.gt.snssharinglibrary.Constants;
import com.gt.snssharinglibrary.StringMapping;
import com.gt.snssharinglibrary.asynctask.FacebookGetUserProfileAsyncTask;
import com.gt.snssharinglibrary.pojo.CusProgressDialog;
import com.gt.snssharinglibrary.pojo.SNSShareDetail;
import com.gt.snssharinglibrary.service.SNSService;
import com.gt.snssharinglibrary.service.SNSServiceCallback;
import com.gt.snssharinglibrary.service.SNSServiceImpl.SNS_LOGIN_TYPE;
import com.gt.snssharinglibrary.service.facebook_2_0.FacebookSessionStore;
import com.gt.snssharinglibrary.service.facebook_2_0.LoginDialogListener;
import com.gt.snssharinglibrary.service.facebook_2_0.LogoutRequestListener;
import com.gt.snssharinglibrary.service.facebook_2_0.PostFeedListener;
import com.gt.snssharinglibrary.service.facebook_2_0.SampleAuthListener;
import com.gt.snssharinglibrary.service.facebook_2_0.SessionEvents;
import com.gt.snssharinglibrary.util.StringUtil;

public class FacebookService20Impl extends SNSServiceSuperImpl implements
		SNSService {

	private Facebook facebook;
	private AsyncFacebookRunner asyncFacebookRunner;
	private SampleAuthListener sampleAuthListener;
	private LoginDialogListener loginDialogListener;

	private String appId;

	private String[] permissions;

	private int facebookDialogCloseBtnResId = 0;

	private CusProgressDialog logoutProgressDialog;

	// Facebook Login Callback Activity RequestCode
	public static final int FACEBOOK_SERVICE_LOGIN_CALLBACK_REQUEST_CODE = 3387;

	public FacebookService20Impl(Resources resources)
	{
		loadConfig();

		this.facebook = new Facebook(appId, resources, facebookDialogCloseBtnResId);
		this.asyncFacebookRunner = new AsyncFacebookRunner(facebook);
	}

	@Override
	public void loadConfig() {
		this.appId = Config.FACEBOOK_2_0_APP_ID;
		this.permissions = Config.FACEBOOK_2_0_PERMISSIONS;
		this.facebookDialogCloseBtnResId = Config.FACEBOOK_2_0_DIALOG_CLOSE_BTN_RESOURCES_ID;
	}

	@Override
	public void setSNSServiceCallback(SNSServiceCallback snsServiceCallback) {
		this.snsServiceCallback = snsServiceCallback;
	}

	@Override
	public boolean isLogged(Context context) {
		return FacebookSessionStore.restore(this.facebook, context);
	}

	@Override
	public void login(Activity activity, Handler handler,
			SNS_LOGIN_TYPE snsLoginType) {
		if (!this.isLogged(activity))
		{
			if (sampleAuthListener != null)
			{
				sampleAuthListener.removeSNSservice();
				SessionEvents.removeAuthListener(sampleAuthListener);
			}

			sampleAuthListener = new SampleAuthListener(activity, this);
			SessionEvents.addAuthListener(sampleAuthListener);

			if (loginDialogListener == null)
			{
				loginDialogListener = new LoginDialogListener();
			}

			this.facebook.authorize(activity, this.permissions, FACEBOOK_SERVICE_LOGIN_CALLBACK_REQUEST_CODE, loginDialogListener);
		}
		else
		{
			this.logginStatus(activity, true, null);
		}
	}

	@Override
	public void logout(Activity activity, Handler handler) {
		this.logoutProgressDialog = new CusProgressDialog(activity);
		this.logoutProgressDialog.setMessage(StringMapping.FACEBOOK_LOGOUT_LOADING_MESSAGE);
		this.logoutProgressDialog.show();

		SessionEvents.onLogoutBegin();
		asyncFacebookRunner = new AsyncFacebookRunner(facebook);
		asyncFacebookRunner.logout(activity, new LogoutRequestListener(activity, this, handler));
	}

	@Override
	public void logginStatus(Context context, boolean isSuccessLogin,
			Object errorObject) {
		if (isSuccessLogin)
		{
			FacebookSessionStore.saveUserLoggedToFacebook(context, true);
		}

		if (isSuccessLogin)
		{
			FacebookSessionStore.save(facebook, context);

			getProfile(context);
		}
		else
		{
			if (this.snsServiceCallback != null)
			{
				this.snsServiceCallback.logginStatus(Constants.FACEBOOK_CODE, isSuccessLogin, errorObject);
			}
		}
	}

	@Override
	public void loggoutStatus(Context context, boolean isSuccessLogout,
			Object errorObject) {
		if (logoutProgressDialog != null)
		{
			logoutProgressDialog.dismiss();
		}

		if (isSuccessLogout)
		{
			Util.clearCookies(context);
			FacebookSessionStore.clear(context);
			if (facebook != null)
			{
				facebook.setAccessToken(null);
			}
		}

		if (this.snsServiceCallback != null)
		{
			this.snsServiceCallback.logginStatus(Constants.FACEBOOK_CODE, isSuccessLogout, errorObject);
		}
	}

	@Override
	public void post(Context context, SNSShareDetail snsShareDetail) {
		if (snsShareDetail != null)
		{
			Bundle bundle = new Bundle();

			if (snsShareDetail.getTitle() != null)
			{
				bundle.putString("name", snsShareDetail.getTitle());
			}

			if (snsShareDetail.getCaption() != null)
			{
				bundle.putString("caption", snsShareDetail.getCaption());
			}

			if (snsShareDetail.getDescription() != null)
			{
				bundle.putString("description", snsShareDetail.getDescription());
			}

			if (snsShareDetail.getPictureLink() != null)
			{
				bundle.putString("picture", snsShareDetail.getPictureLink());
			}

			if (snsShareDetail.getLink() != null)
			{
				bundle.putString("link", snsShareDetail.getLink());
			}

			if (snsShareDetail.getFriendId() != null)
			{
				bundle.putString("to", snsShareDetail.getFriendId());
			}

			facebook.dialog(context, "feed", bundle, new PostFeedListener(this));
		}
	}

	@Override
	public void postStatus(boolean isSuccessPost, Object errorObject) {
		if (this.snsServiceCallback != null)
		{
			this.snsServiceCallback.postStatus(Constants.FACEBOOK_CODE, isSuccessPost, errorObject);
		}
	}

	@Override
	public void getProfile(Context context) {
		FacebookGetUserProfileAsyncTask facebookGetUserProfileAsyncTask = new FacebookGetUserProfileAsyncTask(context, this, facebook);
		facebookGetUserProfileAsyncTask.execute((Void) null);
	}

	@Override
	public void getProfileStatus(Context context, boolean isSuccessGetProfile,
			Object errorObject) {
		if (!isSuccessGetProfile)
		{
			// clear all Facebook Logged Record
			Util.clearCookies(context);
			FacebookSessionStore.clear(context);
			if (facebook != null)
			{
				facebook.setAccessToken(null);
			}
		}

		if (this.snsServiceCallback != null)
		{
			this.snsServiceCallback.getProfileStatus(Constants.FACEBOOK_CODE, isSuccessGetProfile, errorObject);
		}
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
		if (requestCode == FacebookService20Impl.FACEBOOK_SERVICE_LOGIN_CALLBACK_REQUEST_CODE)
		{
			if (facebook != null)
			{
				facebook.authorizeCallback(requestCode, resultCode, data);
			}
		}
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
