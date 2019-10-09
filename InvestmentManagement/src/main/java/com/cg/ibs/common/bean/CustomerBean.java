package com.cg.ibs.common.bean;

import java.time.LocalDate;
import java.util.Set;

public class CustomerBean {
	private String firstName;
	private String lastName;
	private String fatherName;
	private String motherName;
	private LocalDate dob;
	private Gender gender;;
	private AddressBean permanentAddress;
	private AddressBean currentAddress;
	private String mobileNumber;
	private String alternateMobileNumber;
	private String emailId;
	private String aadharNumber;
	private String panNumber;
	private String UCI; // 16 digit Unique Customer ID
	private String userId; // unique credentials created by customer for login
	private String password; // unique credentials created by customer for login
	private Set<AccountBean> accounts;

	public CustomerBean() {
		super();
	}

	public CustomerBean(String firstName, String lastName, String fatherName, String motherName, LocalDate dob,
			Gender gender, AddressBean permanentAddress, AddressBean currentAddress, String mobileNumber,
			String alternateMobileNumber, String emailId, String aadharNumber, String panNumber, String uCI,
			String userId, String password, Set<AccountBean> accounts) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.fatherName = fatherName;
		this.motherName = motherName;
		this.dob = dob;
		this.gender = gender;
		this.permanentAddress = permanentAddress;
		this.currentAddress = currentAddress;
		this.mobileNumber = mobileNumber;
		this.alternateMobileNumber = alternateMobileNumber;
		this.emailId = emailId;
		this.aadharNumber = aadharNumber;
		this.panNumber = panNumber;
		UCI = uCI;
		this.userId = userId;
		this.password = password;
		this.accounts = accounts;
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

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getMotherName() {
		return motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public AddressBean getPermanentAddress() {
		return permanentAddress;
	}

	public void setPermanentAddress(AddressBean permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	public AddressBean getCurrentAddress() {
		return currentAddress;
	}

	public void setCurrentAddress(AddressBean currentAddress) {
		this.currentAddress = currentAddress;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getAlternateMobileNumber() {
		return alternateMobileNumber;
	}

	public void setAlternateMobileNumber(String alternateMobileNumber) {
		this.alternateMobileNumber = alternateMobileNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getUCI() {
		return UCI;
	}

	public void setUCI(String uCI) {
		UCI = uCI;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<AccountBean> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<AccountBean> accounts) {
		this.accounts = accounts;
	}

	enum Gender {
		MALE, FEMALE, OTHERS
	}

}
