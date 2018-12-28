package com.anbang.qipai.members.cqrs.c.service.impl;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.domain.phonefee.MemberPhoneFeeAccountManager;
import com.anbang.qipai.members.cqrs.c.domain.prize.ExchangeException;
import com.anbang.qipai.members.cqrs.c.domain.prize.ExchangeRecord;
import com.anbang.qipai.members.cqrs.c.domain.prize.PhoneFeeExchangeHolder;
import com.anbang.qipai.members.cqrs.c.service.MemberPhoneFeeCmdService;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;
import com.dml.accounting.TextAccountingSummary;
import org.springframework.stereotype.Component;

@Component
public class MemberPhoneFeeCmdServiceImpl extends CmdServiceBase implements MemberPhoneFeeCmdService {
    @Override
    public AccountingRecord givePhoneFeeToMember(String memberId, Integer amount, String textSummary, Long currentTime) throws MemberNotFoundException {
        MemberPhoneFeeAccountManager memberPhoneFeeAccountManager = this.singletonEntityRepository.getEntity(MemberPhoneFeeAccountManager.class);
        return memberPhoneFeeAccountManager.givePhoneFeeToMember(memberId, amount, new TextAccountingSummary(textSummary), currentTime);
    }

    @Override
    public ExchangeRecord exchange(String memberId, Integer amount, String textSummary, Long currentTime) throws InsufficientBalanceException, MemberNotFoundException, ExchangeException {
        PhoneFeeExchangeHolder phoneFeeExchangeHolder = this.singletonEntityRepository.getEntity(PhoneFeeExchangeHolder.class);
        Integer phoneFee = phoneFeeExchangeHolder.get(amount);
        MemberPhoneFeeAccountManager memberPhoneFeeAccountManager = this.singletonEntityRepository.getEntity(MemberPhoneFeeAccountManager.class);
        AccountingRecord accountingRecord = memberPhoneFeeAccountManager.withdraw(memberId, amount, new TextAccountingSummary(textSummary), currentTime);
        return new ExchangeRecord(accountingRecord, phoneFee);
    }
}
