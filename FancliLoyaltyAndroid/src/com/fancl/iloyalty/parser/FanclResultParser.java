package com.fancl.iloyalty.parser;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;

import com.fancl.iloyalty.AndroidProjectApplication;
import com.fancl.iloyalty.Constants;
import com.fancl.iloyalty.exception.FanclException;
import com.fancl.iloyalty.pojo.GPReward;
import com.fancl.iloyalty.pojo.GPRewardHistoryItem;
import com.fancl.iloyalty.pojo.GPRewardItem;
import com.fancl.iloyalty.pojo.Notification;
import com.fancl.iloyalty.pojo.PurchaseHistory;
import com.fancl.iloyalty.pojo.PurchaseHistoryReceipt;
import com.fancl.iloyalty.pojo.ReceiptSetting;
import com.fancl.iloyalty.pojo.User;
import com.fancl.iloyalty.responseimpl.FanclGeneralResult;
import com.fancl.iloyalty.responseimpl.TOSResult;
import com.fancl.iloyalty.responseimpl.ValidationResult;
import com.fancl.iloyalty.util.DataUtil;
import com.fancl.iloyalty.util.LogController;
import com.longevitysoft.android.xml.plist.domain.Array;
import com.longevitysoft.android.xml.plist.domain.Date;
import com.longevitysoft.android.xml.plist.domain.Dict;
import com.longevitysoft.android.xml.plist.domain.PList;
import com.longevitysoft.android.xml.plist.domain.Real;

public class FanclResultParser {

	public ValidationResult parseValidationResult(PList plist) throws FanclException {
		ValidationResult validationResult = new ValidationResult();

		LogController.log("parseValidationResult");

		int status = ((Dict) plist.getRootElement()).getConfigurationInteger("status").getValue();
		validationResult.setStatus(status);

		if (((Dict) plist.getRootElement()).getConfiguration("errMsgEn") != null) {
			validationResult.setErrMsgEn(((Dict) plist.getRootElement()).getConfiguration("errMsgEn").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("errMsgZh") != null) {
			validationResult.setErrMsgZh(((Dict) plist.getRootElement()).getConfiguration("errMsgZh").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("errMsgSc") != null) {
			validationResult.setErrMsgSc(((Dict) plist.getRootElement()).getConfiguration("errMsgSc").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("name") != null) {
			validationResult.setName(((Dict) plist.getRootElement()).getConfiguration("name").getValue());
		}

		LogController.log(validationResult.toString());

		return validationResult;
	}

	public TOSResult parseTOSResult(PList plist) throws FanclException {
		TOSResult theTOSResult = new TOSResult();

		LogController.log("parseTOSResult");

		int status = ((Dict) plist.getRootElement()).getConfigurationInteger("status").getValue();
		theTOSResult.setStatus(status);

		if (((Dict) plist.getRootElement()).getConfiguration("errMsgEn") != null) {
			theTOSResult.setErrMsgEn(((Dict) plist.getRootElement()).getConfiguration("errMsgEn").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("errMsgZh") != null) {
			theTOSResult.setErrMsgZh(((Dict) plist.getRootElement()).getConfiguration("errMsgZh").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("errMsgSc") != null) {
			theTOSResult.setErrMsgSc(((Dict) plist.getRootElement()).getConfiguration("errMsgSc").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("oldMembershipId") != null) {
			theTOSResult.setOldMembershipId(((Dict) plist.getRootElement()).getConfiguration("oldMembershipId").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("newMembershipId") != null) {
			theTOSResult.setNewMembershipId(((Dict) plist.getRootElement()).getConfiguration("newMembershipId").getValue());

			SharedPreferences sharedPreferences = AndroidProjectApplication.application.getSharedPreferences(
					Constants.SHARED_PREFERENCE_APPLICATION_KEY,
					Context.MODE_PRIVATE);
			sharedPreferences.edit().putString(Constants.SHARED_PREFERENCE_MEMBER_ID_KEY, theTOSResult.getNewMembershipId()).commit();
		}

		LogController.log(theTOSResult.toString());

		return theTOSResult;
	}

	public FanclGeneralResult parseGeneralResult(PList plist) throws FanclException {
		FanclGeneralResult fanclGeneralResult = new FanclGeneralResult();

		LogController.log("parseGeneralResult");

		int status = ((Dict) plist.getRootElement()).getConfigurationInteger("status").getValue();
		fanclGeneralResult.setStatus(status);

		if (((Dict) plist.getRootElement()).getConfiguration("errMsgEn") != null) {
			fanclGeneralResult.setErrMsgEn(((Dict) plist.getRootElement()).getConfiguration("errMsgEn").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("errMsgZh") != null) {
			fanclGeneralResult.setErrMsgZh(((Dict) plist.getRootElement()).getConfiguration("errMsgZh").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("errMsgSc") != null) {
			fanclGeneralResult.setErrMsgSc(((Dict) plist.getRootElement()).getConfiguration("errMsgSc").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("fanclMemberId") != null) {
			fanclGeneralResult.setFanclMemberId(((Dict) plist.getRootElement()).getConfiguration("fanclMemberId").getValue());

			SharedPreferences sharedPreferences = AndroidProjectApplication.application.getSharedPreferences(
					Constants.SHARED_PREFERENCE_APPLICATION_KEY,
					Context.MODE_PRIVATE);
			sharedPreferences.edit().putString(Constants.SHARED_PREFERENCE_MEMBER_ID_KEY, fanclGeneralResult.getFanclMemberId()).commit();
		}

		LogController.log(fanclGeneralResult.toString());

		return fanclGeneralResult;
	}

	public User parseMemberProfile(PList plist) throws FanclException
	{
		User user = new User();

		int status = ((Dict) plist.getRootElement()).getConfigurationInteger("status").getValue();
		user.setStatus(status);

		if (((Dict) plist.getRootElement()).getConfiguration("errMsgEn") != null) {
			user.setErrMsgEn(((Dict) plist.getRootElement()).getConfiguration("errMsgEn").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("errMsgZh") != null) {
			user.setErrMsgZh(((Dict) plist.getRootElement()).getConfiguration("errMsgZh").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("errMsgSc") != null) {
			user.setErrMsgSc(((Dict) plist.getRootElement()).getConfiguration("errMsgSc").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("name") != null) {
			user.setName(((Dict) plist.getRootElement()).getConfiguration("name").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("fanclMemberId") != null) {
			user.setFanclMemberId(((Dict) plist.getRootElement()).getConfiguration("fanclMemberId").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("lastName") != null) {
			user.setLastName(((Dict) plist.getRootElement()).getConfiguration("lastName").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("firstName") != null) {
			user.setFirstName(((Dict) plist.getRootElement()).getConfiguration("firstName").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("mobile") != null) {
			user.setMobile(((Dict) plist.getRootElement()).getConfiguration("mobile").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("email") != null) {
			user.setEmail(((Dict) plist.getRootElement()).getConfiguration("email").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("gender") != null) {
			user.setGender(((Dict) plist.getRootElement()).getConfiguration("gender").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("monthOfBirth") != null) {
			user.setMonthOfBirth(((Dict) plist.getRootElement()).getConfiguration("monthOfBirth").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("yearOfBirth") != null) {
			user.setYearOfBirth(((Dict) plist.getRootElement()).getConfiguration("yearOfBirth").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("country") != null) {
			user.setCountry(((Dict) plist.getRootElement()).getConfiguration("country").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("city") != null) {
			user.setCity(((Dict) plist.getRootElement()).getConfiguration("city").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("skinType") != null) {
			user.setSkinType(((Dict) plist.getRootElement()).getConfiguration("skinType").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("address1") != null) {
			user.setAddress1(((Dict) plist.getRootElement()).getConfiguration("address1").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("address2") != null) {
			user.setAddress2(((Dict) plist.getRootElement()).getConfiguration("address2").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("address3") != null) {
			user.setAddress3(((Dict) plist.getRootElement()).getConfiguration("address3").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("gpBalance") != null) {
			user.setGpBalance(((Dict) plist.getRootElement()).getConfiguration("gpBalance").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("dpBalance") != null) {
			user.setDpBalance(((Dict) plist.getRootElement()).getConfiguration("dpBalance").getValue());
		}
//		if (((Dict) plist.getRootElement()).getConfiguration("expiryDate") != null) {
//			user.setExpiryDate(((Dict) plist.getRootElement()).getConfiguration("expiryDate").getValue());
//		}
		
		Date expiryDateObj = ((Dict) plist.getRootElement()).getConfigurationObject("expiryDate");
		java.util.Date tmpExpireDate = expiryDateObj.getValue();

		String expireDate = null;
		expireDate = DataUtil.convertDateToString(tmpExpireDate);
		if (expireDate != null) {
			user.setExpiryDate(expireDate);
		}
		
		if (((Dict) plist.getRootElement()).getConfiguration("vipGrade") != null) {
			user.setVipGrade(((Dict) plist.getRootElement()).getConfiguration("vipGrade").getValue());
		}
		if (((Dict) plist.getRootElement()).getConfiguration("vipGradeName") != null) {
			user.setVipGradeName(((Dict) plist.getRootElement()).getConfiguration("vipGradeName").getValue());
		}

		LogController.log(user.toString());

		return user;
	}

	public Object parsePurchaseHistoryListResult(PList plist) throws FanclException {
		int status = ((Dict) plist.getRootElement()).getConfigurationInteger("status").getValue();

		if (status == 0) {
			List<PurchaseHistory> purchaseHistoryList = new ArrayList<PurchaseHistory>();

			Array itemArray = ((Dict) plist.getRootElement()).getConfigurationArray("item");
			LogController.log("itemArray.size() : " + itemArray.size());

			for (int i = 0; i < itemArray.size(); i++) {
				PurchaseHistory purchaseHistory = new PurchaseHistory();
				Date purchaseDateObj = ((Dict) itemArray.get(i)).getConfigurationObject("purchaseDate");
				java.util.Date tmpPurchaseDate = purchaseDateObj.getValue();
				String purchaseDate = null;
				purchaseDate = DataUtil.convertDateToString(tmpPurchaseDate);
				if (purchaseDate != null) {
					purchaseHistory.setPurchaseDate(purchaseDate);
				}
				
				Date purchaseDatetimeObj = ((Dict) itemArray.get(i)).getConfigurationObject("purchaseDatetime");
				java.util.Date tmpPurchaseDatetime = purchaseDatetimeObj.getValue();
				String purchaseDatetime = null;
				purchaseDatetime = DataUtil.convertDateToStringAddEight(tmpPurchaseDatetime);
				if (purchaseDatetime != null) {
					LogController.log("set purchaseDatetime:"+purchaseDatetime);
					purchaseHistory.setPurchaseDatetime(purchaseDatetime);
				}
				
				String salesMemo = ((Dict) itemArray.get(i)).getConfiguration("salesMemo").getValue();
				if (salesMemo != null) {
					purchaseHistory.setSalesMemo(salesMemo);
				}
				String shopCode = ((Dict) itemArray.get(i)).getConfiguration("shopCode").getValue();
				if (shopCode != null) {
					purchaseHistory.setShopCode(shopCode);
				}
				String shopNameZh = ((Dict) itemArray.get(i)).getConfiguration("shopNameZh").getValue();
				if (shopNameZh != null) {
					purchaseHistory.setShopNameZh(shopNameZh);
				}
				String shopNameSc = ((Dict) itemArray.get(i)).getConfiguration("shopNameSc").getValue();
				if (shopNameSc != null) {
					purchaseHistory.setShopNameSc(shopNameSc);
				}
				String shopNameEn = ((Dict) itemArray.get(i)).getConfiguration("shopNameEn").getValue();
				if (shopNameEn != null) {
					purchaseHistory.setShopNameEn(shopNameEn);
				}
				Real totalAmountObg = ((Dict) itemArray.get(i)).getConfigurationObject("totalAmount");
				float totalAmount = totalAmountObg.getValue();
				purchaseHistory.setTotalAmount(Float.valueOf(totalAmount));
				
				
				String receiptInd = ((Dict) itemArray.get(i)).getConfiguration("receiptInd").getValue();
				if (receiptInd != null) {
					purchaseHistory.setReceiptInd(receiptInd);
				}
				String errCode = ((Dict) itemArray.get(i)).getConfiguration("errorCode").getValue();
				if (errCode != null) {
					purchaseHistory.setErrCode(errCode);
				}
				String errMessage = ((Dict) itemArray.get(i)).getConfiguration("errorMessage").getValue();
				if (errMessage != null) {
					purchaseHistory.setErrMessage(errMessage);
				}

				purchaseHistoryList.add(purchaseHistory);
			}

			return purchaseHistoryList;
		}
		else {
			return parseGeneralResult(plist);
		}
	}

	public Object parsePurchaseHistoryReceipt(PList plist) throws FanclException {
		int status = ((Dict) plist.getRootElement()).getConfigurationInteger("status").getValue();

		if (status == 0) {
			List<PurchaseHistoryReceipt> purchaseHistoryReceiptList = new ArrayList<PurchaseHistoryReceipt>();

			Array itemArray = ((Dict) plist.getRootElement()).getConfigurationArray("item");
			LogController.log("itemArray.size() : " + itemArray.size());

			for (int i = 0; i < itemArray.size(); i++) {
				PurchaseHistoryReceipt purchaseHistoryReceipt = new PurchaseHistoryReceipt();
				int lineNo = ((Dict) itemArray.get(i)).getConfigurationInteger("lineNo").getValue();
				purchaseHistoryReceipt.setLineNo(lineNo);
				String lineAlign = ((Dict) itemArray.get(i)).getConfiguration("lineAlign").getValue();
				if (lineAlign != null) {
					purchaseHistoryReceipt.setLineAlign(lineAlign);
				}
				String lineData = ((Dict) itemArray.get(i)).getConfiguration("lineData").getValue();
				if (lineData != null) {
					purchaseHistoryReceipt.setLineData(lineData);
				}
				String errorCode = ((Dict) itemArray.get(i)).getConfiguration("errorCode").getValue();
				if (errorCode != null) {
					purchaseHistoryReceipt.setErrorCode(errorCode);
				}
				String errorMessage = ((Dict) itemArray.get(i)).getConfiguration("errorMessage").getValue();
				if (errorMessage != null) {
					purchaseHistoryReceipt.setErrorMessage(errorMessage);
				}

				purchaseHistoryReceiptList.add(purchaseHistoryReceipt);
			}

			return purchaseHistoryReceiptList;
		}
		else {
			return parseGeneralResult(plist);
		}
	}

	public Object parseGPRewardListResult(PList plist) throws FanclException {
		int status = ((Dict) plist.getRootElement()).getConfigurationInteger("status").getValue();

		if (status == 0) {
			GPReward gpReward = new GPReward();
			String name = ((Dict) plist.getRootElement()).getConfiguration("name").getValue();
			if (name != null) {
				gpReward.setName(name);
			}
			String discount = ((Dict) plist.getRootElement()).getConfiguration("discount").getValue();
			if (discount != null) {
				gpReward.setDiscount(discount);
			}
			String vipGrade = ((Dict) plist.getRootElement()).getConfiguration("vipGrade").getValue();
			if (vipGrade != null) {
				gpReward.setVipGrade(vipGrade);
			}
			String vipGradeName = ((Dict) plist.getRootElement()).getConfiguration("vipGradeName").getValue();
			if (vipGradeName != null) {
				gpReward.setVipGradeName(vipGradeName);
			}
			String fanclMemberId = ((Dict) plist.getRootElement()).getConfiguration("fanclMemberId").getValue();
			if (fanclMemberId != null) {
				gpReward.setFanclMemberId(fanclMemberId);
			}
			String gpBalance = ((Dict) plist.getRootElement()).getConfiguration("gpBalance").getValue();
			if (gpBalance != null) {
				gpReward.setGpBalance(gpBalance);
			}
			String dpBalance = ((Dict) plist.getRootElement()).getConfiguration("dpBalance").getValue();
			if (dpBalance != null) {
				gpReward.setDpBalance(dpBalance);
			}
			Date expireDateObj = ((Dict) plist.getRootElement()).getConfigurationObject("expiryDate");
			java.util.Date tmpExpireDate = expireDateObj.getValue();

			String expireDate = null;
			expireDate = DataUtil.convertDateToString(tmpExpireDate);
			if (expireDate != null) {
				gpReward.setExpireDate(expireDate);
			}

			List<GPRewardItem> gpRewardItemList = new ArrayList<GPRewardItem>();

			Array itemArray = ((Dict) plist.getRootElement()).getConfigurationArray("item");
			LogController.log("itemArray.size() : " + itemArray.size());

			for (int i = 0; i < itemArray.size(); i++) {
				GPRewardItem gpRewardItem = new GPRewardItem();
				String actionType = ((Dict) itemArray.get(i)).getConfiguration("actionType").getValue();
				if (actionType != null) {
					gpRewardItem.setActionType(actionType);
				}

				Date transactionDateObj = ((Dict) itemArray.get(i)).getConfigurationObject("transactionDate");
				java.util.Date tmpTransactionDate = transactionDateObj.getValue();

				String transactionDate = null;
				transactionDate = DataUtil.convertDateToString(tmpTransactionDate);
				if (transactionDate != null) {
					gpRewardItem.setTransactionDate(transactionDate);
				}
				Real pointAmountObj = ((Dict) itemArray.get(i)).getConfigurationObject("pointAmount");
				float pointAmount = pointAmountObj.getValue();
				gpRewardItem.setPointAmount(pointAmount);
				Real pointBalanceObj = ((Dict) itemArray.get(i)).getConfigurationObject("pointBalance");
				float pointBalance = pointBalanceObj.getValue();
				gpRewardItem.setPointBalance(pointBalance);

				String receiptInd = ((Dict) itemArray.get(i)).getConfiguration("receiptInd").getValue();
				if (receiptInd != null) {
					gpRewardItem.setReceiptInd(receiptInd);
				}
				String giftInd = ((Dict) itemArray.get(i)).getConfiguration("giftInd").getValue();
				if (giftInd != null) {
					gpRewardItem.setGiftInd(giftInd);
				}
				String shopCode = ((Dict) itemArray.get(i)).getConfiguration("shopCode").getValue();
				if (shopCode != null) {
					gpRewardItem.setShopCode(shopCode);
				}
				String salesMemo = ((Dict) itemArray.get(i)).getConfiguration("salesMemo").getValue();
				if (salesMemo != null) {
					gpRewardItem.setSalesMemo(salesMemo);
				}

				Date transactionDatetimeObj = ((Dict) itemArray.get(i)).getConfigurationObject("transactionDatetime");
				java.util.Date tmpTransactionDatetime = transactionDatetimeObj.getValue();

				String transactionDatetime = null;
				transactionDatetime = DataUtil.convertDateToStringAddEight(tmpTransactionDatetime);
				if (transactionDatetime != null) {
					gpRewardItem.setTransactionDatetime(transactionDatetime);
				}

				String itemCode = ((Dict) itemArray.get(i)).getConfiguration("itemCode").getValue();
				if (itemCode != null) {
					gpRewardItem.setItemCode(itemCode);
				}

				gpRewardItemList.add(gpRewardItem);
			}

			gpReward.setItemList(gpRewardItemList);

			return gpReward;
		}
		else {
			return parseGeneralResult(plist);
		}
	}

	public Object parseGPRewardHistoryItem(PList plist) throws FanclException {
		int status = ((Dict) plist.getRootElement()).getConfigurationInteger("status").getValue();

		if (status == 0) {
			GPRewardHistoryItem gpRewardHistoryItem = new GPRewardHistoryItem();
			String nameZh = ((Dict) plist.getRootElement()).getConfiguration("nameZh").getValue();
			if (nameZh != null) {
				gpRewardHistoryItem.setNameZh(nameZh);
			}
			String nameSc = ((Dict) plist.getRootElement()).getConfiguration("nameSc").getValue();
			if (nameSc != null) {
				gpRewardHistoryItem.setNameSc(nameSc);
			}
			String nameEn = ((Dict) plist.getRootElement()).getConfiguration("nameEn").getValue();
			if (nameEn != null) {
				gpRewardHistoryItem.setNameEn(nameEn);
			}
			String thumbnail = ((Dict) plist.getRootElement()).getConfiguration("thumbnail").getValue();
			if (thumbnail != null) {
				gpRewardHistoryItem.setThumbnail(DataUtil.convertImageName(thumbnail));
			}
			String image = ((Dict) plist.getRootElement()).getConfiguration("image").getValue();
			if (image != null) {
				gpRewardHistoryItem.setImage(DataUtil.convertImageName(image));
			}
			String descriptionZh = ((Dict) plist.getRootElement()).getConfiguration("descriptionZh").getValue();
			if (descriptionZh != null) {
				gpRewardHistoryItem.setDescriptionZh(descriptionZh);
			}
			String descriptionSc = ((Dict) plist.getRootElement()).getConfiguration("descriptionSc").getValue();
			if (descriptionSc != null) {
				gpRewardHistoryItem.setDescriptionSc(descriptionSc);
			}
			String descriptionEn = ((Dict) plist.getRootElement()).getConfiguration("descriptionEn").getValue();
			if (descriptionEn != null) {
				gpRewardHistoryItem.setDescriptionEn(descriptionEn);
			}
			String pointNeed = ((Dict) plist.getRootElement()).getConfiguration("pointNeed").getValue();
			if (pointNeed != null) {
				gpRewardHistoryItem.setPointNeed(Integer.valueOf(pointNeed));
			}
			String shopName = ((Dict) plist.getRootElement()).getConfiguration("shopName").getValue();
			if (shopName != null) {
				gpRewardHistoryItem.setShopName(shopName);
			}
			String lineNo = ((Dict) plist.getRootElement()).getConfiguration("lineNo").getValue();
			if (lineNo != null && !lineNo.equals("")) {
				gpRewardHistoryItem.setLineNo(Integer.valueOf(lineNo));
			}
			String itemQuantity = ((Dict) plist.getRootElement()).getConfiguration("itemQuantity").getValue();
			if (itemQuantity != null) {
				gpRewardHistoryItem.setItemQuantity(Integer.valueOf(itemQuantity));
			}
			String gpAmount = ((Dict) plist.getRootElement()).getConfiguration("gpAmount").getValue();
			if (gpAmount != null) {
				gpRewardHistoryItem.setGpAmount(Float.valueOf(gpAmount));
			}

			return gpRewardHistoryItem;
		}
		else {
			return parseGeneralResult(plist);
		}
	}

	public Object parseGetReceiptResult(PList plist) throws FanclException {
		int status = ((Dict) plist.getRootElement()).getConfigurationInteger("status").getValue();

		if (status == 0) {
			ReceiptSetting receiptSetting = new ReceiptSetting();
			receiptSetting.setStatus(status);
			String printReceipt = ((Dict) plist.getRootElement()).getConfiguration("printReceipt").getValue();
			if (printReceipt != null) {
				receiptSetting.setPrintReceipt(printReceipt);
			}
			String emailReceipt = ((Dict) plist.getRootElement()).getConfiguration("emailReceipt").getValue();
			if (emailReceipt != null) {
				receiptSetting.setEmailReceipt(emailReceipt);
			}

			return receiptSetting;
		}
		else {
			return parseGeneralResult(plist);
		}
	}

	public Object parseNotificationResultList(PList plist) throws FanclException {
		int status = ((Dict) plist.getRootElement()).getConfigurationInteger("status").getValue();

		if (status == 0) {
			List<Notification> notificationList = new ArrayList<Notification>();

			Array itemArray = ((Dict) plist.getRootElement()).getConfigurationArray("item");
			LogController.log("itemArray.size() : " + itemArray.size());

			for (int i = 0; i < itemArray.size(); i++) {
				Notification notification = new Notification();
				
				Date createDatetimeObj = ((Dict) itemArray.get(i)).getConfigurationObject("createDatetime");
				java.util.Date tmpCreateDatetime = createDatetimeObj.getValue();
				String createDatetime = null;
				createDatetime = DataUtil.convertDateToString(tmpCreateDatetime);
				if (createDatetime != null) {
					notification.setCreateDatetime(createDatetime);
				}
				
				String contentType = ((Dict) itemArray.get(i)).getConfiguration("contentType").getValue();
				if (contentType != null) {
					notification.setContentType(contentType);
				}
				String recordId = ((Dict) itemArray.get(i)).getConfiguration("recordId").getValue();
				if (recordId != null) {
					notification.setRecordId(recordId);
				}
				String content = ((Dict) itemArray.get(i)).getConfiguration("content").getValue();
				if (content != null) {
					notification.setContent(content);
				}

				notificationList.add(notification);
			}

			return notificationList;
		}
		else {
			return parseGeneralResult(plist);
		}
	}
	
	public String parsePromotionCount(PList plist) throws FanclException {
		String count = "0";
		if (plist == null) {
			return count;
		}
		int status = ((Dict) plist.getRootElement()).getConfigurationInteger("status").getValue();
		if (status != 0) {
			return count;
		}
		String unreadCount = ((Dict) plist.getRootElement()).getConfiguration("unreadCount").getValue();
		if (unreadCount != null) {
			count = unreadCount;
		}
		return count;
	}
	
	public Object parseEarnCredit(PList plist) throws FanclException {
		String credit = ((Dict) plist.getRootElement()).getConfiguration("iCreditAmount").getValue();
		if (credit != null) {
			return credit;
		}
		else {
			return parseGeneralResult(plist);
		}
	}
}
