package com.anbang.qipai.members.cqrs.c.domain.hongbao;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.dml.accounting.*;

import java.util.HashMap;
import java.util.Map;

public class MemberHongBaoAccountManager {

    private Map<String, Account> idAccountMap = new HashMap<>();

    private Map<String, String> memberIdAccountIdMap = new HashMap<>();

    private Map<Integer/*积分*/, Integer/*钱*/> scoreMoneyMap = new HashMap<>();


    public String createHongBaoAccountForNewMember(String memberId) throws MemberHongBaoAccountAlreadException {
        if (memberIdAccountIdMap.containsKey(memberId)) {
            throw new MemberHongBaoAccountAlreadException();
        }
        MemberHongBaoAccountOwner owner = new MemberHongBaoAccountOwner();
        owner.setMemberId(memberId);

        AccountingSubject subject = new AccountingSubject();
        subject.setName("wallat");

        Account account = new Account();
        account.setId(memberId + "_hongbao_wallet");
        account.setCurrency("hongbao");
        account.setOwner(owner);
        account.setSubject(subject);

        idAccountMap.put(account.getId(), account);
        memberIdAccountIdMap.put(memberId, account.getId());
        return account.getId();
    }

    public AccountingRecord giveHongBaoToMember(String memberId, int amount, AccountingSummary accountingSummary,
                                                long time) throws MemberNotFoundException {
        if (!memberIdAccountIdMap.containsKey(memberId)) {
            throw new MemberNotFoundException();
        }
        Account account = idAccountMap.get(memberIdAccountIdMap.get(memberId));
        return account.deposit(amount, accountingSummary, time);
    }

    public AccountingRecord withdraw(String memberId, int amount, AccountingSummary accountingSummary,
                                     long time) throws MemberNotFoundException, InsufficientBalanceException {
        Account account = idAccountMap.get(memberIdAccountIdMap.get(memberId));
        return account.withdraw(amount, accountingSummary, time);
    }

}
