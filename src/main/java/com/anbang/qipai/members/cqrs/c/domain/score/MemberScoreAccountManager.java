package com.anbang.qipai.members.cqrs.c.domain.score;

import java.util.HashMap;
import java.util.Map;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.dml.accounting.Account;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.AccountingSubject;
import com.dml.accounting.AccountingSummary;
import com.dml.accounting.InsufficientBalanceException;

public class MemberScoreAccountManager {

	private Map<String, Account> idAccountMap = new HashMap<>();

	private Map<String, String> memberIdAccountIdMap = new HashMap<>();

	public String createScoreAccountForNewMember(String memberId) throws MemberHasScoreAccountAlreadyException {
		if (memberIdAccountIdMap.containsKey(memberId)) {
			throw new MemberHasScoreAccountAlreadyException();
		}
		MemberScoreAccountOwner mao = new MemberScoreAccountOwner();
		mao.setMemberId(memberId);

		AccountingSubject subject = new AccountingSubject();
		subject.setName("wallet");

		Account account = new Account();
		account.setId(memberId + "_score_wallet");
		account.setCurrency("score");
		account.setOwner(mao);
		account.setSubject(subject);

		idAccountMap.put(account.getId(), account);
		memberIdAccountIdMap.put(memberId, account.getId());
		return account.getId();
	}

	public AccountingRecord giveScoreToMember(String memberId, int giveScoreAmount, AccountingSummary accountingSummary,
			long giveTime) throws MemberNotFoundException {
		if (!memberIdAccountIdMap.containsKey(memberId)) {
			throw new MemberNotFoundException();
		}
		Account account = idAccountMap.get(memberIdAccountIdMap.get(memberId));
		AccountingRecord record = account.deposit(giveScoreAmount, accountingSummary, giveTime);
		return record;
	}

	public AccountingRecord withdraw(String memberId, int amount, AccountingSummary accountingSummary,
			long withdrawTime) throws MemberNotFoundException, InsufficientBalanceException {
		if (!memberIdAccountIdMap.containsKey(memberId)) {
			throw new MemberNotFoundException();
		}
		Account account = idAccountMap.get(memberIdAccountIdMap.get(memberId));
		AccountingRecord record = account.withdraw(amount, accountingSummary, withdrawTime);
		return record;
	}

	public Map<String, Account> getIdAccountMap() {
		return idAccountMap;
	}

	public void setIdAccountMap(Map<String, Account> idAccountMap) {
		this.idAccountMap = idAccountMap;
	}

	public Map<String, String> getMemberIdAccountIdMap() {
		return memberIdAccountIdMap;
	}

	public void setMemberIdAccountIdMap(Map<String, String> memberIdAccountIdMap) {
		this.memberIdAccountIdMap = memberIdAccountIdMap;
	}

}
