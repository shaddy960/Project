package com.cg.ibs.investment.bean;

import java.util.Set;
import java.util.TreeSet;

import com.cg.ibs.common.bean.TransactionBean;

public class InvestmentBean extends MutualFund {

	private String UCI;				//Declaring Unique Customer Id of a Customer
	private String userId;			//Declaring User Id of a Customer
	private String password;		//Declaring Password of a Customer
	private double goldunits;		//Declaring Gold units of a Customer
	private double silverunits;		//Declaring Silver units of a Customer
	private double balance;			//Declaring Balance of a Customer
	private Set<MutualFund> funds;	//Set of Mutual Funds of a Customer
	private TreeSet<TransactionBean> transactionList;

	public InvestmentBean(String uCI, String userId, String password, double goldunits, double silverunits,
			double balance, Set<MutualFund> funds, TreeSet<TransactionBean> transactionList) {
		super();
		UCI = uCI;
		this.userId = userId;
		this.password = password;
		this.goldunits = goldunits;
		this.silverunits = silverunits;
		this.balance = balance;
		this.funds = funds;
		this.transactionList = transactionList;
	}

	public InvestmentBean(String userId, String password) {
		super();
		this.userId = userId;
		this.password = password;
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

	public double getGoldunits() {
		return goldunits;
	}

	public void setGoldunits(double goldunits) {
		this.goldunits = goldunits;
	}

	public double getSilverunits() {
		return silverunits;
	}

	public void setSilverunits(double silverunits) {
		this.silverunits = silverunits;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public Set<MutualFund> getFunds() {
		return funds;
	}

	public void setFunds(Set<MutualFund> funds) {
		this.funds = funds;
	}

	public TreeSet<TransactionBean> getTransactionList() {
		return transactionList;
	}

	public void setTransactionList(TreeSet<TransactionBean> transactionList) {
		this.transactionList = transactionList;
	}

	@Override
	public String toString() {
		return "InvestmentBean [UCI=" + UCI + ", goldunits=" + goldunits + ", silverunits=" + silverunits + ", balance="
				+ balance + ", funds=" + funds + "]";
	}

}
