package com.cg.ibs.investment.bean;

import java.time.LocalDate;

public class MutualFund {
	private int mfid;				//Declaring Id of the Mutual fund
	private String title;			//Declaring Title of the Mutual fund
	private double nav;				//Declaring NAV of the Mutual fund
	private double mfUnits;			//Declaring units of the Mutual fund
	private double mfAmount;		//Declaring Amount of the Mutual fund
	private LocalDate openingDate;	//Opening Date of the Mutual fund
	private LocalDate closingDate;	//Closing Date of the Mutual fund
	private boolean status;

	public MutualFund() {

	}

	public MutualFund(int mfid, String title, double nav, double mfUnits, LocalDate openingDate, LocalDate closingDate,
			boolean status) {
		super();
		this.mfid = mfid;
		this.title = title;
		this.nav = nav;
		this.mfUnits = mfUnits;
		this.openingDate = openingDate;
		this.closingDate = closingDate;
		this.status = status;
	}

	@Override
	public String toString() {
		return "mutualFundId=" + mfid + ", title=" + title + ", nav=" + nav + ", mfUnits=" + mfUnits + ", openingDate="
				+ openingDate + ", closingDate=" + closingDate + "";
	}

	public int getmfid() {
		return mfid;
	}

	public void setmfid(int mfid) {
		this.mfid = mfid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getNav() {
		return nav;
	}

	public void setNav(double nav) {
		this.nav = nav;
	}

	public double getMfUnits() {
		return mfUnits;
	}

	public void setMfUnits(double mfUnits) {
		this.mfUnits = mfUnits;
	}

	public double getMfAmount() {
		return mfAmount = nav * mfUnits;
	}

	public void setMfAmount(double mfAmount) {
		this.mfAmount = mfAmount;
	}

	public LocalDate getOpeningDate() {
		return openingDate;
	}

	public void setOpeningDate(LocalDate openingDate) {
		this.openingDate = openingDate;
	}

	public LocalDate getClosingDate() {
		return closingDate;
	}

	public void setClosingDate(LocalDate closingDate) {
		this.closingDate = closingDate;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}