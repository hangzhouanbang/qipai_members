package com.anbang.qipai.members.cqrs.c.service;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.dml.accounting.AccountingRecord;

public interface MemberScoreCmdService {
	AccountingRecord giveScoreToMember(String memberId, Integer amount, String textSummary, Long currentTime)
			throws MemberNotFoundException;
}
