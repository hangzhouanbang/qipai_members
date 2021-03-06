package com.anbang.qipai.members.cqrs.c.service.impl;

import org.springframework.stereotype.Component;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.domain.score.MemberScoreAccountManager;
import com.anbang.qipai.members.cqrs.c.service.MemberScoreCmdService;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;
import com.dml.accounting.TextAccountingSummary;

@Component
public class MemberScoreCmdServiceImpl extends CmdServiceBase implements MemberScoreCmdService {

	@Override
	public AccountingRecord giveScoreToMember(String memberId, Integer amount, String textSummary, Long currentTime)
			throws MemberNotFoundException {
		MemberScoreAccountManager memberScoreAccountManager = singletonEntityRepository
				.getEntity(MemberScoreAccountManager.class);
		return memberScoreAccountManager.giveScoreToMember(memberId, amount, new TextAccountingSummary(textSummary),
				currentTime);
	}

	@Override
	public AccountingRecord withdraw(String memberId, Integer amount, String textSummary, Long currentTime)
			throws InsufficientBalanceException, MemberNotFoundException {
		MemberScoreAccountManager memberScoreAccountManager = singletonEntityRepository
				.getEntity(MemberScoreAccountManager.class);
		AccountingRecord rcd = memberScoreAccountManager.withdraw(memberId, amount,
				new TextAccountingSummary(textSummary), currentTime);
		return rcd;
	}

}
