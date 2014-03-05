package com.fancl.iloyalty.service;

import android.content.Context;

public interface SNSService {
	
	public boolean isLogin(Context context);
	
	public void login(Context context);
	
	public void logout(Context context);
	
	public void logginStatus(boolean isSuccessLogin, Object errorObject);
	
	public void loggoutStatus(boolean isSuccessLogout, Object errorObject);
	
	public void getProfile(Context context);
	
	public void getProfileStatus(boolean isSuccessGetProfile, Object errorObject);
	
	public void post(Context context, String message);
	
	public void postStatus(boolean isSuccessPost, Object errorObject);
}
