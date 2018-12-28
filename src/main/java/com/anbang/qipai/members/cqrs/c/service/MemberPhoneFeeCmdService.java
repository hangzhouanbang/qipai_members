package com.anbang.qipai.members.cqrs.c.service;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.domain.prize.ExchangeException;
import com.anbang.qipai.members.cqrs.c.domain.prize.ExchangeRecord;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;

public interface MemberPhoneFeeCmdService {

    AccountingRecord givePhoneFeeToMember(String memberId, Integer amount, String textSummary, Long currentTime)
            throws MemberNotFoundException;

    ExchangeRecord exchange(String memberId, Integer amount, String textSummary, Long currentTime)
            throws InsufficientBalanceException, MemberNotFoundException, ExchangeException;

}
