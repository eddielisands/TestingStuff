package com.fancl.iloyalty.pojo;

import java.io.Serializable;

import com.fancl.iloyalty.util.DataUtil;

public class PurchaseHistory implements Serializable{
	
	private String purchaseDate;
	private String purchaseDatetime;
	private String salesMemo;
	private String shopNameZh;
	private String shopNameSc;
	private String shopNameEn;
	private String shopCode;
	private float totalAmount;
	private String receiptInd;
	private String errCode;
	private String errMessage;
	
	public PurchaseHistory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(String purchaseDate) {
		String returnString = DataUtil.trimDateString(purchaseDate);
		this.purchaseDate = returnString;
	}

	public String getPurchaseDatetime() {
		return purchaseDatetime;
	}

	public void setPurchaseDatetime(String purchaseDatetime) {
		this.purchaseDatetime = purchaseDatetime;
	}

	public String getSalesMemo() {
		return salesMemo;
	}

	public void setSalesMemo(String salesMemo) {
		this.salesMemo = salesMemo;
	}

	public String getShopNameZh() {
		return shopNameZh;
	}

	public void setShopNameZh(String shopNameZh) {
		this.shopNameZh = shopNameZh;
	}

	public String getShopNameSc() {
		return shopNameSc;
	}

	public void setShopNameSc(String shopNameSc) {
		this.shopNameSc = shopNameSc;
	}

	public String getShopNameEn() {
		return shopNameEn;
	}

	public void setShopNameEn(String shopNameEn) {
		this.shopNameEn = shopNameEn;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public float getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(float totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getReceiptInd() {
		return receiptInd;
	}

	public void setReceiptInd(String receiptInd) {
		this.receiptInd = receiptInd;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMessage() {
		return errMessage;
	}

	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}

	@Override
	public String toString() {
		return "PurchaseHistory [purchaseDate=" + purchaseDate
				+ ", purchaseDatetime=" + purchaseDatetime + ", salesMemo="
				+ salesMemo + ", shopNameZh=" + shopNameZh + ", shopNameSc="
				+ shopNameSc + ", shopNameEn=" + shopNameEn + ", shopCode="
				+ shopCode + ", totalAmount=" + totalAmount + ", receiptInd="
				+ receiptInd + ", errCode=" + errCode + ", errMessage="
				+ errMessage + "]";
	}
}
