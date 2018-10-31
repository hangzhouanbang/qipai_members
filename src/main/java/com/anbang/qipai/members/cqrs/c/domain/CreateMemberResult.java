package com.anbang.qipai.members.cqrs.c.domain;

import com.dml.accounting.AccountingRecord;

public class CreateMemberResult {

	private String memberId;
	private String goldAccountId;
    private String scoreAccountId;
    private String hongBaoAccountId;
    private String phoneFeeAccountId;
	private AccountingRecord accountingRecordForGiveGold;
	private AccountingRecord accountingRecordForGiveScore;
	private AccountingRecord accountingRecordForHongBao;
	private AccountingRecord accountingRecordForPhoneFee;

    public CreateMemberResult(String memberId,
                              String goldAccountId,
                              String scoreAccountId,
                              String hongBaoAccountId,
                              String phoneFeeAccountId,
                              AccountingRecord accountingRecordForGiveGold,
                              AccountingRecord accountingRecordForGiveScore,
                              AccountingRecord accountingRecordForHongBao,
                              AccountingRecord accountingRecordForPhoneFee) {
        this.memberId = memberId;
        this.goldAccountId = goldAccountId;
        this.scoreAccountId = scoreAccountId;
        this.hongBaoAccountId = hongBaoAccountId;
        this.phoneFeeAccountId = phoneFeeAccountId;
        this.accountingRecordForGiveGold = accountingRecordForGiveGold;
        this.accountingRecordForGiveScore = accountingRecordForGiveScore;
        this.accountingRecordForHongBao = accountingRecordForHongBao;
        this.accountingRecordForPhoneFee = accountingRecordForPhoneFee;
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

    public AccountingRecord getAccountingRecordForHongBao() {
        return accountingRecordForHongBao;
    }

    public void setAccountingRecordForHongBao(AccountingRecord accountingRecordForHongBao) {
        this.accountingRecordForHongBao = accountingRecordForHongBao;
    }

    public AccountingRecord getAccountingRecordForPhoneFee() {
        return accountingRecordForPhoneFee;
    }

    public void setAccountingRecordForPhoneFee(AccountingRecord accountingRecordForPhoneFee) {
        this.accountingRecordForPhoneFee = accountingRecordForPhoneFee;
    }

    public String getHongBaoAccountId() {
        return hongBaoAccountId;
    }

    public void setHongBaoAccountId(String hongBaoAccountId) {
        this.hongBaoAccountId = hongBaoAccountId;
    }

    public String getPhoneFeeAccountId() {
        return phoneFeeAccountId;
    }

    public void setPhoneFeeAccountId(String phoneFeeAccountId) {
        this.phoneFeeAccountId = phoneFeeAccountId;
    }
}
