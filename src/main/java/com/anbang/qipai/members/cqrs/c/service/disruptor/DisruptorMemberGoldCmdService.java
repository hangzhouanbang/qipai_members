package com.anbang.qipai.members.cqrs.c.service.disruptor;

import com.anbang.qipai.members.cqrs.c.domain.prize.ObatinSigningPrizeRecord;
import com.anbang.qipai.members.cqrs.c.domain.sign.SignTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.service.MemberGoldCmdService;
import com.anbang.qipai.members.cqrs.c.service.impl.MemberGoldCmdServiceImpl;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;
import com.highto.framework.concurrent.DeferredResult;
import com.highto.framework.ddd.CommonCommand;

@Component(value = "memberGoldCmdService")
public class DisruptorMemberGoldCmdService extends DisruptorCmdServiceBase implements MemberGoldCmdService {

	@Autowired
	private MemberGoldCmdServiceImpl memberGoldCmdServiceImpl;

	@Override
	public AccountingRecord withdraw(String memberId, Integer amount, String textSummary, Long currentTime)
			throws InsufficientBalanceException, MemberNotFoundException {
		CommonCommand cmd = new CommonCommand(MemberGoldCmdServiceImpl.class.getName(), "withdraw", memberId, amount,
				textSummary, currentTime);
		DeferredResult<AccountingRecord> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			AccountingRecord accountingRecord = memberGoldCmdServiceImpl.withdraw(cmd.getParameter(),
					cmd.getParameter(), cmd.getParameter(), cmd.getParameter());
			return accountingRecord;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			if (e instanceof InsufficientBalanceException) {
				throw (InsufficientBalanceException) e;
			} else if (e instanceof MemberNotFoundException) {
				throw (MemberNotFoundException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public ObatinSigningPrizeRecord obtainSignPrizeGold(String memberId, SignTypeEnum signTypeEnum) {
		return null;
	}

	@Override
	public AccountingRecord giveGoldToMember(String memberId, Integer amount, String textSummary, Long currentTime)
			throws MemberNotFoundException {
		CommonCommand cmd = new CommonCommand(MemberGoldCmdServiceImpl.class.getName(), "giveGoldToMember", memberId,
				amount, textSummary, currentTime);
		DeferredResult<AccountingRecord> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			AccountingRecord accountingRecord = memberGoldCmdServiceImpl.giveGoldToMember(cmd.getParameter(),
					cmd.getParameter(), cmd.getParameter(), cmd.getParameter());
			return accountingRecord;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			if (e instanceof MemberNotFoundException) {
				throw (MemberNotFoundException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

}
