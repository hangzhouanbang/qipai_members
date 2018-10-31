package com.anbang.qipai.members.cqrs.c.domain.prize;

import com.dml.accounting.AccountingRecord;

public class ExchangeRecord {
    private AccountingRecord record;
    private int concurrency;

    public ExchangeRecord(AccountingRecord record, int concurrency) {
        this.record = record;
        this.concurrency = concurrency;
    }

    public AccountingRecord getRecord() {
        return record;
    }

    public int getConcurrency() {
        return concurrency;
    }

}
