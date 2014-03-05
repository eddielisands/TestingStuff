package com.fancl.iloyalty.service;

import java.util.List;

import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.pojo.FormContent;
import com.fancl.iloyalty.pojo.GPReward;
import com.fancl.iloyalty.pojo.GPRewardHistoryItem;
import com.fancl.iloyalty.pojo.Notification;
import com.fancl.iloyalty.pojo.PurchaseHistory;
import com.fancl.iloyalty.pojo.PurchaseHistoryReceipt;
import com.fancl.iloyalty.pojo.User;
import com.fancl.iloyalty.pojo.UserRegistrationParam;
import com.fancl.iloyalty.pojo.ValidateUserParam;
import com.fancl.iloyalty.responseimpl.FanclGeneralResult;
import com.fancl.iloyalty.responseimpl.TOSResult;
import com.fancl.iloyalty.responseimpl.ValidationResult;

public interface AccountService {
	
	public boolean isLogin();
	
	public String currentMemberId();
	
	public String currentDeviceUUID();
	
	public String currentUserToken();
	
	public List<FormContent> getFormContentWithType(String type) throws FanclException;
	
	public ValidationResult validateUserWithMemberId(ValidateUserParam validateUserParam) throws FanclException;
	
	public TOSResult newUserCardReplaceWithOldMemberId(String oldMemberId) throws FanclException;
	
	public FanclGeneralResult registerUserWithMemberId(UserRegistrationParam userRegistrationParam)
			throws FanclException;
	
	public FanclGeneralResult loginWithEmail(String loginName, String password) throws FanclException;
	
	public FanclGeneralResult forgetPasswordWithMobile(String mobile, String email) throws FanclException;
	
	public User getUserProfile() throws FanclException;
	
	public FanclGeneralResult updateUserProfile(User userParam) throws FanclException;
	
	public FanclGeneralResult updateUserPassword(String oldPassword, String newPassword, 
			String retypeNewPassword) throws FanclException;
	
	public Object getPurchaseHistory() throws FanclException;
	
	public Object getPurchaseHistoryReceipt(String purchaseDatetime, 
			String memo, String shopCode) throws FanclException;
	
	public Object getGPRewards() throws FanclException;
	
	public GPRewardHistoryItem getGPRewardsHistoryItem(String purchaseDatetime, String salesMemo, String shopCode, String itemCode) throws FanclException;
	
	public Object getNotificationList() throws FanclException;
	
	public FanclGeneralResult addAndUpdateUser() throws FanclException;
	
	public String currentLanguage();
	
	public String currentUserLanguage();
	
	public void saveFavouriteList (String itemType, String itemId , String favouriteType);
	
	public Object earnCreditWithEventId(String eventId) throws FanclException;
}
