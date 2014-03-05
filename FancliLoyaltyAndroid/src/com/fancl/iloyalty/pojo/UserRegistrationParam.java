package com.fancl.iloyalty.pojo;

public class UserRegistrationParam {
	private String id;
	private String surname;
	private String name;
	private String mobile;
	private String email;
	private String retypeEmail;
	private String gender;
	private String password;
	private String retypePassword;
	private String skinType;
	private String address1;
	private String address2;
	private String address3;
	private String country;
	private String city;
	private String monthOfBirth;
	private String yearOfBirth;
	
	public UserRegistrationParam(String id, String surname, String name,
			String mobile, String email, String retypeEmail, String gender,
			String password, String retypePassword, String skinType,
			String address1, String address2, String address3, String country,
			String city, String monthOfBirth, String yearOfBirth) {
		super();
		this.id = id;
		this.surname = surname;
		this.name = name;
		this.mobile = mobile;
		this.email = email;
		this.retypeEmail = retypeEmail;
		this.gender = gender;
		this.password = password;
		this.retypePassword = retypePassword;
		this.skinType = skinType;
		this.address1 = address1;
		this.address2 = address2;
		this.address3 = address3;
		this.country = country;
		this.city = city;
		this.monthOfBirth = monthOfBirth;
		this.yearOfBirth = yearOfBirth;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRetypeEmail() {
		return retypeEmail;
	}

	public void setRetypeEmail(String retypeEmail) {
		this.retypeEmail = retypeEmail;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRetypePassword() {
		return retypePassword;
	}

	public void setRetypePassword(String retypePassword) {
		this.retypePassword = retypePassword;
	}

	public String getSkinType() {
		return skinType;
	}

	public void setSkinType(String skinType) {
		this.skinType = skinType;
	}

	public String getAddress1() {
		String address = "";
		if (address1 != null) {
			address = address1;
		}
		return address;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		String address = "";
		if (address2 != null) {
			address = address1;
		}
		return address;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		String address = "";
		if (address3 != null) {
			address = address1;
		}
		return address;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		String city = "";
		if (this.city != null) {
			city = this.city;
		}
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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
}
