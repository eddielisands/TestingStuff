package com.fancl.iloyalty.responseimpl;

public class PromotionCountResult {
	
	private int status;
	private String countType;
	private String count;
	private String unreadCount;
	
	public PromotionCountResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCountType() {
		return countType;
	}

	public void setCountType(String countType) {
		this.countType = countType;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getUnreadCount() {
		return unreadCount;
	}

	public void setUnreadCount(String unreadCount) {
		this.unreadCount = unreadCount;
	}

	@Override
	public String toString() {
		return "PromotionCountResult [status=" + status + ", countType="
				+ countType + ", count=" + count + ", unreadCount="
				+ unreadCount + "]";
	}
}
