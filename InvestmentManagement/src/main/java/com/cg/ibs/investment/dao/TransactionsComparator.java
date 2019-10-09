package com.cg.ibs.investment.dao;

import java.util.Comparator;

import com.cg.ibs.common.bean.TransactionBean;

public class TransactionsComparator implements Comparator<TransactionBean> {
	@Override
	public int compare(TransactionBean t1, TransactionBean t2) {

		return t1.getTransactionId().compareTo(t2.getTransactionId());
	}
}
