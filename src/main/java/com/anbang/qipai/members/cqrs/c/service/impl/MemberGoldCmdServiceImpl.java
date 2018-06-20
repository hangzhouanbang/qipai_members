package com.anbang.qipai.members.cqrs.c.service.impl;

import org.springframework.stereotype.Component;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.domain.gold.MemberGoldAccountManager;
import com.anbang.qipai.members.cqrs.c.service.MemberGoldCmdService;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;
import com.dml.accounting.TextAccountingSummary;

@Component
public class MemberGoldCmdServiceImpl extends CmdServiceBase implements MemberGoldCmdService {

	@Override
	public AccountingRecord giveGoldToMember(String memberId, Integer amount, String textSummary, Long currentTime)
			throws MemberNotFoundException {
		MemberGoldAccountManager memberGoldAccountManager = singletonEntityRepository
				.getEntity(MemberGoldAccountManager.class);
		return memberGoldAccountManager.giveGoldToMember(memberId, amount, new TextAccountingSummary(textSummary),
				currentTime);
	}

	@Override
	public AccountingRecord withdraw(String memberId, Integer amount, String textSummary, Long currentTime)
			throws InsufficientBalanceException, MemberNotFoundException {
		MemberGoldAccountManager memberGoldAccountManager = singletonEntityRepository
				.getEntity(MemberGoldAccountManager.class);
		AccountingRecord rcd = memberGoldAccountManager.withdraw(memberId, amount,
				new TextAccountingSummary(textSummary), currentTime);
		return rcd;
	}

}
