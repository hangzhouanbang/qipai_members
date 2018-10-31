package com.anbang.qipai.members.cqrs.q.dbo;

import com.anbang.qipai.members.enums.ExchangeType;
import com.dml.accounting.AccountingRecord;

public class ScoreExchangeRecordDbo {

    private String id;
    private String memberId;
    private String phone;
    private long time;
    /**
     * 兑换的积分数量
     */
    private int score;
    /**
     * 兑换的话费或红包数量
     */
    private int currency;
    private ExchangeType exchangeType;

    private int rest;

    private AccountingRecord accountingRecord;

    public ScoreExchangeRecordDbo() {
    }

    public ScoreExchangeRecordDbo(String id, String memberId, String phone, long time, int score, int currency, ExchangeType exchangeType, int rest, AccountingRecord accountingRecord) {
        this.id = id;
        this.memberId = memberId;
        this.phone = phone;
        this.time = time;
        this.score = score;
        this.currency = currency;
        this.exchangeType = exchangeType;
        this.rest = rest;
        this.accountingRecord = accountingRecord;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public ExchangeType getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(ExchangeType exchangeType) {
        this.exchangeType = exchangeType;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public AccountingRecord getAccountingRecord() {
        return accountingRecord;
    }

    public void setAccountingRecord(AccountingRecord accountingRecord) {
        this.accountingRecord = accountingRecord;
    }

    @Override
    public String toString() {
        return "ScoreExchangeRecordDbo{" +
                "id='" + id + '\'' +
                ", memberId='" + memberId + '\'' +
                ", phone='" + phone + '\'' +
                ", time=" + time +
                ", score=" + score +
                ", currency=" + currency +
                ", exchangeType=" + exchangeType +
                ", rest=" + rest +
                ", accountingRecord=" + accountingRecord +
                '}';
    }
}
