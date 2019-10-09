package com.cg.ibs.investment.service;
//Bank Service Interface with all the methods 

import com.cg.ibs.investment.bean.BankMutualFund;
import com.cg.ibs.investment.exception.IBSException;

public interface BankService {
	public void updateGoldPrice(double goldPrice) throws IBSException;

	public void updateSilverPrice(double silverPrice) throws IBSException;

	public void addMF(BankMutualFund mutualFund) throws IBSException;

	public boolean validateBank(String userId, String password) throws IBSException;
}