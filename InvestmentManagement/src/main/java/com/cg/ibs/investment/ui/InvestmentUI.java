package com.cg.ibs.investment.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

import com.cg.ibs.investment.bean.BankMutualFund;
import com.cg.ibs.investment.bean.MutualFund;
import com.cg.ibs.investment.exception.IBSException;
import com.cg.ibs.investment.service.BankService;
import com.cg.ibs.investment.service.BankServiceImpl;
import com.cg.ibs.investment.service.ClientService;
import com.cg.ibs.investment.service.ClientServiceImpl;

public class InvestmentUI {
	static Scanner sc;				
	static double goldUnits = 0;	
	static double silverUnits = 0;
	static int status = 3;
	
	//Declaring objects of Client Service and Bank Service
	ClientService service = new ClientServiceImpl();
	BankService bankservice = new BankServiceImpl();

	//Method to start the program
	public void doIt() {
		InvestmentUI investmentUI = new InvestmentUI();
		while (status == 3) {
			System.out.println("	Press 1 for customer and 2 for bank representative");
			System.out.println("----------------------------------------------------------");
			status = sc.nextInt();

			Menu choice = null;
			BankMenu option = null;
			ClientService service = new ClientServiceImpl();
			
			//Options for the Customer
			if (status == 1) {
				System.out.println("Enter the userId");
				String userId = sc.next();
				System.out.println("Enter the password");
				String password = sc.next();

				try {
					if (service.validateCustomer(userId, password)) {
						while (choice != Menu.QUIT) {
							System.out.println("Choice");
							System.out.println("--------------------");
							for (Menu menu : Menu.values()) {
								System.out.println(menu.ordinal() + "\t" + menu.toString().replace("_", " "));
							}

							int ordinal = sc.nextInt();
							if (ordinal >= 0 && ordinal < Menu.values().length) {
								choice = Menu.values()[ordinal];

								switch (choice) {
								case VIEW_MY_INVESTMENT:
									investmentUI.viewMyInvestments(userId);
									break;

								case VIEW_GOLD_PRICE:
									System.out.println(service.viewGoldPrice());

									break;
								case VIEW_SILVER_PRICE:
									System.out.println(service.viewSilverPrice());
									break;
								case VIEW_MF_PLANS:
									investmentUI.viewMFPlans();
									break;
								case BUY_GOLD:
									investmentUI.buyGold(userId);
									break;
								case SELL_GOLD:
									investmentUI.sellGold(userId);
									break;
								case BUY_SILVER:
									investmentUI.buySilver(userId);
									break;
								case SELL_SILVER:
									investmentUI.sellSilver(userId);
									break;
								case INVEST_MF_PLAN:
									investmentUI.investMFPlan(userId);
									break;
								case WITHDRAW_MF_PLAN:
									investmentUI.withdrawMFPlan(userId);
									break;
								case QUIT:

									System.out.println("You are successfully logged out");
									break;
								}
							} else {
								System.out.println("Invalid Option");

							}

						}
					}
					System.out.println("Press 3 to continue");
					status = sc.nextInt();
				} catch (IBSException e) {
					System.out.println(e.getMessage());
				} catch (Exception e) {
					System.out.println("An error occured");
				}
			} 
			//Options for the Bank Representative
			else if (status == 2) {
				System.out.println("Enter the userId");
				String userId = sc.next();
				System.out.println("Enter the password");
				String password = sc.next();

				BankService bankservice = new BankServiceImpl();
				try {
					if (bankservice.validateBank(userId, password)) {
						while (option != BankMenu.QUIT) {
							System.out.println("Choice");
							System.out.println("--------------------");
							for (BankMenu menu : BankMenu.values()) {
								System.out.println(menu.ordinal() + "\t" + menu.toString().replace("_", " "));
							}
							System.out.println("Choice");

							int ordinal = sc.nextInt();
							if (ordinal >= 0 && ordinal < BankMenu.values().length) {
								option = BankMenu.values()[ordinal];

								switch (option) {
								case UPDATE_GOLD_PRICE:
									investmentUI.updateGoldPrice();
									break;
								case UPDATE_SILVER_PRICE:
									investmentUI.updateSiverPrice();
									break;
								case ADD_MUTUALFUND_PLAN:
									investmentUI.addMFPlans();
									break;

								case QUIT:
									System.out.println("You are successfully logged out");

									break;

								}
							}
						}
					}
					System.out.println("Press 3 to continue");
					status = sc.nextInt();
				} catch (IBSException e) {
					System.out.println(e.getMessage());
				} catch (Exception e) {
					System.out.println("An error occurred");
				}
			} else {
				System.out.println("Please enter valid choice");
				System.out.println("Press 3 to continue");
				status = sc.nextInt();
			}
		}
	}

	//Customer views his/her Investments
	public void viewMyInvestments(String userId) {
		try {

			System.out.println("UCI" + " " + service.viewInvestments(userId).getUCI());
			System.out.println("GoldUnits" + " " + service.viewInvestments(userId).getGoldunits());
			System.out.println("SilverUnits" + " " + service.viewInvestments(userId).getSilverunits());
			System.out.println("Balance" + " " + service.viewInvestments(userId).getBalance());
			List<MutualFund> InvstmntList = new ArrayList<>(service.viewInvestments(userId).getFunds());

			for (int i = 0; i < InvstmntList.size(); i++) {
				System.out.println(i + " -----  " + InvstmntList.get(i));

			}
		} catch (IBSException exp) {
			System.out.println(exp.getMessage());
		}
	}

	//Customer sells his/her gold
	public void sellGold(String userId) {
		System.out.println("Enter number of gold units to sell(in grams):");
		goldUnits = sc.nextDouble();

		try {
			service.sellGold(goldUnits, userId);
			System.out.println("transaction completed");
		} catch (IBSException e) {
			System.out.println(e.getMessage());
		}
	}

	//Customer buys gold
	public void buyGold(String userId) {
		System.out.println("Enter number of gold units to buy(in grams):");
		try {

			double goldUnits = sc.nextDouble();

			service.buyGold(goldUnits, userId);
			System.out.println("transaction completed");
		} catch (IBSException e) {
			System.out.println(e.getMessage());
		}
	}

	//Customer sells his/her Silver
	public void sellSilver(String userId) {
		System.out.println("Enter number of silver units to sell(in grams):");
		silverUnits = sc.nextDouble();
		try {
			service.sellSilver(silverUnits, userId);
			System.out.println("transaction completed");
		} catch (IBSException e) {
			System.out.println(e.getMessage());
		}
	}
	
	//Customer views available Mutual Fund plans
	public void viewMFPlans(){
		System.out.println("ID" + "\t\t" + "Title" + "\t\t\t" + "NAV");
		for (Entry<Integer, BankMutualFund> entry : service.viewMFPlans().entrySet()) {
			System.out.println(entry.getValue().getmfid() + "\t\t" + entry.getValue().getTitle() + "\t\t"
					+ entry.getValue().getNav());
		}
		
	}
	
	//Customer buys Silver
	public void buySilver(String userId) {
		System.out.println("Enter number of silver units to buy(in grams):");
		silverUnits = sc.nextDouble();

		try {
			service.buySilver(silverUnits, userId);
			System.out.println("transaction completed");
		} catch (IBSException e) {
			System.out.println(e.getMessage());
		}
	}

	//Customer invests in a Mutual Fund
	public void investMFPlan(String userId) {
		System.out.println("ID" + "\t\t" + "Title" + "\t\t\t" + "NAV");
		for (Entry<Integer, BankMutualFund> entry : service.viewMFPlans().entrySet()) {
			System.out.println(entry.getValue().getmfid() + "\t\t" + entry.getValue().getTitle() + "\t\t"
					+ entry.getValue().getNav());

		}

		System.out.println("Enter the mutual fund Id:");
		int mfId = sc.nextInt();
		System.out.println("Enter the amount to invest");
		double mfAmount = sc.nextDouble();
		try {
			service.investMF(mfAmount, userId, mfId);
			System.out.println("transaction completed");
		} catch (IBSException e) {
			System.out.println(e.getMessage());
		}
	}

	//Customer withdraws from a Mutual Fund
	public void withdrawMFPlan(String userId) {
		List<MutualFund> InvestmentList;
		try {
			InvestmentList = new ArrayList<>(service.viewInvestments(userId).getFunds());
			for (int i = 0; i < InvestmentList.size(); i++) {
				System.out.println(i + " -----  " + InvestmentList.get(i));
			}
			System.out.println("Enter the plan number you want to choose");
			int temp = sc.nextInt();
			MutualFund mutualFund = InvestmentList.get(temp);
			service.withdrawMF(userId, mutualFund);
		} catch (IBSException exp) {
			System.out.println(exp.getMessage());
		}
	}

	//Bank representative updates gold price
	public void updateGoldPrice() {
		System.out.println("Enter the updated gold price");
		double goldPrice = sc.nextDouble();
		try {
			bankservice.updateGoldPrice(goldPrice);
			System.out.println("Gold Price updated successfully");
		} catch (IBSException e) {
			System.out.println(e.getMessage());
		}
	}

	//Bank representative updates Silver price
	public void updateSiverPrice() {
		System.out.println("Enter the updated silver price");
		double silverPrice = sc.nextDouble();
		try {
			bankservice.updateSilverPrice(silverPrice);
			System.out.println("Silver Price updated successfully");
		} catch (IBSException e) {
			System.out.println(e.getMessage());
		}
	}

	//Bank representative adds Mutual Fund plans
	public void addMFPlans() {
		System.out.println("Enter mutualfundId");
		int mfId = sc.nextInt();
		System.out.println("Enter mutualfundtitle");
		String title = sc.next();

		System.out.println("Enter nav value");
		double nav = sc.nextDouble();
		try {
			bankservice.addMF(new BankMutualFund(mfId, title, nav));
			System.out.println("Mutual Fund plans updated successfully");
		} catch (IBSException e) {
			System.out.println(e.getMessage());
		}

	}

	//Main Method
	public static void main(String[] args) {
		sc = new Scanner(System.in);
		InvestmentUI investmentUI = new InvestmentUI();
		investmentUI.doIt();
		System.out.println("The program has ended");
		sc.close();

	}
}