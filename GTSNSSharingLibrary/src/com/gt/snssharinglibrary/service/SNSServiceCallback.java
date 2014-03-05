package com.gt.snssharinglibrary.service;

public interface SNSServiceCallback {
	public void logginStatus(int snsCode, boolean isSuccessLogin, Object errorObject);
	
	public void getProfileStatus(int snsCode, boolean isSuccessGetProfile, Object errorObject);
	
	public void loggoutStatus(int snsCode, boolean isSuccessLogout, Object errorObject);
	
	public void postStatus(int snsCode, boolean isSuccessPost, Object errorObject);
	
	public void getFriendsStatus(boolean isSuccessGetFriends, Object friendsResultObject, Object errorObject);
	
	public void getFeedsStatus(boolean isSuccess, Object feedsResultObject, Object errorObject);

	public void likeFeedStatus(boolean isSuccess, Object feedsResultObject, Object errorObject);
}
