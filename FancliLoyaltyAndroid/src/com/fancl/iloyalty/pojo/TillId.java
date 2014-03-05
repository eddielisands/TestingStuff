package com.fancl.iloyalty.pojo;

public class TillId {
	private String objectId;
	private String storeCode;
	private String storeName;
	private String storeTill;
	private String posId;
	
	public TillId(String objectId, String storeCode, String storeName,
			String storeTill, String posId) {
		super();
		this.objectId = objectId;
		this.storeCode = storeCode;
		this.storeName = storeName;
		this.storeTill = storeTill;
		this.posId = posId;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreTill() {
		return storeTill;
	}

	public void setStoreTill(String storeTill) {
		this.storeTill = storeTill;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

	@Override
	public String toString() {
		return "TillId [objectId=" + objectId + ", storeCode=" + storeCode
				+ ", storeName=" + storeName + ", storeTill=" + storeTill
				+ ", posId=" + posId + "]";
	}
}
