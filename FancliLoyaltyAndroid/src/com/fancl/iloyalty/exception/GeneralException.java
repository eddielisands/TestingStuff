package com.fancl.iloyalty.exception;

public class GeneralException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	private String status;
	private String errMsgEn;
	private String errMsgTc;
	private String errMsgSc;
	
	public GeneralException(String status) {
		super();
		this.status = status;
	}
	
	public GeneralException(String status, String errMsg) {
		super();
		this.status = status;
		errMsgEn = errMsgTc = errMsgSc = errMsg;
	}
	
	public GeneralException(String status, String errMsgEn, String errMsgTc, String errMsgSc) {
		super();
		this.status = status;
		this.errMsgEn = errMsgEn;
		this.errMsgTc = errMsgTc;
		this.errMsgSc = errMsgSc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrMsgEn() {
		return errMsgEn;
	}

	public void setErrMsgEn(String errMsgEn) {
		this.errMsgEn = errMsgEn;
	}

	public String getErrMsgTc() {
		return errMsgTc;
	}

	public void setErrMsgTc(String errMsgTc) {
		this.errMsgTc = errMsgTc;
	}

	public String getErrMsgSc() {
		return errMsgSc;
	}

	public void setErrMsgSc(String errMsgSc) {
		this.errMsgSc = errMsgSc;
	}
}
