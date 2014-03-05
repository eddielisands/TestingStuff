package com.fancl.iloyalty.pojo;

public class QRCode {
	private String objectId; 
	private String qrCodeId;
	private String objectType;
	private String qrCodeString;
	
	public QRCode(String objectId, String qrCodeId, String objectType, String qrCodeString)
	{
		this.objectId = objectId;
		this.qrCodeId = qrCodeId;
		this.objectType = objectType;
		this.qrCodeString = qrCodeString;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getQrCodeId() {
		return qrCodeId;
	}

	public void setQrCodeId(String qrCodeId) {
		this.qrCodeId = qrCodeId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getQrCodeString() {
		return qrCodeString;
	}

	public void setQrCodeString(String qrCodeString) {
		this.qrCodeString = qrCodeString;
	}

	@Override
	public String toString() {
		return "QRCode [qrCodeId=" + qrCodeId + ", objectType=" + objectType + ", qrCodeString=" + qrCodeString + ", objectId=" + objectId + "]";
	}
	
	
}
