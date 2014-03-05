package com.fancl.iloyalty.pojo;

public class PurchaseHistoryReceipt {
	
	private int lineNo;
	private String lineAlign;
	private String lineData;
	private String errorCode;
	private String errorMessage;
	
	public PurchaseHistoryReceipt() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getLineNo() {
		return lineNo;
	}

	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}

	public String getLineAlign() {
		return lineAlign;
	}

	public void setLineAlign(String lineAlign) {
		this.lineAlign = lineAlign;
	}

	public String getLineData() {
		return lineData;
	}

	public void setLineData(String lineData) {
		this.lineData = lineData;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return "PurchaseHistoryReceipt [lineNo=" + lineNo + ", lineAlign="
				+ lineAlign + ", lineData=" + lineData + ", errorCode="
				+ errorCode + ", errorMessage=" + errorMessage + "]";
	}
}
