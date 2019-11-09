package com.cg.ibs.investment.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.ibs.investment.bean.AccountBean;
import com.cg.ibs.investment.bean.BankMutualFund;
import com.cg.ibs.investment.bean.CustomerBean;
import com.cg.ibs.investment.bean.Frequency;
import com.cg.ibs.investment.bean.InvestmentBean;
import com.cg.ibs.investment.bean.InvestmentTransaction;
import com.cg.ibs.investment.bean.MFType;
import com.cg.ibs.investment.bean.MutualFund;
import com.cg.ibs.investment.bean.TransactionBean;
import com.cg.ibs.investment.bean.TransactionMode;
import com.cg.ibs.investment.bean.TransactionType;
import com.cg.ibs.investment.dao.AccountDao;
import com.cg.ibs.investment.dao.BankMutualFundDao;
import com.cg.ibs.investment.dao.CustomerDao;
import com.cg.ibs.investment.dao.GoldPriceDao;
import com.cg.ibs.investment.dao.InvestmentDao;
import com.cg.ibs.investment.dao.InvestmentTransactionDao;
import com.cg.ibs.investment.dao.MutualFundDao;
import com.cg.ibs.investment.dao.SilverPriceDao;
import com.cg.ibs.investment.exception.ErrorMessages;
import com.cg.ibs.investment.exception.IBSException;
import com.cg.ibs.investment.util.JPAUtil;

@Service
public class CustomerServiceImpl implements CustomerService {
	private static final Logger log = Logger.getLogger(CustomerServiceImpl.class);

	/*
	 * BankMutualFundDao bkDao = new BankMutualFundDaoImpl(); GoldPriceDao golDao =
	 * new GoldPriceDaoImpl(); SilverPriceDao silDao = new SilverPriceDaoImpl();
	 * CustomerDao cs = new CustomerDaoImpl(); InvestmentDao invdao = new
	 * InvestmentDaoImpl(); MutualFundDao mfDao = new MutualFundDaoImpl();
	 * AccountDao accDao= new AccountDaoImpl(); InvestmentTransactionDao trxDao =
	 * new InvestmentTransactionDaoImpl();
	 */
	@Autowired
	BankMutualFundDao bkDao;
	@Autowired
	GoldPriceDao golDao;
	@Autowired
	SilverPriceDao silDao;
	@Autowired
	CustomerDao cs;
	@Autowired
	InvestmentDao invdao;
	@Autowired
	MutualFundDao mfDao;
	@Autowired
	AccountDao accDao;
	@Autowired
	InvestmentTransactionDao trxDao;

	@Override
	public HashMap<Integer, BankMutualFund> viewMFPlans() throws IBSException {

		List<BankMutualFund> bmf = bkDao.getAllBankMutualFunds();
		HashMap<Integer, BankMutualFund> bankMutualFund = new HashMap<>();
		for (BankMutualFund t : bmf) {
			bankMutualFund.put(t.getMfPlanId(), t);
		}
		log.info("Mutual Fund Plans Succesfully Received");
		return bankMutualFund;
	}

	@Override
	public double viewGoldPrice() throws IBSException {

		Double price = golDao.viewGoldPrice();
		log.info("Gold Price Returned");
		return price;
	}

	@Override
	public double viewSilverPrice() throws IBSException {

		Double price = silDao.viewSilverPrice();
		log.info("Silver Price Returned");
		return price;

	}

	@Override
	public void buyGold(double gunits, String userId) throws IBSException {
		EntityTransaction txn = JPAUtil.getTransaction();

		BigInteger uci = cs.getUciByUserId(userId);

		InvestmentBean inv = invdao.getInvestmentByUci(uci);
		if (gunits > 0) {
			if (inv != null) {
				Double balance = Double.parseDouble(inv.getAccount().getBalance().toString());
				if (balance > gunits * viewGoldPrice()) {
					txn.begin();
					inv.setGoldunits(gunits + inv.getGoldunits());
					AccountBean acc = inv.getAccount();

					BigDecimal amount = inv.getAccount().getBalance()
							.subtract(new BigDecimal(gunits * viewGoldPrice()));
					acc.setBalance(amount);
					Set<TransactionBean> tBeans = inv.getAccount().getTransaction();
					InvestmentTransaction tBean = new InvestmentTransaction();
					// tBean.setTransactionId((int) Math.round(Math.random() * 10000));
					tBean.setTransactionAmount(new BigDecimal(gunits * viewGoldPrice()));
					tBean.setTransactionDate(LocalDateTime.now());
					tBean.setAccount(inv.getAccount());
					tBean.setTransactionDescription("gold is bought");
					tBean.setTransactionMode(TransactionMode.ONLINE);
					tBean.setTransactionType(TransactionType.DEBIT);
					tBean.setUnits(gunits);
					tBean.setPricePerUnit(viewGoldPrice());
					tBeans.add(tBean);
					txn.commit();
				} else {
					log.error(ErrorMessages.INSUFF_BALANCE_MESSAGE);
					throw new IBSException(ErrorMessages.INSUFF_BALANCE_MESSAGE);
				}

			}
			log.info("Service buys gold");
		} else {
			log.error("Invalid units");
			throw new IBSException(ErrorMessages.INVALID_UNITS_MESSAGE);

		}
	}

	@Override
	public void sellGold(double gunits, String userId) throws IBSException {
		EntityTransaction txn = JPAUtil.getTransaction();

		BigInteger uci = cs.getUciByUserId(userId);

		InvestmentBean inv = invdao.getInvestmentByUci(uci);
		if (gunits > 0) {
			if (inv != null) {
				Double units = inv.getGoldunits();
				if (units > gunits) {
					txn.begin();
					inv.setGoldunits(inv.getGoldunits() - gunits);
					AccountBean acc = inv.getAccount();

					BigDecimal amount = inv.getAccount().getBalance().add(new BigDecimal(gunits * viewGoldPrice()));
					acc.setBalance(amount);
					Set<TransactionBean> tBeans = inv.getAccount().getTransaction();
					InvestmentTransaction tBean = new InvestmentTransaction();
					// tBean.setTransactionId((int) Math.round(Math.random() * 10000));
					tBean.setTransactionAmount(new BigDecimal(gunits * viewGoldPrice()));
					tBean.setTransactionDate(LocalDateTime.now());
					tBean.setAccount(inv.getAccount());
					tBean.setTransactionDescription("gold is sold");
					tBean.setTransactionMode(TransactionMode.ONLINE);
					tBean.setTransactionType(TransactionType.CREDIT);
					tBean.setUnits(gunits);
					tBean.setPricePerUnit(viewGoldPrice());
					tBeans.add(tBean);
					txn.commit();
				} else {
					log.error(ErrorMessages.INSUFF_GOLD_UNITS);
					throw new IBSException(ErrorMessages.INSUFF_GOLD_UNITS);
				}
			}
			log.info("Service sells gold");
		} else {
			log.error(ErrorMessages.INVALID_UNITS_MESSAGE);
			throw new IBSException(ErrorMessages.INVALID_UNITS_MESSAGE);
		}
	}

	@Override
	public void buySilver(double sunits, String userId) throws IBSException {
		EntityTransaction txn = JPAUtil.getTransaction();

		BigInteger uci = cs.getUciByUserId(userId);

		InvestmentBean inv = invdao.getInvestmentByUci(uci);
		if (sunits > 0) {
			if (inv != null) {
				Double balance = Double.parseDouble(inv.getAccount().getBalance().toString());
				if (balance > sunits * viewSilverPrice()) {
					txn.begin();
					inv.setSilverunits(sunits + inv.getSilverunits());
					AccountBean acc = inv.getAccount();

					BigDecimal amount = inv.getAccount().getBalance()
							.subtract(new BigDecimal(sunits * viewSilverPrice()));
					acc.setBalance(amount);
					Set<TransactionBean> tBeans = inv.getAccount().getTransaction();
					InvestmentTransaction tBean = new InvestmentTransaction();
					// tBean.setTransactionId((int) Math.round(Math.random() * 10000));
					tBean.setTransactionAmount(new BigDecimal(sunits * viewSilverPrice()));
					tBean.setTransactionDate(LocalDateTime.now());
					tBean.setAccount(inv.getAccount());
					tBean.setTransactionDescription("Silver is bought");
					tBean.setTransactionMode(TransactionMode.ONLINE);
					tBean.setTransactionType(TransactionType.DEBIT);
					tBean.setUnits(sunits);
					tBean.setPricePerUnit(viewSilverPrice());
					tBeans.add(tBean);
					txn.commit();
				} else {
					log.error(ErrorMessages.INSUFF_BALANCE_MESSAGE);
					throw new IBSException(ErrorMessages.INSUFF_BALANCE_MESSAGE);
				}

			}
			log.info("Service buys Silver");
		} else {
			log.error("Invalid units");
			throw new IBSException(ErrorMessages.INVALID_UNITS_MESSAGE);

		}
	}

	@Override
	public void sellSilver(double sunits, String userId) throws IBSException {
		EntityTransaction txn = JPAUtil.getTransaction();

		BigInteger uci = cs.getUciByUserId(userId);

		InvestmentBean inv = invdao.getInvestmentByUci(uci);
		if (sunits > 0) {
			if (inv != null) {
				Double units = inv.getSilverunits();
				if (units > sunits) {
					txn.begin();
					inv.setSilverunits(inv.getSilverunits() - sunits);
					AccountBean acc = inv.getAccount();
					BigDecimal amount = inv.getAccount().getBalance().add(new BigDecimal(sunits * viewSilverPrice()));
					acc.setBalance(amount);
					Set<TransactionBean> tBeans = inv.getAccount().getTransaction();
					InvestmentTransaction tBean = new InvestmentTransaction();
					// tBean.setTransactionId((int) Math.round(Math.random() * 10000));
					tBean.setTransactionAmount(new BigDecimal(sunits * viewSilverPrice()));
					tBean.setTransactionDate(LocalDateTime.now());
					tBean.setAccount(inv.getAccount());
					tBean.setTransactionDescription("silver is sold");
					tBean.setTransactionMode(TransactionMode.ONLINE);
					tBean.setTransactionType(TransactionType.CREDIT);
					tBean.setUnits(sunits);
					tBean.setPricePerUnit(viewSilverPrice());
					tBeans.add(tBean);
					txn.commit();
				} else {
					log.error(ErrorMessages.INSUFF_SILVER_UNITS);
					throw new IBSException(ErrorMessages.INSUFF_SILVER_UNITS);
				}

			}
			log.info("Service sells Silver");
		} else {
			log.error(ErrorMessages.INVALID_UNITS_MESSAGE);
			throw new IBSException(ErrorMessages.INVALID_UNITS_MESSAGE);
		}
	}

	@Override
	public void investDirMF(double mfAmount, String userId, Integer mfPlanId) throws IBSException {
		EntityTransaction txn = JPAUtil.getTransaction();

		BigInteger uci = cs.getUciByUserId(userId);
		if (mfAmount >= viewMFPlans().get(mfPlanId).getMinAmtDir()) {
			if (viewMFPlans().containsKey(mfPlanId)) {

				InvestmentBean inv = invdao.getInvestmentByUci(uci);
				Double balance = Double.parseDouble(inv.getAccount().getBalance().toString());

				if (balance >= mfAmount) {
					txn.begin();
					MutualFund mutualFund = new MutualFund();
					mutualFund.setBankMutualFund(viewMFPlans().get(mfPlanId));
					mutualFund.setOpeningDate(LocalDate.now());
					mutualFund.setMfAmount(mfAmount);
					mutualFund.setFolioNumber((int) Math.floor(Math.random() * 10000));
					Double mfUnits = mfAmount / (viewMFPlans().get(mfPlanId).getNav());
					mutualFund.setMfUnits(mfUnits);
					mutualFund.setStatus(true);
					mutualFund.setType(MFType.DIRECT);
					BigDecimal amount = inv.getAccount().getBalance().subtract(new BigDecimal(mfAmount));
					inv.getAccount().setBalance(amount);
					inv.getFunds().add(mutualFund);

					Set<TransactionBean> tBeans = inv.getAccount().getTransaction();
					InvestmentTransaction tBean = new InvestmentTransaction();
					// tBean.setTransactionId((int) Math.round(Math.random() * 10000));
					tBean.setTransactionAmount(new BigDecimal(mfAmount));
					tBean.setTransactionDate(LocalDateTime.now());
					tBean.setAccount(inv.getAccount());
					tBean.setTransactionDescription("invested in mutualfunds");
					tBean.setTransactionMode(TransactionMode.ONLINE);
					tBean.setTransactionType(TransactionType.DEBIT);
					tBean.setUnits(mfUnits);
					tBean.setPricePerUnit(viewMFPlans().get(mfPlanId).getNav());
					tBeans.add(tBean);

					txn.commit();
					log.info("Investment in MF successful");

				} else {
					log.error(ErrorMessages.INSUFF_BALANCE_MESSAGE);
					throw new IBSException(ErrorMessages.INSUFF_BALANCE_MESSAGE);
				}
			} else {
				log.error(ErrorMessages.INVALID_DETAILS_MESSAGE);
				throw new IBSException(ErrorMessages.INVALID_DETAILS_MESSAGE);
			}
		} else {
			log.error(ErrorMessages.INVALID_AMOUNT_MESSAGE);
			throw new IBSException(ErrorMessages.INVALID_MIN_AMOUNT);
		}

	}

	public Boolean autoSip(String userId, Integer folio) throws IBSException {
		EntityTransaction txn = JPAUtil.getTransaction();
		Boolean status = false;
		LocalDate todayDate = LocalDate.now();
		BigInteger uci = cs.getUciByUserId(userId);
		InvestmentBean inv = invdao.getInvestmentByUci(uci);
		
		Set<MutualFund> mfSet = inv.getFunds();
		MutualFund corrMf = new MutualFund();

		for (MutualFund mutualFund : mfSet) {
			mutualFund.getOpeningDate();
			Frequency freq = mutualFund.getFrequency();
			LocalDate dtOld=mutualFund.getLastInstDate();
			switch (freq) {
			case DAILY:
				
				if(todayDate==mutualFund.getLastInstDate().plusDays(1) || todayDate==mutualFund.getOpeningDate()) {
				txn.begin();
				Double amt=mutualFund.getMfAmount();
				BigDecimal newBal=inv.getAccount().getBalance().subtract(BigDecimal.valueOf(amt));
				inv.getAccount().setBalance(newBal);
				Double newUnits=amt/viewMFPlans().get(mutualFund.getBankMutualFund().getMfPlanId()).getNav();
				mutualFund.setMfUnits(mutualFund.getMfUnits()+newUnits);
				mutualFund.setLastInstDate(todayDate);
				
				Set<TransactionBean> tBeans = inv.getAccount().getTransaction();
				InvestmentTransaction tBean = new InvestmentTransaction();
				// tBean.setTransactionId((int) Math.round(Math.random() * 10000));
				tBean.setTransactionAmount(new BigDecimal(amt));
				tBean.setTransactionDate(LocalDateTime.now());
				tBean.setAccount(inv.getAccount());
				tBean.setTransactionDescription("invested in mutualfunds sip");
				tBean.setTransactionMode(TransactionMode.ONLINE);
				tBean.setTransactionType(TransactionType.DEBIT);
				tBean.setUnits(newUnits);
				tBean.setPricePerUnit(viewMFPlans().get(mutualFund.getBankMutualFund().getMfPlanId()).getNav());
				tBeans.add(tBean);
				txn.commit();
				}
				break;
			case MONTHLY:
				
				if(todayDate==mutualFund.getLastInstDate().plusMonths(1) || todayDate==mutualFund.getOpeningDate()) {
				txn.begin();
				Double amt=mutualFund.getMfAmount();
				BigDecimal newBal=inv.getAccount().getBalance().subtract(BigDecimal.valueOf(amt));
				inv.getAccount().setBalance(newBal);
				Double newUnits=amt/viewMFPlans().get(mutualFund.getBankMutualFund().getMfPlanId()).getNav();
				mutualFund.setMfUnits(mutualFund.getMfUnits()+newUnits);
				mutualFund.setLastInstDate(todayDate);
				
				Set<TransactionBean> tBeans = inv.getAccount().getTransaction();
				InvestmentTransaction tBean = new InvestmentTransaction();
				// tBean.setTransactionId((int) Math.round(Math.random() * 10000));
				tBean.setTransactionAmount(new BigDecimal(amt));
				tBean.setTransactionDate(LocalDateTime.now());
				tBean.setAccount(inv.getAccount());
				tBean.setTransactionDescription("invested in mutualfunds sip");
				tBean.setTransactionMode(TransactionMode.ONLINE);
				tBean.setTransactionType(TransactionType.DEBIT);
				tBean.setUnits(newUnits);
				tBean.setPricePerUnit(viewMFPlans().get(mutualFund.getBankMutualFund().getMfPlanId()).getNav());
				tBeans.add(tBean);
				txn.commit();
				}
				break;

			case HALFYEARLY:
				if(todayDate==mutualFund.getLastInstDate().plusMonths(6) || todayDate==mutualFund.getOpeningDate()) {
					txn.begin();
					Double amt=mutualFund.getMfAmount();
					BigDecimal newBal=inv.getAccount().getBalance().subtract(BigDecimal.valueOf(amt));
					inv.getAccount().setBalance(newBal);
					Double newUnits=amt/viewMFPlans().get(mutualFund.getBankMutualFund().getMfPlanId()).getNav();
					mutualFund.setMfUnits(mutualFund.getMfUnits()+newUnits);
					mutualFund.setLastInstDate(todayDate);
					
					Set<TransactionBean> tBeans = inv.getAccount().getTransaction();
					InvestmentTransaction tBean = new InvestmentTransaction();
					// tBean.setTransactionId((int) Math.round(Math.random() * 10000));
					tBean.setTransactionAmount(new BigDecimal(amt));
					tBean.setTransactionDate(LocalDateTime.now());
					tBean.setAccount(inv.getAccount());
					tBean.setTransactionDescription("invested in mutualfunds sip");
					tBean.setTransactionMode(TransactionMode.ONLINE);
					tBean.setTransactionType(TransactionType.DEBIT);
					tBean.setUnits(newUnits);
					tBean.setPricePerUnit(viewMFPlans().get(mutualFund.getBankMutualFund().getMfPlanId()).getNav());
					tBeans.add(tBean);
					txn.commit();
					}
				break;

			case QUATERLY:
				if(todayDate==mutualFund.getLastInstDate().plusMonths(3) || todayDate==mutualFund.getOpeningDate()) {
					txn.begin();
					Double amt=mutualFund.getMfAmount();
					BigDecimal newBal=inv.getAccount().getBalance().subtract(BigDecimal.valueOf(amt));
					inv.getAccount().setBalance(newBal);
					Double newUnits=amt/viewMFPlans().get(mutualFund.getBankMutualFund().getMfPlanId()).getNav();
					mutualFund.setMfUnits(mutualFund.getMfUnits()+newUnits);
					mutualFund.setLastInstDate(todayDate);
					
					Set<TransactionBean> tBeans = inv.getAccount().getTransaction();
					InvestmentTransaction tBean = new InvestmentTransaction();
					// tBean.setTransactionId((int) Math.round(Math.random() * 10000));
					tBean.setTransactionAmount(new BigDecimal(amt));
					tBean.setTransactionDate(LocalDateTime.now());
					tBean.setAccount(inv.getAccount());
					tBean.setTransactionDescription("invested in mutualfunds sip");
					tBean.setTransactionMode(TransactionMode.ONLINE);
					tBean.setTransactionType(TransactionType.DEBIT);
					tBean.setUnits(newUnits);
					tBean.setPricePerUnit(viewMFPlans().get(mutualFund.getBankMutualFund().getMfPlanId()).getNav());
					tBeans.add(tBean);
					txn.commit();
					}
				break;

			case ANNUALLY:
				if(todayDate==mutualFund.getLastInstDate().plusYears(1) || todayDate==mutualFund.getOpeningDate()) {
					txn.begin();
					Double amt=mutualFund.getMfAmount();
					BigDecimal newBal=inv.getAccount().getBalance().subtract(BigDecimal.valueOf(amt));
					inv.getAccount().setBalance(newBal);
					Double newUnits=amt/viewMFPlans().get(mutualFund.getBankMutualFund().getMfPlanId()).getNav();
					mutualFund.setMfUnits(mutualFund.getMfUnits()+newUnits);
					mutualFund.setLastInstDate(todayDate);
					
					Set<TransactionBean> tBeans = inv.getAccount().getTransaction();
					InvestmentTransaction tBean = new InvestmentTransaction();
					// tBean.setTransactionId((int) Math.round(Math.random() * 10000));
					tBean.setTransactionAmount(new BigDecimal(amt));
					tBean.setTransactionDate(LocalDateTime.now());
					tBean.setAccount(inv.getAccount());
					tBean.setTransactionDescription("invested in mutualfunds sip");
					tBean.setTransactionMode(TransactionMode.ONLINE);
					tBean.setTransactionType(TransactionType.DEBIT);
					tBean.setUnits(newUnits);
					tBean.setPricePerUnit(viewMFPlans().get(mutualFund.getBankMutualFund().getMfPlanId()).getNav());
					tBeans.add(tBean);
					txn.commit();
					}
				break;

			default:
				break;
			}

		}

		return status;
	}

	@Override
	public void investSipMF(String userId, MutualFund mutualFund) throws IBSException {
		EntityTransaction txn = JPAUtil.getTransaction();

		BigInteger uci = cs.getUciByUserId(userId);

		InvestmentBean inv = invdao.getInvestmentByUci(uci);
		Double balance = Double.parseDouble(inv.getAccount().getBalance().toString());

		if (balance >= mutualFund.getMfAmount()) {
			txn.begin();
			// mutualFund.setBankMutualFund(viewMFPlans().get(mfPlanId));
			mutualFund.setBuyDate(LocalDate.now());
			// mutualFund.setMfAmount(mfAmount);
			mutualFund.setFolioNumber((int) Math.floor(Math.random() * 10000));
			Double mfUnits = mutualFund.getMfAmount() / mutualFund.getBankMutualFund().getNav();
			mutualFund.setMfUnits(mfUnits);
			mutualFund.setInstallments(mutualFund.getDuration());
			mutualFund.setStatus(true);
			mutualFund.setType(MFType.SIP);
			// BigDecimal amount = inv.getAccount().getBalance().subtract(new
			// BigDecimal(mutualFund.getMfAmount()));
			// inv.getAccount().setBalance(amount);
			inv.getFunds().add(mutualFund);

			Set<TransactionBean> tBeans = inv.getAccount().getTransaction();
			InvestmentTransaction tBean = new InvestmentTransaction();
			// tBean.setTransactionId((int) Math.round(Math.random() * 10000));
			tBean.setTransactionAmount(new BigDecimal(mutualFund.getMfAmount()));
			tBean.setTransactionDate(LocalDateTime.now());
			tBean.setAccount(inv.getAccount());
			tBean.setTransactionDescription("invested in mutualfunds");
			tBean.setTransactionMode(TransactionMode.ONLINE);
			tBean.setTransactionType(TransactionType.DEBIT);
			tBean.setUnits(mfUnits);
			tBean.setPricePerUnit(mutualFund.getBankMutualFund().getNav());
			tBeans.add(tBean);

			txn.commit();
			log.info("Investment in MF successful");

		} else {
			log.error(ErrorMessages.INSUFF_BALANCE_MESSAGE);
			throw new IBSException(ErrorMessages.INSUFF_BALANCE_MESSAGE);
		}

	}

	@Override
	public void withdrawDirMF(String userId, MutualFund mutualFund) throws IBSException {
		EntityTransaction txn = JPAUtil.getTransaction();

		BigInteger uci = cs.getUciByUserId(userId);

		InvestmentBean inv = invdao.getInvestmentByUci(uci);
		if (inv != null) {
			txn.begin();

			MutualFund mf = mfDao.getMutualFundById(mutualFund.getFolioNumber());
			mf.setClosingDate(LocalDate.now());
			mf.setStatus(false);
			Double mfAmount = mutualFund.getMfUnits()
					* viewMFPlans().get(mutualFund.getBankMutualFund().getMfPlanId()).getNav();
			BigDecimal amount = inv.getAccount().getBalance().add(new BigDecimal(mfAmount));
			inv.getAccount().setBalance(amount);
			Set<TransactionBean> tBeans = inv.getAccount().getTransaction();
			InvestmentTransaction tBean = new InvestmentTransaction();
			// tBean.setTransactionId((int) Math.round(Math.random() * 10000));
			tBean.setTransactionAmount(new BigDecimal(mfAmount));
			tBean.setTransactionDate(LocalDateTime.now());
			tBean.setAccount(inv.getAccount());
			tBean.setTransactionDescription("mfPlan is withdrawn");
			tBean.setTransactionMode(TransactionMode.ONLINE);
			tBean.setTransactionType(TransactionType.CREDIT);
			tBean.setUnits(mutualFund.getMfUnits());
			tBean.setPricePerUnit(mf.getBankMutualFund().getNav());
			tBeans.add(tBean);
			txn.commit();
			log.info("MF withdrawn successfully");

		} else {
			log.error(ErrorMessages.INVALID_DETAILS_MESSAGE);
			throw new IBSException(ErrorMessages.INVALID_DETAILS_MESSAGE);
		}

	}

	@Override
	public void withdrawSipMF(String userId, MutualFund mutualFund) throws IBSException {
		EntityTransaction txn = JPAUtil.getTransaction();

		BigInteger uci = cs.getUciByUserId(userId);

		InvestmentBean inv = invdao.getInvestmentByUci(uci);
		if (inv != null) {

			MutualFund mf = mfDao.getMutualFundById(mutualFund.getFolioNumber());
			if (mf.getInstallments() == 0) {
				txn.begin();
				mf.setClosingDate(LocalDate.now());
				mf.setStatus(false);
				Double mfAmount = mutualFund.getMfUnits()
						* viewMFPlans().get(mutualFund.getBankMutualFund().getMfPlanId()).getNav();
				BigDecimal amount = inv.getAccount().getBalance().add(new BigDecimal(mfAmount));
				inv.getAccount().setBalance(amount);
				Set<TransactionBean> tBeans = inv.getAccount().getTransaction();
				InvestmentTransaction tBean = new InvestmentTransaction();
				// tBean.setTransactionId((int) Math.round(Math.random() * 10000));
				tBean.setTransactionAmount(new BigDecimal(mfAmount));
				tBean.setTransactionDate(LocalDateTime.now());
				tBean.setAccount(inv.getAccount());
				tBean.setTransactionDescription("mfPlan is withdrawn");
				tBean.setTransactionMode(TransactionMode.ONLINE);
				tBean.setTransactionType(TransactionType.CREDIT);
				tBean.setUnits(mutualFund.getMfUnits());
				tBean.setPricePerUnit(mf.getBankMutualFund().getNav());
				tBeans.add(tBean);
				txn.commit();
				log.info("MF withdrawn successfully");
			} else {
				// Premature Sip Withdraw
			}

		} else {
			log.error(ErrorMessages.INVALID_DETAILS_MESSAGE);
			throw new IBSException(ErrorMessages.INVALID_DETAILS_MESSAGE);
		}

	}

	@Override
	public InvestmentBean viewInvestments(String userId) throws IBSException {

		EntityTransaction txn = JPAUtil.getTransaction();

		BigInteger uci = cs.getUciByUserId(userId);

		InvestmentBean inv = invdao.getInvestmentByUci(uci);
		txn.begin();
		for (MutualFund mf : inv.getFunds()) {
			mf.setMfAmount(mf.getMfUnits() * viewMFPlans().get(mf.getBankMutualFund().getMfPlanId()).getNav());
		}
		txn.commit();
		log.info("Service calls for viewing investments");
		return inv;
	}

	@Override
	public boolean validateCustomer(String userId, String password) {

		BigInteger uci = cs.getUciByUserId(userId);
		CustomerBean customer = new CustomerBean();
		if (uci != null) {
			customer = cs.getCustByUci(uci);

			if (customer != null && userId.equals(customer.getUserId())) {

				String correctPassword = customer.getPassword();
				if (password.equals(correctPassword)) {
					log.info("Customer Validated Successfully");
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public List<InvestmentTransaction> getTransactions(String userId) throws IBSException {

		BigInteger uci = cs.getUciByUserId(userId);

		InvestmentBean inv = invdao.getInvestmentByUci(uci);

		List<InvestmentTransaction> invtrnList = trxDao.getTransactionByAcc(inv.getAccount().getAccNo());

		return invtrnList;

	}

	@Override
	public ArrayList<AccountBean> getAccountList(String userId) throws IBSException {

		BigInteger uci = cs.getUciByUserId(userId);
		ArrayList<AccountBean> list = new ArrayList<>();
		list = (ArrayList<AccountBean>) accDao.getAccByUci(uci);
		return list;
	}

	@Override
	public void linkMyAccount(BigInteger accountNumber, String userId) throws IBSException {
		EntityTransaction txn = JPAUtil.getTransaction();

		BigInteger uci = cs.getUciByUserId(userId);

		txn.begin();
		InvestmentBean inv = invdao.getInvestmentByUci(uci);

		AccountBean acc = accDao.getAccountByAccNo(accountNumber);
		inv.setAccount(acc);
		txn.commit();

	}

	@Override
	public List<InvestmentTransaction> getPeriodicStmt(LocalDateTime startDate, LocalDateTime endDate, BigInteger accNo)
			throws IBSException {
		List<InvestmentTransaction> transList = null;
		LocalDate startDate1 = startDate.toLocalDate();
		LocalDate endDate1 = endDate.toLocalDate();

		// Period period= Period.between(startDate1, endDate1);
		// int months = ((int) period.getDays()) / 30;
		long noOfDaysBetween = ChronoUnit.DAYS.between(startDate1, endDate1);
		int months = (int) (noOfDaysBetween / 30);
		// System.out.println(noOfDaysBetween);
		if (startDate.compareTo(endDate) < 0 && months <= 6 && startDate.compareTo(LocalDateTime.now()) < 0) {
			transList = trxDao.getPeriodicTransactions(startDate, endDate, accNo);
			// logger.info(" Periodic statement fetched");
		} else {
			throw new IBSException(ErrorMessages.INVALID_PERIOD);
		}
		return transList;
	}

}
