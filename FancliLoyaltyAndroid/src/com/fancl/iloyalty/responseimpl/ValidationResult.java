package com.fancl.iloyalty.responseimpl;


public class ValidationResult {
	private Integer status;
	private String errMsgEn;
	private String errMsgZh;
	private String errMsgSc;
	private String name;

	public ValidationResult() {
		super();
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getErrMsgEn() {
		return errMsgEn;
	}

	public void setErrMsgEn(String errMsgEn) {
		this.errMsgEn = errMsgEn;
	}

	public String getErrMsgZh() {
		return errMsgZh;
	}

	public void setErrMsgZh(String errMsgZh) {
		this.errMsgZh = errMsgZh;
	}

	public String getErrMsgSc() {
		return errMsgSc;
	}

	public void setErrMsgSc(String errMsgSc) {
		this.errMsgSc = errMsgSc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ValidationResult [status=" + status + ", errMsgEn=" + errMsgEn
				+ ", errMsgZh=" + errMsgZh + ", errMsgSc=" + errMsgSc
				+ ", name=" + name + "]";
	}
	
}
