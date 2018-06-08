package com.anbang.qipai.members.cqrs.c.domain.gold;

import java.util.HashMap;
import java.util.Map;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.dml.accounting.Account;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.AccountingSubject;
import com.dml.accounting.AccountingSummary;

public class MemberGoldAccountManager {

	private Map<String, Account> idAccountMap = new HashMap<>();

	private Map<String, String> memberIdAccountIdMap = new HashMap<>();

	public String createGoldAccountForNewMember(String memberId) throws MemberHasGoldAccountAlreadyException {
		if (memberIdAccountIdMap.containsKey(memberId)) {
			throw new MemberHasGoldAccountAlreadyException();
		}
		MemberGoldAccountOwner mao = new MemberGoldAccountOwner();
		mao.setMemberId(memberId);

		AccountingSubject subject = new AccountingSubject();
		subject.setName("wallet");

		Account account = new Account();
		account.setId(memberId + "_gold_wallet");
		account.setCurrency("gold");
		account.setOwner(mao);
		account.setSubject(subject);

		idAccountMap.put(account.getId(), account);
		memberIdAccountIdMap.put(memberId, account.getId());
		return account.getId();
	}

	public AccountingRecord giveGoldToMember(String memberId, int giveGoldAmount, AccountingSummary accountingSummary,
			long giveTime) throws MemberNotFoundException {
		if (!memberIdAccountIdMap.containsKey(memberId)) {
			throw new MemberNotFoundException();
		}
		Account account = idAccountMap.get(memberIdAccountIdMap.get(memberId));
		AccountingRecord record = account.deposit(giveGoldAmount, accountingSummary, giveTime);
		return record;
	}

}
