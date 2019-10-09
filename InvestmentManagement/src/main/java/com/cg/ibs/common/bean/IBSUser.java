package com.cg.ibs.common.bean;

import java.math.BigInteger;

public class IBSUser {
	private BigInteger uid;
	private String password;
	private String username;
	private Role role;
	
	

	public IBSUser() {
		super();
	}

	public IBSUser(BigInteger uid, String password, String username, Role role) {
		super();
		this.uid = uid;
		this.password = password;
		this.username = username;
		this.role = role;
	}

	public BigInteger getUid() {
		return uid;
	}

	public void setUid(BigInteger uid) {
		this.uid = uid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	enum Role {
		ADMIN, CUSTOMER, SPROVIDER
	}

}
