package com.fancl.iloyalty.pojo;

import java.io.Serializable;
import java.util.List;

import com.fancl.iloyalty.util.DataUtil;

public class GPReward{
	private int status;
	private String name;
	private String discount;
	private String vipGrade;
	private String vipGradeName;
	private String fanclMemberId;
	private String gpBalance;
	private String dpBalance;
	private String expireDate;
	private List<GPRewardItem> itemList;
	
	public GPReward() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getVipGrade() {
		return vipGrade;
	}

	public void setVipGrade(String vipGrade) {
		this.vipGrade = vipGrade;
	}

	public String getVipGradeName() {
		return vipGradeName;
	}

	public void setVipGradeName(String vipGradeName) {
		this.vipGradeName = vipGradeName;
	}

	public String getFanclMemberId() {
		return fanclMemberId;
	}

	public void setFanclMemberId(String fanclMemberId) {
		this.fanclMemberId = fanclMemberId;
	}

	public String getGpBalance() {
		return gpBalance;
	}

	public void setGpBalance(String gpBalance) {
		this.gpBalance = gpBalance;
	}

	public String getDpBalance() {
		return dpBalance;
	}

	public void setDpBalance(String dpBalance) {
		this.dpBalance = dpBalance;
	}

	public String getExpireDate() {
		String returnString = DataUtil.trimDateString(expireDate);
		return returnString;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public List<GPRewardItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<GPRewardItem> itemList) {
		this.itemList = itemList;
	}

	@Override
	public String toString() {
		return "GPReward [status=" + status + ", name=" + name + ", discount="
				+ discount + ", vipGrade=" + vipGrade + ", vipGradeName="
				+ vipGradeName + ", fanclMemberId=" + fanclMemberId
				+ ", gpBalance=" + gpBalance + ", dpBalance=" + dpBalance
				+ ", expireDate=" + expireDate + ", itemList=" + itemList + "]";
	}
}
