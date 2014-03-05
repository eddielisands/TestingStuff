package com.fancl.iloyalty.pojo;

import java.io.Serializable;

import com.fancl.iloyalty.util.DataUtil;

public class GPRewardItem implements Serializable{
	private String actionType;
	private String transactionDate;
	private float pointAmount;
	private float pointBalance;
	private String receiptInd;
	private String giftInd;
	private String shopCode;
	private String salesMemo;
	private String transactionDatetime;
	private String itemCode;
	
	public GPRewardItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getTransactionDate() {
		String returnString = DataUtil.trimDateString(transactionDate);
		return returnString;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public float getPointAmount() {
		return pointAmount;
	}

	public void setPointAmount(float pointAmount) {
		this.pointAmount = pointAmount;
	}

	public float getPointBalance() {
		return pointBalance;
	}

	public void setPointBalance(float pointBalance) {
		this.pointBalance = pointBalance;
	}

	public String getReceiptInd() {
		return receiptInd;
	}

	public void setReceiptInd(String receiptInd) {
		this.receiptInd = receiptInd;
	}

	public String getGiftInd() {
		return giftInd;
	}

	public void setGiftInd(String giftInd) {
		this.giftInd = giftInd;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getSalesMemo() {
		return salesMemo;
	}

	public void setSalesMemo(String salesMemo) {
		this.salesMemo = salesMemo;
	}

	public String getTransactionDatetime() {
		return transactionDatetime;
	}

	public void setTransactionDatetime(String transactionDatetime) {
		this.transactionDatetime = transactionDatetime;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	@Override
	public String toString() {
		return "GPRewardItem [actionType=" + actionType + ", transactionDate="
				+ transactionDate + ", pointAmount=" + pointAmount
				+ ", pointBalance=" + pointBalance + ", receiptInd="
				+ receiptInd + ", giftInd=" + giftInd + ", shopCode="
				+ shopCode + ", salesMemo=" + salesMemo
				+ ", transactionDatetime=" + transactionDatetime
				+ ", itemCode=" + itemCode + "]";
	}
}
