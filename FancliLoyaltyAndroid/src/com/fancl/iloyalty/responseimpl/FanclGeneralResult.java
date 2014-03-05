package com.fancl.iloyalty.responseimpl;

public class FanclGeneralResult {
	
	private int status;
	private String errMsgEn;
	private String errMsgZh;
	private String errMsgSc;
	private String fanclMemberId;
	
	public FanclGeneralResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
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

	public String getFanclMemberId() {
		return fanclMemberId;
	}

	public void setFanclMemberId(String fanclMemberId) {
		this.fanclMemberId = fanclMemberId;
	}

	@Override
	public String toString() {
		return "FanclGeneralResult [status=" + status + ", errMsgEn="
				+ errMsgEn + ", errMsgZh=" + errMsgZh + ", errMsgSc="
				+ errMsgSc + ", fanclMemberId=" + fanclMemberId + "]";
	}
}
