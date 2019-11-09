package com.cg.ibs.investment.service;

import java.time.LocalDate;

import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cg.ibs.investment.bean.BankAdmins;
import com.cg.ibs.investment.bean.BankMutualFund;
import com.cg.ibs.investment.dao.BankAdminsDao;
import com.cg.ibs.investment.dao.BankAdminsDaoImpl;
import com.cg.ibs.investment.dao.BankMutualFundDao;
import com.cg.ibs.investment.dao.BankMutualFundDaoImpl;
import com.cg.ibs.investment.dao.GoldPriceDao;
import com.cg.ibs.investment.dao.GoldPriceDaoImpl;
import com.cg.ibs.investment.dao.SilverPriceDao;
import com.cg.ibs.investment.dao.SilverPriceDaoImpl;
import com.cg.ibs.investment.exception.ErrorMessages;
import com.cg.ibs.investment.exception.IBSException;
import com.cg.ibs.investment.ui.ViewMFPlans;
import com.cg.ibs.investment.util.JPAUtil;

@Service
public class BankServiceImpl implements BankService {
	static Logger log = Logger.getLogger(BankServiceImpl.class.getName());
	
	// Declaring an objects of DAO
	/*GoldPriceDao golDao = new GoldPriceDaoImpl();
	SilverPriceDao silDao = new SilverPriceDaoImpl();
	BankAdminsDao daoObject = new BankAdminsDaoImpl();
	BankMutualFundDao bkDao = new BankMutualFundDaoImpl();
	*/
	

	@Autowired
	GoldPriceDao golDao ;
	@Autowired
	SilverPriceDao silDao ;
	
	BankAdminsDao baObject=new BankAdminsDaoImpl();
	@Autowired
	BankMutualFundDao bkDao ;
	
	
	@Override
	public Boolean removeMF(Integer mfPlanId) {
		Boolean status=false;
		BankMutualFund bk=bkDao.getMfById(mfPlanId);
		bk.setDirStatus(false);
		bk.setSipStatus(false);
		bk.setExpiryDate(LocalDate.now());
		status=true;
		
		return status;
	}
	// To check whether Gold/Silver price is valid
	public boolean isValidGoldSilver(double price) {
		boolean check = false;
		if (price > 0) {
			check = true;
		}
		log.info("Service Validates gold and silver price");
		return check;
	}

	// To check whether Mutual Fund is valid
	public boolean isValidMutualFund(BankMutualFund mutualFund) {
		boolean check = false;
		if (mutualFund.getNav() > 0) {
			check = true;
		}
		log.info("Service validates mutual fund");
		return check;

	}

	// To update Gold price
	public boolean updateGoldPrice(Double goldPrice) throws IBSException {
		boolean status = false;
		if (isValidGoldSilver(goldPrice)) {

			EntityTransaction txn = JPAUtil.getTransaction();
			txn.begin();

			status = golDao.addGoldPrice(goldPrice);
			txn.commit();
		} else {
			log.error("Gold Price not updated");
			throw new IBSException(ErrorMessages.INVALID_PRICE_MESSAGE);

		}
		return status;

	}

	// To update Silver price
	@Override
	public boolean updateSilverPrice(Double silverPrice) throws IBSException {
		boolean status = false;

		if (isValidGoldSilver(silverPrice)) {

			EntityTransaction txn = JPAUtil.getTransaction();
			txn.begin();

			status = silDao.addSilverPrice(silverPrice);
			txn.commit();
		} else {
			log.error("Silver price not updated");
			throw new IBSException(ErrorMessages.INVALID_PRICE_MESSAGE);
		}
		return status;

	}

	// To validate login details of Bank representative
	@Override
	public boolean validateBank(String userId, String password) throws IBSException {

		BankAdmins ba  = baObject.getBankById(userId);
		if (ba != null && userId.equals(ba.getAdminId())) {

			String correctPassword = ba.getPassword();
			if (password.equals(correctPassword)) {
				return true;

			}
		}
		log.error("Bank validated by service");
		return false;

	}

	@Override
	public void addMF(BankMutualFund mutualFund) throws IBSException {
		if (isValidMutualFund(mutualFund)) {

			EntityTransaction txn = JPAUtil.getTransaction();
			txn.begin();
			bkDao.addBankMutFund(mutualFund);
			txn.commit();
		} else {
			log.error("Mutual Fund not added");
			throw new IBSException(ErrorMessages.INVALID_MF_MESSAGE);
		}

	}

	@Override
	public void updateNav(Integer mfPlanId, Double nav) throws IBSException {
		if (isValidGoldSilver(nav)) {

			EntityTransaction txn = JPAUtil.getTransaction();
			txn.begin();

			BankMutualFund fund = bkDao.getMfById(mfPlanId);
			fund.setNav(nav);
			txn.commit();
		} else {
			log.error("NOT A VALID nav VALUE");
			throw new IBSException(ErrorMessages.VALID_NAV);

		}

	}
	@Override
	public void updateMinDir(Integer mfPlanId, Double amt) throws IBSException {
		if (isValidGoldSilver(amt)) {

			EntityTransaction txn = JPAUtil.getTransaction();
			txn.begin();

			BankMutualFund fund = bkDao.getMfById(mfPlanId);
			fund.setMinAmtDir(amt);;
			txn.commit();
		} else {
			log.error("NOT A VALID AMOUNT VALUE");
			throw new IBSException(ErrorMessages.INVALID_AMOUNT_MESSAGE);

		}

	}
	@Override
	public void updateMinSip(Integer mfPlanId, Double amt) throws IBSException {
		if (isValidGoldSilver(amt)) {

			EntityTransaction txn = JPAUtil.getTransaction();
			txn.begin();

			BankMutualFund fund = bkDao.getMfById(mfPlanId);
			fund.setMinAmtSip(amt);;
			txn.commit();
		} else {
			log.error("NOT A VALID nav VALUE");
			throw new IBSException(ErrorMessages.VALID_NAV);

		}

	}
	@Override
	public void updateSipStatus(Integer mfPlanId, Boolean status) throws IBSException {
		
			EntityTransaction txn = JPAUtil.getTransaction();
			txn.begin();

			BankMutualFund fund = bkDao.getMfById(mfPlanId);
			fund.setSipStatus(status);
			txn.commit();
		
	}
	@Override
	public void updateDirStatus(Integer mfPlanId, Boolean status) throws IBSException {
		

			EntityTransaction txn = JPAUtil.getTransaction();
			txn.begin();

			BankMutualFund fund = bkDao.getMfById(mfPlanId);
			fund.setDirStatus(status);
			txn.commit();
		

	}
}