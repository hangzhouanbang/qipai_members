package com.anbang.qipai.members.cqrs.c.service;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;

public interface MemberScoreCmdService {
	AccountingRecord giveScoreToMember(String memberId, Integer amount, String textSummary, Long currentTime)
			throws MemberNotFoundException;

	AccountingRecord withdraw(String memberId, Integer amount, String textSummary, Long currentTime)
			throws InsufficientBalanceException, MemberNotFoundException;
}
