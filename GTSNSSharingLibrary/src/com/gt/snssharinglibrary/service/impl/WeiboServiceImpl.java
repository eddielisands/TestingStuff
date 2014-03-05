package com.gt.snssharinglibrary.service.impl;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.gt.snssharinglibrary.Config;
import com.gt.snssharinglibrary.Config.SHARING_TYPE;
import com.gt.snssharinglibrary.Constants;
import com.gt.snssharinglibrary.pojo.SNSShareDetail;
import com.gt.snssharinglibrary.service.SNSService;
import com.gt.snssharinglibrary.service.SNSServiceCallback;
import com.gt.snssharinglibrary.service.SNSServiceImpl.SNS_LOGIN_TYPE;
import com.gt.snssharinglibrary.service.weibo.AuthDialogListener;
import com.gt.snssharinglibrary.util.StringUtil;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.WeiboParameters;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.api.WeiboAPI;
import com.weibo.sdk.android.keep.AccessTokenKeeper;
import com.weibo.sdk.android.net.AsyncWeiboRunner;
import com.weibo.sdk.android.net.RequestListener;
import com.weibo.sdk.android.sso.SsoHandler;

public class WeiboServiceImpl extends SNSServiceSuperImpl implements SNSService {

	private Weibo mWeibo;
	private String CONSUMER_KEY;
	private String REDIRECT_URL;

	private SsoHandler mSsoHandler;

	private Oauth2AccessToken accessToken;

	private SNSServiceCallback snsServiceCallback;

	public WeiboServiceImpl(Context context)
	{
		loadConfig();

		mWeibo = Weibo.getInstance(CONSUMER_KEY, REDIRECT_URL);

		accessToken = AccessTokenKeeper.readAccessToken(context);
	}

	@Override
	public void loadConfig() {
		CONSUMER_KEY = Config.WEIBO_CONSUMER_KEY;
		REDIRECT_URL = Config.WEIBO_REDIRECT_URL;
	}

	@Override
	public void setSNSServiceCallback(SNSServiceCallback snsServiceCallback) {
		this.snsServiceCallback = snsServiceCallback;
	}

	@Override
	public boolean isLogged(Context context) {
		if (accessToken != null)
		{
			return accessToken.isSessionValid();
		}

		return false;
	}

	@Override
	public void login(Activity activity, Handler handler,
			SNS_LOGIN_TYPE snsLoginType) {
		// mWeibo.authorize(activity, new AuthDialogListener(activity, this));
		mSsoHandler = new SsoHandler(activity, mWeibo);
		mSsoHandler.authorize(new AuthDialogListener(activity, this));
	}

	@Override
	public void logout(Activity activity, Handler handler) {
		AccessTokenKeeper.clear(activity);

		loggoutStatus(activity, true, null);
	}

	@Override
	public void logginStatus(Context context, boolean isSuccessLogin,
			Object errorObject) {
		if (snsServiceCallback != null)
		{
			snsServiceCallback.logginStatus(Constants.WEIBO_CODE, isSuccessLogin, errorObject);
		}
	}

	@Override
	public void loggoutStatus(Context context, boolean isSuccessLogout,
			Object errorObject) {
		if (snsServiceCallback != null)
		{
			snsServiceCallback.loggoutStatus(Constants.WEIBO_CODE, isSuccessLogout, errorObject);
		}
	}

	@Override
	public void post(Context context, SNSShareDetail snsShareDetail) {
		if (isLogged(context) && snsShareDetail != null)
		{
			String message = snsShareDetail.getDescription();

			int limited = 140;
			if (message.length() > limited)
			{
				message = message.substring(0, limited);
			}

			// try
			// {
			// message = URLEncoder.encode(message, "UTF-8");
			// }
			// catch (Exception e)
			// {
			// e.printStackTrace();
			// }
			RequestListener requestListener = new RequestListener()
			{
				@Override
				public void onIOException(IOException arg0) {
					postStatus(false, arg0);
				}

				@Override
				public void onError(WeiboException arg0) {
					postStatus(false, arg0);
				}

				@Override
				public void onComplete(String arg0) {
					postStatus(true, null);
				}
			};

			StatusesAPI statusesAPI = new StatusesAPI(accessToken);
			statusesAPI.update(message, "0.0", "0.0", requestListener);
		}
	}

	@Override
	public void postStatus(boolean isSuccessPost, Object errorObject) {
		if (snsServiceCallback != null)
		{
			snsServiceCallback.postStatus(Constants.WEIBO_CODE, isSuccessPost, errorObject);
		}
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
		if (mSsoHandler != null)
		{
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub

	}

	public void setAccessToken(Oauth2AccessToken accessToken) {
		this.accessToken = accessToken;
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
		RequestListener requestListener = new RequestListener()
		{
			@Override
			public void onIOException(IOException arg0) {
				getFeedsStatus(false, arg0, arg0);
			}

			@Override
			public void onError(WeiboException arg0) {
				getFeedsStatus(false, arg0, arg0);
			}

			@Override
			public void onComplete(String arg0) {
				getFeedsStatus(true, arg0, arg0);
			}
		};

		StatusesAPI statusesAPI = new StatusesAPI(accessToken);

		int since_id = 0;
		int max_id = 0;
		int count = 200;
		int page = 1;
		boolean base_app = false;
		WeiboAPI.FEATURE feature = WeiboAPI.FEATURE.ORIGINAL;
		boolean trim_user = false;

		if (!StringUtil.isStringEmpty(Config.WEIBO_FEEDER_ID))
		{
			try
			{
				statusesAPI.userTimeline(Long.parseLong(Config.WEIBO_FEEDER_ID), since_id, max_id, count, page, base_app, feature, trim_user, requestListener);
			}
			catch (Exception e)
			{
				statusesAPI.userTimeline(Config.WEIBO_FEEDER_ID, since_id, max_id, count, page, base_app, feature, trim_user, requestListener);
			}
		}
		else
		{
			getFeedsStatus(false, "", "");
		}
	}

	@Override
	public void getFeedsStatus(boolean isSuccess, Object feedsResultObject,
			Object errorObject) {
		if (snsServiceCallback != null)
		{
			snsServiceCallback.getFeedsStatus(isSuccess, feedsResultObject, errorObject);
		}
	}

	@Override
	public void likeFeed(String feedId) {

		if (!StringUtil.isStringEmpty(feedId))
		{
			RequestListener requestListener = new RequestListener()
			{
				@Override
				public void onIOException(IOException arg0) {
					likeFeedStatus(false, arg0, arg0);
				}

				@Override
				public void onError(WeiboException arg0) {
					likeFeedStatus(false, arg0, arg0);
				}

				@Override
				public void onComplete(String arg0) {
					likeFeedStatus(true, arg0, arg0);
				}
			};

			StatusesAPI statusesAPI = new StatusesAPI(accessToken);
			statusesAPI.repost(Long.parseLong(feedId), "", WeiboAPI.COMMENTS_TYPE.NONE, requestListener);
		}
		else
		{
			likeFeedStatus(false, null, null);
		}
	}

	@Override
	public void likeFeedStatus(boolean isSuccess, Object feedsResultObject,
			Object errorObject) {
		if (snsServiceCallback != null)
		{
			snsServiceCallback.likeFeedStatus(isSuccess, feedsResultObject, errorObject);
		}
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
