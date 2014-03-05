package com.fancl.iloyalty.pojo;

public class ContactUs {
	private String objectId;
	private String officeHourZh;
	private String officeHourSc;
	private String officeHourEn;
	private String addressZh;
	private String addressSc;
	private String addressEn;
	private String phone;
	private String email;
	
	public ContactUs(String objectId, String officeHourZh, String officeHourSc,
			String officeHourEn, String addressZh, String addressSc,
			String addressEn, String phone, String email)
	{
		this.objectId = objectId;
		this.officeHourZh = officeHourZh;
		this.officeHourSc = officeHourSc;
		this.officeHourEn = officeHourEn;
		this.addressZh = addressZh;
		this.addressSc = addressSc;
		this.addressEn = addressEn;
		this.phone = phone;
		this.email = email;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getOfficeHourZh() {
		return officeHourZh;
	}

	public void setOfficeHourZh(String officeHourZh) {
		this.officeHourZh = officeHourZh;
	}

	public String getOfficeHourSc() {
		return officeHourSc;
	}

	public void setOfficeHourSc(String officeHourSc) {
		this.officeHourSc = officeHourSc;
	}

	public String getOfficeHourEn() {
		return officeHourEn;
	}

	public void setOfficeHourEn(String officeHourEn) {
		this.officeHourEn = officeHourEn;
	}

	public String getAddressZh() {
		return addressZh;
	}

	public void setAddressZh(String addressZh) {
		this.addressZh = addressZh;
	}

	public String getAddressSc() {
		return addressSc;
	}

	public void setAddressSc(String addressSc) {
		this.addressSc = addressSc;
	}

	public String getAddressEn() {
		return addressEn;
	}

	public void setAddressEn(String addressEn) {
		this.addressEn = addressEn;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "ContactUs [officeHourZh=" + officeHourZh + ", officeHourSc=" + officeHourSc + ", officeHourEn=" + officeHourEn + ", addressZh=" + addressZh + ", addressSc=" + addressSc + ", addressEn=" + addressEn + ", phone=" + phone + ", email=" + email + ", objectId=" + objectId + "]";
	}
}
