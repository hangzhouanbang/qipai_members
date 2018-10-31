package com.anbang.qipai.members.cqrs.c.domain.phonefee;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.dml.accounting.*;

import java.util.HashMap;
import java.util.Map;

public class MemberPhoneFeeAccountManager {

    private Map<String, Account> idAccountMap = new HashMap<>();

    private Map<String, String> memberIdAccountIdMap = new HashMap<>();

    public String createPhoneFeeAccountForNewMember(String memberId) throws MemberPhoneFeeAccountExistsException {
        if (memberIdAccountIdMap.containsKey(memberId)) {
            throw new MemberPhoneFeeAccountExistsException();
        }
        MemberPhoneFeeAccountOwner owner = new MemberPhoneFeeAccountOwner();
        owner.setMemberId(memberId);

        AccountingSubject subject = new AccountingSubject();
        subject.setName("wallet");

        Account account = new Account();
        account.setId(memberId + "phonefee_wallet");
        account.setCurrency("phonefee");
        account.setOwner(owner);
        account.setSubject(subject);

        idAccountMap.put(account.getId(), account);
        memberIdAccountIdMap.put(memberId, account.getId());
        return account.getId();
    }

    public AccountingRecord givePhoneFeeToMember(String memberId, int amount, AccountingSummary accountingSummary,
                                             long giveTime) throws MemberNotFoundException {
        if (!memberIdAccountIdMap.containsKey(memberId)) {
            throw new MemberNotFoundException();
        }
        Account account = idAccountMap.get(memberIdAccountIdMap.get(memberId));
        return account.deposit(amount, accountingSummary, giveTime);
    }

    public AccountingRecord withdraw(String memberId, int amount, AccountingSummary accountingSummary,
                                     long withdrawTime) throws MemberNotFoundException, InsufficientBalanceException {
        if (!memberIdAccountIdMap.containsKey(memberId)) {
            throw new MemberNotFoundException();
        }
        Account account = idAccountMap.get(memberIdAccountIdMap.get(memberId));
        return account.withdraw(amount, accountingSummary, withdrawTime);
    }

}
