package com.anbang.qipai.members.cqrs.c.domain;

import com.dml.accounting.AccountingRecord;

public class CreateMemberResult {

	private String memberId;
	private String goldAccountId;
	private AccountingRecord accountingRecordForGiveGold;
	private AccountingRecord accountingRecordForGiveScore;
	private String scoreAccountId;

	public CreateMemberResult(String memberId, String goldAccountId, AccountingRecord accountingRecordForGiveGold,
			String scoreAccountId) {
		this.memberId = memberId;
		this.goldAccountId = goldAccountId;
		this.accountingRecordForGiveGold = accountingRecordForGiveGold;
		this.scoreAccountId = scoreAccountId;
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

	public AccountingRecord getAccountingRecordForGiveScore() {
		return accountingRecordForGiveScore;
	}

	public void setAccountingRecordForGiveScore(AccountingRecord accountingRecordForGiveScore) {
		this.accountingRecordForGiveScore = accountingRecordForGiveScore;
	}

	public String getScoreAccountId() {
		return scoreAccountId;
	}

	public void setScoreAccountId(String scoreAccountId) {
		this.scoreAccountId = scoreAccountId;
	}

}
