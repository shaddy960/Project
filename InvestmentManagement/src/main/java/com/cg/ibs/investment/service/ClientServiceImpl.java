package com.cg.ibs.investment.service;

import java.util.HashMap;
import java.util.TreeSet;
import com.cg.ibs.common.bean.TransactionBean;
import com.cg.ibs.investment.bean.BankMutualFund;
import com.cg.ibs.investment.bean.InvestmentBean;
import com.cg.ibs.investment.bean.MutualFund;
import com.cg.ibs.investment.dao.ClientDao;
import com.cg.ibs.investment.dao.InvestmentDaoImpl;
import com.cg.ibs.investment.exception.ErrorMessages;
import com.cg.ibs.investment.exception.IBSException;

public class ClientServiceImpl implements ClientService {
	static long count = 0;
	//Declaring  an object of Client DAO
	ClientDao clientdao = new InvestmentDaoImpl();

	//To view Mutual Fund plans of the customer
	@Override
	public HashMap<Integer, BankMutualFund> viewMFPlans() {
		return (clientdao.viewMF());
	}

	//To view Gold price
	@Override
	public double viewGoldPrice() {
		return (clientdao.viewGoldPrice());

	}

	//To view Silver price
	@Override
	public double viewSilverPrice() {
		return (clientdao.viewSilverPrice());

	}

	//To buy gold
	@Override
	public void buyGold(double gunits, String userId) throws IBSException {

		if (gunits > 0) {
			InvestmentBean investmentBean = clientdao.viewInvestments(userId);
			if (investmentBean.getBalance() >= gunits * clientdao.viewGoldPrice()) {
				clientdao.updateUnits(userId, gunits, investmentBean, 1);
				double amount = gunits * clientdao.viewGoldPrice();
				clientdao.updateTransaction(userId, 1, investmentBean, amount);

			} else {

				throw new IBSException(ErrorMessages.INSUFF_BALANCE_MESSAGE);
			}

		} else {
			throw new IBSException(ErrorMessages.INVALID_UNITS_MESSAGE);

		}
	}

	//To Sell Gold
	@Override
	public void sellGold(double gunits, String userId) throws IBSException {
		InvestmentBean investmentBean = clientdao.viewInvestments(userId);
		if (gunits > 0) {
			if (gunits < investmentBean.getGoldunits()) {
				clientdao.updateUnits(userId, gunits, investmentBean, 2);

				double amount = gunits * clientdao.viewGoldPrice();
				clientdao.updateTransaction(userId, 2, investmentBean, amount);
			} else {
				throw new IBSException(ErrorMessages.INSUFF_BALANCE_MESSAGE);
			}

		} else {
			throw new IBSException(ErrorMessages.INVALID_UNITS_MESSAGE);
		}

	}

	//To buy Silver
	@Override
	public void buySilver(double sunits, String userId) throws IBSException {

		if (sunits > 0) {
			InvestmentBean investmentBean = clientdao.viewInvestments(userId);
			if (investmentBean.getBalance() >= sunits * clientdao.viewSilverPrice()) {

				clientdao.updateUnits(userId, sunits, investmentBean, 3);
				double amount = sunits * clientdao.viewSilverPrice();
				clientdao.updateTransaction(userId, 1, investmentBean, amount);

			} else {
				throw new IBSException(ErrorMessages.INSUFF_BALANCE_MESSAGE);
			}

		} else {
			throw new IBSException(ErrorMessages.INVALID_UNITS_MESSAGE);
		}

	}

	//To Sell Silver
	@Override
	public void sellSilver(double sunits, String userId) throws IBSException {
		InvestmentBean investmentBean = clientdao.viewInvestments(userId);
		if (sunits > 0) {
			if (sunits < investmentBean.getSilverunits()) {
				clientdao.updateUnits(userId, sunits, investmentBean, 4);
				double amount = sunits * clientdao.viewSilverPrice();
				clientdao.updateTransaction(userId, 2, investmentBean, amount);
			} else {
				throw new IBSException(ErrorMessages.INSUFF_BALANCE_MESSAGE);
			}

		} else {
			throw new IBSException(ErrorMessages.INVALID_UNITS_MESSAGE);
		}
	}

	//To invest in Mutual Funds
	@Override
	public void investMF(double mfAmount, String userId, Integer mfId) throws IBSException {
		if (mfAmount > 0) {
			if (clientdao.viewMF().containsKey(mfId)) {
				InvestmentBean investmentBean = null;

				investmentBean = clientdao.viewInvestments(userId);

				if (investmentBean.getBalance() >= mfAmount) {
					clientdao.addMFInvestments(mfAmount, mfId, investmentBean);

					clientdao.updateTransaction(userId, 1, investmentBean, mfAmount);

				} else {
					throw new IBSException(ErrorMessages.INSUFF_BALANCE_MESSAGE);
				}
			} else {
				throw new IBSException(ErrorMessages.INVALID_DETAILS_MESSAGE);
			}
		} else {
			throw new IBSException(ErrorMessages.INVALID_AMOUNT_MESSAGE);
		}
	}

	//To withdraw from Mutual Funds
	@Override
	public void withdrawMF(String userId, MutualFund mutualFund) throws IBSException {

		if (clientdao.viewInvestments(userId) != null) {
			InvestmentBean investmentBean = clientdao.viewInvestments(userId);
			clientdao.withdrawMF(userId, mutualFund, investmentBean);

			clientdao.updateTransaction(userId, 2, investmentBean, mutualFund.getMfAmount());

		} else {
			throw new IBSException(ErrorMessages.INVALID_DETAILS_MESSAGE);
		}
	}

	//To view Investments of a customer
	@Override
	public InvestmentBean viewInvestments(String userId) throws IBSException {
		if (clientdao.viewInvestments(userId) != null) {
			return clientdao.viewInvestments(userId);
		} else {
			throw new IBSException(ErrorMessages.INVALID_ACCOUNT_MESSAGE);
		}

	}

	//To validate login details of a customer
	@Override
	public boolean validateCustomer(String userId, String password) throws IBSException {
		if (clientdao.viewInvestments(userId) != null) {
			if (userId.equals(clientdao.viewInvestments(userId).getUserId())) {

				String correctPassword = clientdao.viewInvestments(userId).getPassword();
				if (password.equals(correctPassword)) {
					return true;
				} else {
					throw new IBSException(ErrorMessages.INVALID_PASSWORD_MESSAGE);
				}
			}
		} else {
			throw new IBSException(ErrorMessages.INVALID_USERNAME_MESSAGE);
		}
		return false;

	}

	//To generate transaction history of a customer
	@Override
	public TreeSet<TransactionBean> getTransactions(String userId) {
		return clientdao.getTransactions(userId);
	}

}
