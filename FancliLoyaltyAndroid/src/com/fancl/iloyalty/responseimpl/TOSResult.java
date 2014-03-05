package com.fancl.iloyalty.responseimpl;

public class TOSResult {
	private int status;
	private String oldMembershipId;
	private String newMembershipId;
	private String errMsgEn;
	private String errMsgZh;
	private String errMsgSc;
	
	public TOSResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getOldMembershipId() {
		return oldMembershipId;
	}

	public void setOldMembershipId(String oldMembershipId) {
		this.oldMembershipId = oldMembershipId;
	}

	public String getNewMembershipId() {
		return newMembershipId;
	}

	public void setNewMembershipId(String newMembershipId) {
		this.newMembershipId = newMembershipId;
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

	@Override
	public String toString() {
		return "TOSResult [status=" + status + ", oldMembershipId="
				+ oldMembershipId + ", newMembershipId=" + newMembershipId
				+ ", errMsgEn=" + errMsgEn + ", errMsgZh=" + errMsgZh
				+ ", errMsgSc=" + errMsgSc + "]";
	}
}
