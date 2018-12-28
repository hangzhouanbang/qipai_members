package com.anbang.qipai.members.cqrs.c.service.disruptor;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.domain.prize.ExchangeException;
import com.anbang.qipai.members.cqrs.c.domain.prize.ExchangeRecord;
import com.anbang.qipai.members.cqrs.c.service.MemberPhoneFeeCmdService;
import com.anbang.qipai.members.cqrs.c.service.impl.MemberPhoneFeeCmdServiceImpl;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;
import com.highto.framework.concurrent.DeferredResult;
import com.highto.framework.ddd.CommonCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

@Component("memberPhoneFeeCmdService")
public class DisruptorMemberPhoneFeeCmdService extends DisruptorCmdServiceBase implements MemberPhoneFeeCmdService {

    @Autowired
    private MemberPhoneFeeCmdServiceImpl memberPhoneFeeCmdService;

    @Override
    public AccountingRecord givePhoneFeeToMember(String memberId, Integer amount, String textSummary, Long currentTime) throws MemberNotFoundException {
        CommonCommand cmd = new CommonCommand(MemberPhoneFeeCmdServiceImpl.class.getName(), "givePhoneFeeToMember", memberId, amount, textSummary, currentTime);
        DeferredResult<AccountingRecord> deferredResult = publishEvent(this.disruptorFactory.getCoreCmdDisruptor(), cmd, new Callable<AccountingRecord>() {
            @Override
            public AccountingRecord call() throws Exception {
                return memberPhoneFeeCmdService.givePhoneFeeToMember(cmd.getParameter(), cmd.getParameter(), cmd.getParameter(), cmd.getParameter());
            }
        });
        try {
            return deferredResult.getResult();
        } catch (Exception e) {
            if (e instanceof MemberNotFoundException) {
                throw (MemberNotFoundException) e;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public ExchangeRecord exchange(String memberId, Integer amount, String textSummary, Long currentTime) throws InsufficientBalanceException, MemberNotFoundException, ExchangeException {
        CommonCommand cmd = new CommonCommand(MemberPhoneFeeCmdServiceImpl.class.getName(), "exchange", memberId, amount, textSummary, currentTime);
        DeferredResult<ExchangeRecord> deferredResult = publishEvent(this.disruptorFactory.getCoreCmdDisruptor(), cmd, new Callable<ExchangeRecord>() {
            @Override
            public ExchangeRecord call() throws Exception {
                return memberPhoneFeeCmdService.exchange(cmd.getParameter(), cmd.getParameter(), cmd.getParameter(), cmd.getParameter());
            }
        });
        try {
            return deferredResult.getResult();
        } catch (Exception e) {
            if (e instanceof MemberNotFoundException) {
                throw (MemberNotFoundException) e;
            } else if (e instanceof InsufficientBalanceException) {
                throw (InsufficientBalanceException) e;
            } else if (e instanceof ExchangeException) {
                throw (ExchangeException) e;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

}