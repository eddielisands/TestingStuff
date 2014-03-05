package com.fancl.iloyalty.pojo;

import com.fancl.iloyalty.util.DataUtil;

public class User {
	private int status;
	
	private String name;
	private String fanclMemberId;
	private String firstName;
	private String lastName;
	private String email;
	private String mobile;
	private String memberType;
	private String gpBalance;
	private String dpBalance;
	private String expiryDate;
	private String monthOfBirth;
	private String yearOfBirth;
	private String gender;
	private String skinType;
	private String country;
	private String city;
	private String address1;
	private String address2;
	private String address3;
	private String vipGrade;
	private String vipGradeName;
	
	private String errMsgZh;
	private String errMsgSc;
	private String errMsgEn;
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFanclMemberId() {
		return fanclMemberId;
	}

	public void setFanclMemberId(String fanclMemberId) {
		this.fanclMemberId = fanclMemberId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMemberType() {
		return memberType;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	public String getGpBalance() {
		return gpBalance;
	}

	public void setGpBalance(String gpBalance) {
		this.gpBalance = gpBalance;
	}

	public String getDpBalance() {
		return dpBalance;
	}

	public void setDpBalance(String dpBalance) {
		this.dpBalance = dpBalance;
	}

	public String getExpiryDate() {
		String returnString = DataUtil.trimDateString(expiryDate);
		return returnString;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getMonthOfBirth() {
		return monthOfBirth;
	}

	public void setMonthOfBirth(String monthOfBirth) {
		this.monthOfBirth = monthOfBirth;
	}

	public String getYearOfBirth() {
		return yearOfBirth;
	}

	public void setYearOfBirth(String yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSkinType() {
		return skinType;
	}

	public void setSkinType(String skinType) {
		this.skinType = skinType;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getVipGrade() {
		return vipGrade;
	}

	public void setVipGrade(String vipGrade) {
		this.vipGrade = vipGrade;
	}

	public String getVipGradeName() {
		return vipGradeName;
	}

	public void setVipGradeName(String vipGradeName) {
		this.vipGradeName = vipGradeName;
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

	public String getErrMsgEn() {
		return errMsgEn;
	}

	public void setErrMsgEn(String errMsgEn) {
		this.errMsgEn = errMsgEn;
	}

	@Override
	public String toString() {
		return "User [status=" + status + ", name=" + name + ", fanclMemberId="
				+ fanclMemberId + ", firstName=" + firstName + ", lastName="
				+ lastName + ", email=" + email + ", mobile=" + mobile
				+ ", memberType=" + memberType + ", gpBalance=" + gpBalance
				+ ", dpBalance=" + dpBalance + ", expiryDate=" + expiryDate
				+ ", monthOfBirth=" + monthOfBirth + ", yearOfBirth="
				+ yearOfBirth + ", gender=" + gender + ", skinType=" + skinType
				+ ", country=" + country + ", city=" + city + ", address1="
				+ address1 + ", address2=" + address2 + ", address3="
				+ address3 + ", vipGrade=" + vipGrade + ", vipGradeName="
				+ vipGradeName + ", errMsgZh=" + errMsgZh + ", errMsgSc="
				+ errMsgSc + ", errMsgEn=" + errMsgEn + "]";
	}
}
