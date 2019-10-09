package com.cg.ibs.investment.service;
//Customer Service Interface with all the methods 

import java.util.HashMap;

import java.util.TreeSet;

import com.cg.ibs.common.bean.TransactionBean;
import com.cg.ibs.investment.bean.BankMutualFund;
import com.cg.ibs.investment.bean.InvestmentBean;
import com.cg.ibs.investment.bean.MutualFund;
import com.cg.ibs.investment.exception.IBSException;

public interface ClientService {
	public HashMap<Integer, BankMutualFund> viewMFPlans();

	public double viewGoldPrice();

	public double viewSilverPrice();

	public void buyGold(double gunits, String userId) throws IBSException;

	public void sellGold(double gunits, String userId) throws IBSException;

	public void buySilver(double sunits, String userId) throws IBSException;

	public void sellSilver(double sunits, String userId) throws IBSException;

	public void investMF(double mfAmount, String userId, Integer mfId) throws IBSException;

	public void withdrawMF(String userId, MutualFund mutualFund) throws IBSException;

	public InvestmentBean viewInvestments(String userId) throws IBSException;

	public boolean validateCustomer(String userId, String password) throws IBSException;

	public TreeSet<TransactionBean> getTransactions(String userId);
}
