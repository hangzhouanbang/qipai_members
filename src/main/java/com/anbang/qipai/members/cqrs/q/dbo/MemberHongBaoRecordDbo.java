package com.anbang.qipai.members.cqrs.q.dbo;

import com.dml.accounting.AccountingSummary;

public class MemberHongBaoRecordDbo {
    private String id;
    private String accountId;
    private String memberId;
    private double accountingAmount;
    private long accountingNo;
    private double balanceAfter;
    private AccountingSummary summary;
    private long time;

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

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public double getAccountingAmount() {
        return accountingAmount;
    }

    public void setAccountingAmount(double accountingAmount) {
        this.accountingAmount = accountingAmount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public AccountingSummary getSummary() {
        return summary;
    }

    public void setSummary(AccountingSummary summary) {
        this.summary = summary;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getAccountingNo() {
        return accountingNo;
    }

    public void setAccountingNo(long accountingNo) {
        this.accountingNo = accountingNo;
    }
}
