package com.anbang.qipai.members.cqrs.q.service;

import com.anbang.qipai.members.cqrs.c.domain.CreateMemberResult;
import com.anbang.qipai.members.cqrs.q.dao.MemberPhoneFeeAccountDao;
import com.anbang.qipai.members.cqrs.q.dao.MemberPhoneFeeRecordDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.PhoneFeeAccountDbo;
import com.anbang.qipai.members.cqrs.q.dbo.PhoneFeeRecordDbo;
import com.dml.accounting.AccountingRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PhoneFeeQueryService {

    @Autowired
    private MemberPhoneFeeAccountDao memberPhoneFeeAccountDao;

    @Autowired
    private MemberPhoneFeeRecordDboDao memberPhoneFeeRecordDboDao;

    public PhoneFeeAccountDbo createAccount(CreateMemberResult createMemberResult) {
        PhoneFeeAccountDbo account = new PhoneFeeAccountDbo();
        account.setId(createMemberResult.getPhoneFeeAccountId());
        account.setMemberId(createMemberResult.getMemberId());
        memberPhoneFeeAccountDao.save(account);
        return account;
    }

    public PhoneFeeRecordDbo withdraw(String memberId, AccountingRecord accountingRecord) {
        PhoneFeeRecordDbo dbo = new PhoneFeeRecordDbo();
        dbo.setMemberId(memberId);
        dbo.setAccountId(accountingRecord.getAccountId());
        dbo.setAccountingAmount(accountingRecord.getAccountingAmount());
        dbo.setAccountingNo(accountingRecord.getAccountingNo());
        dbo.setBalanceAfter(accountingRecord.getBalanceAfter());
        dbo.setSummary(accountingRecord.getSummary());
        dbo.setTime(accountingRecord.getAccountingTime());
        this.memberPhoneFeeRecordDboDao.save(dbo);

        PhoneFeeAccountDbo phoneFeeAccountDbo = memberPhoneFeeAccountDao.findByMemberId(memberId);
        //如果没有账户 创建一个账户
        if (phoneFeeAccountDbo == null) {
            phoneFeeAccountDbo = new PhoneFeeAccountDbo();
            phoneFeeAccountDbo.setId(memberId + "phonefee_wallet");
            phoneFeeAccountDbo.setBalance(accountingRecord.getBalanceAfter());
            phoneFeeAccountDbo.setMemberId(memberId);
            memberPhoneFeeAccountDao.save(phoneFeeAccountDbo);
        } else {
            this.memberPhoneFeeAccountDao.update(accountingRecord.getAccountId(), accountingRecord.getBalanceAfter());
        }
        return dbo;
    }

    public double find(String memberId) {
        return memberPhoneFeeAccountDao.findByMemberId(memberId).getBalance();
    }

    public PhoneFeeAccountDbo findAccount(String memberId) {
        return memberPhoneFeeAccountDao.findByMemberId(memberId);
    }

}
