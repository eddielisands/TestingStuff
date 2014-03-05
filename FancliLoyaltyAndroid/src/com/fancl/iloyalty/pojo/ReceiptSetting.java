package com.fancl.iloyalty.pojo;

public class ReceiptSetting {
	private int status;
	private String printReceipt;
	private String emailReceipt;
	
	public ReceiptSetting() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ReceiptSetting(int status, String printReceipt,
			String emailReceipt) {
		this.status = status;
		this.printReceipt = printReceipt;
		this.emailReceipt = emailReceipt;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPrintReceipt() {
		return printReceipt;
	}

	public void setPrintReceipt(String printReceipt) {
		this.printReceipt = printReceipt;
	}

	public String getEmailReceipt() {
		return emailReceipt;
	}

	public void setEmailReceipt(String emailReceipt) {
		this.emailReceipt = emailReceipt;
	}

	@Override
	public String toString() {
		return "ReceiptSetting [status=" + status + ", printReceipt="
				+ printReceipt + ", emailReceipt=" + emailReceipt + "]";
	}
	
	
}
