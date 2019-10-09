package com.cg.ibs.investment.dao;

import java.util.HashMap;
import java.util.TreeSet;

import com.cg.ibs.common.bean.TransactionBean;
import com.cg.ibs.investment.bean.*;

public interface ClientDao {
	double viewGoldPrice();

	double viewSilverPrice();

	InvestmentBean viewInvestments(String userId);

	HashMap<Integer, BankMutualFund> viewMF();

	public void updateTransaction(String uCI, int choice, InvestmentBean investmentBean, double amount);

	public void updateUnits(String userId, double gunits, InvestmentBean investmentBean, int choice);

	public void addMFInvestments(double mfAmount, int mfId, InvestmentBean investmentBean);

	public void withdrawMF(String userId, MutualFund mutualFund, InvestmentBean investmentBean);

	public TreeSet<TransactionBean> getTransactions(String userId);

}