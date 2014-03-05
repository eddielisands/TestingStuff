package com.gt.snssharinglibrary.service.weibo;

import android.app.Activity;
import android.os.Bundle;

import com.gt.snssharinglibrary.service.impl.WeiboServiceImpl;
import com.gt.snssharinglibrary.util.LogController;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;

public class AuthDialogListener implements WeiboAuthListener {
	
	private Activity activity;
	private WeiboServiceImpl weiboServiceImpl;
	
	public AuthDialogListener(Activity activity, WeiboServiceImpl weiboServiceImpl)
	{
		this.activity = activity;
		this.weiboServiceImpl = weiboServiceImpl;
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onComplete(Bundle values) {
		String token = values.getString("access_token");
		String expires_in = values.getString("expires_in");
		Oauth2AccessToken accessToken = new Oauth2AccessToken(token, expires_in);

		weiboServiceImpl.setAccessToken(accessToken);

		if (accessToken != null)
		{
			if (accessToken.isSessionValid())
			{

				try
				{
					Class.forName("com.weibo.sdk.android.api.WeiboAPI");//
				}
				catch (ClassNotFoundException e)
				{
					// e.printStackTrace();
					LogController.log("com.weibo.sdk.android.api.WeiboAPI not found");
				}

				AccessTokenKeeper.keepAccessToken(activity, accessToken);
			
				weiboServiceImpl.logginStatus(activity, true, null);
				return;
			}
		}

		weiboServiceImpl.logginStatus(activity, false, null);
	}

	@Override
	public void onError(WeiboDialogError arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onWeiboException(WeiboException arg0) {
		// TODO Auto-generated method stub
		
	}

}
