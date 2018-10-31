package com.anbang.qipai.members.cqrs.c.service.impl;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.domain.hongbao.MemberHongBaoAccountManager;
import com.anbang.qipai.members.cqrs.c.domain.prize.ExchangeRecord;
import com.anbang.qipai.members.cqrs.c.domain.prize.ExchangeException;
import com.anbang.qipai.members.cqrs.c.domain.prize.HongBaoExchangeHolder;
import com.anbang.qipai.members.cqrs.c.service.MemberHongBaoCmdService;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;
import com.dml.accounting.TextAccountingSummary;
import org.springframework.stereotype.Component;

@Component
public class MemberHongBaoCmdServiceImpl extends CmdServiceBase implements MemberHongBaoCmdService {

    @Override
    public AccountingRecord giveHongBaoToMember(String memberId, Integer amount, String textSummary, long currentTime)
            throws MemberNotFoundException {
        MemberHongBaoAccountManager hongBaoAccountManager = this.singletonEntityRepository.getEntity(MemberHongBaoAccountManager.class);
        return hongBaoAccountManager.giveHongBaoToMember(memberId, amount, new TextAccountingSummary(textSummary), currentTime);
    }

    @Override
    public ExchangeRecord exchange(String memberId, Integer amount, String textSummary, Long currentTime) throws InsufficientBalanceException, MemberNotFoundException, ExchangeException {
        HongBaoExchangeHolder holder = this.singletonEntityRepository.getEntity(HongBaoExchangeHolder.class);
        Integer money = holder.get(amount);
        MemberHongBaoAccountManager hongBaoAccountManager = this.singletonEntityRepository.getEntity(MemberHongBaoAccountManager.class);
        AccountingRecord accountingRecord = hongBaoAccountManager.withdraw(memberId, amount, new TextAccountingSummary(textSummary), currentTime);
        return new ExchangeRecord(accountingRecord, money);
    }

}
