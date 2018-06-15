package com.anbang.qipai.members.cqrs.c.service.disruptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.service.MemberScoreCmdService;
import com.anbang.qipai.members.cqrs.c.service.impl.MemberScoreCmdServiceImpl;
import com.dml.accounting.AccountingRecord;
import com.highto.framework.concurrent.DeferredResult;
import com.highto.framework.ddd.CommonCommand;

@Component(value = "memberScoreCmdService")
public class DisruptorMemberScoreCmdService extends DisruptorCmdServiceBase implements MemberScoreCmdService {

	@Autowired
	private MemberScoreCmdServiceImpl memberScoreCmdServiceImpl;

	@Override
	public AccountingRecord giveScoreToMember(String memberId, Integer amount, String textSummary, Long currentTime)
			throws MemberNotFoundException {
		CommonCommand cmd = new CommonCommand(MemberScoreCmdServiceImpl.class.getName(), "giveScoreToMember", memberId,
				amount, textSummary, currentTime);
		DeferredResult<AccountingRecord> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			AccountingRecord accountingRecord = memberScoreCmdServiceImpl.giveScoreToMember(cmd.getParameter(),
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
