package com.anbang.qipai.members.cqrs.q.service;

import com.anbang.qipai.members.cqrs.c.domain.CreateMemberResult;
import com.anbang.qipai.members.cqrs.q.dao.MemberHongBaoAccountDao;
import com.anbang.qipai.members.cqrs.q.dao.MemberHongBaoRecordDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.HongBaoAccountDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberHongBaoRecordDbo;
import com.dml.accounting.AccountingRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HongBaoQueryService {

    @Autowired
    private MemberHongBaoRecordDboDao memberHongBaoRecordDboDao;

    @Autowired
    private MemberHongBaoAccountDao memberHongBaoAccountDao;


    public HongBaoAccountDbo createAccount(CreateMemberResult createMemberResult) {
        HongBaoAccountDbo account = new HongBaoAccountDbo();
        account.setId(createMemberResult.getHongBaoAccountId());
        account.setMemberId(createMemberResult.getMemberId());
        memberHongBaoAccountDao.save(account);
        return account;
    }

    public MemberHongBaoRecordDbo withdraw(String memberId, AccountingRecord accountingRecord) {
        MemberHongBaoRecordDbo dbo = new MemberHongBaoRecordDbo();
        dbo.setAccountId(accountingRecord.getAccountId());
        dbo.setMemberId(memberId);
        dbo.setAccountingAmount(accountingRecord.getAccountingAmount());
        dbo.setBalanceAfter(accountingRecord.getBalanceAfter());
        dbo.setSummary(accountingRecord.getSummary());
        dbo.setTime(accountingRecord.getAccountingTime());
        dbo.setAccountingNo(accountingRecord.getAccountingNo());
        this.memberHongBaoRecordDboDao.save(dbo);

//        如果没有账户 就先新建一个账户
        HongBaoAccountDbo hongBaoAccountDbo = memberHongBaoAccountDao.find(memberId);
        if (hongBaoAccountDbo == null) {
            hongBaoAccountDbo = new HongBaoAccountDbo();
            hongBaoAccountDbo.setMemberId(memberId);
            hongBaoAccountDbo.setId(memberId + "\uFEFF_hongbao_wallet");
            hongBaoAccountDbo.setBalance((int) accountingRecord.getBalanceAfter());
            memberHongBaoAccountDao.save(hongBaoAccountDbo);
        } else {
            this.memberHongBaoAccountDao.update(accountingRecord.getAccountId(), accountingRecord.getBalanceAfter());
        }
        return dbo;
    }


    public double find(String memberId) {
        HongBaoAccountDbo hongBaoAccountDbo = this.memberHongBaoAccountDao.find(memberId);
        return hongBaoAccountDbo.getBalance();
    }

    public HongBaoAccountDbo findAccount(String memberId) {
        return memberHongBaoAccountDao.find(memberId);
    }
}
