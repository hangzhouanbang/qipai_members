package com.anbang.qipai.members.cqrs.q.dbo;

import org.springframework.data.mongodb.core.mapping.Document;

import com.dml.accounting.AccountingSummary;

@Document(collection="membergoldrecorddbo")
public class MemberGoldRecordDbo {

	private String id;

	private String accountId;

	private long accountingNo;

	private int accountingAmount;

	private int balanceAfter;

	private AccountingSummary summary;
	
	private long accountingTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public long getAccountingNo() {
		return accountingNo;
	}

	public void setAccountingNo(long accountingNo) {
		this.accountingNo = accountingNo;
	}

	public int getAccountingAmount() {
		return accountingAmount;
	}

	public void setAccountingAmount(int accountingAmount) {
		this.accountingAmount = accountingAmount;
	}

	public int getBalanceAfter() {
		return balanceAfter;
	}

	public void setBalanceAfter(int balanceAfter) {
		this.balanceAfter = balanceAfter;
	}

	public long getAccountingTime() {
		return accountingTime;
	}

	public void setAccountingTime(long accountingTime) {
		this.accountingTime = accountingTime;
	}

	public AccountingSummary getSummary() {
		return summary;
	}

	public void setSummary(AccountingSummary summary) {
		this.summary = summary;
	}

}
