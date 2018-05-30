package com.anbang.qipai.members.cqrs.c.domain;

import com.dml.accounting.AccountingRecord;

public class CreateMemberResult {

	private String memberId;
	private String goldAccountId;
	private AccountingRecord accountingRecordForGiveGold;

	public CreateMemberResult(String memberId, String goldAccountId, AccountingRecord accountingRecordForGiveGold) {
		this.memberId = memberId;
		this.goldAccountId = goldAccountId;
		this.accountingRecordForGiveGold = accountingRecordForGiveGold;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getGoldAccountId() {
		return goldAccountId;
	}

	public void setGoldAccountId(String goldAccountId) {
		this.goldAccountId = goldAccountId;
	}

	public AccountingRecord getAccountingRecordForGiveGold() {
		return accountingRecordForGiveGold;
	}

	public void setAccountingRecordForGiveGold(AccountingRecord accountingRecordForGiveGold) {
		this.accountingRecordForGiveGold = accountingRecordForGiveGold;
	}

}
