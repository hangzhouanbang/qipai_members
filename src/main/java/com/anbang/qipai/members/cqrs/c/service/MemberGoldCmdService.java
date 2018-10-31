package com.anbang.qipai.members.cqrs.c.service;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.domain.prize.ObatinSigningPrizeRecord;
import com.anbang.qipai.members.cqrs.c.domain.sign.OpportunityInvalidUsedException;
import com.anbang.qipai.members.cqrs.c.domain.sign.OpportunityNotExistsExcetion;
import com.anbang.qipai.members.cqrs.c.domain.sign.SignTypeEnum;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;

public interface MemberGoldCmdService {
    AccountingRecord giveGoldToMember(String memberId, Integer amount, String textSummary, Long currentTime)
            throws MemberNotFoundException;

    AccountingRecord withdraw(String memberId, Integer amount, String textSummary, Long currentTime)
            throws InsufficientBalanceException, MemberNotFoundException;

    ObatinSigningPrizeRecord obtainSignPrizeGold(String memberId, SignTypeEnum signPrizeType) throws OpportunityInvalidUsedException, OpportunityNotExistsExcetion, MemberNotFoundException;

}
