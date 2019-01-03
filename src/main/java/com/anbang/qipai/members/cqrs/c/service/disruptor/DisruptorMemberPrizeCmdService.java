package com.anbang.qipai.members.cqrs.c.service.disruptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.cqrs.c.domain.prize.Lottery;
import com.anbang.qipai.members.cqrs.c.domain.prize.RaffleHistoryValueObject;
import com.anbang.qipai.members.cqrs.c.domain.sign.SignEvent;
import com.anbang.qipai.members.cqrs.c.domain.sign.SignHistoryValueObject;
import com.anbang.qipai.members.cqrs.c.domain.sign.SignPrizeOpportunityValueObject;
import com.anbang.qipai.members.cqrs.c.domain.sign.SigningRaffleOpportunity;
import com.anbang.qipai.members.cqrs.c.domain.vip.VIPEnum;
import com.anbang.qipai.members.cqrs.c.service.MemberPrizeCmdService;
import com.anbang.qipai.members.cqrs.c.service.impl.MemberPrizeCmdServiceImpl;
import com.highto.framework.concurrent.DeferredResult;
import com.highto.framework.ddd.CommonCommand;

@Component("memberPrizeCmdService")
public class DisruptorMemberPrizeCmdService extends DisruptorCmdServiceBase
		implements MemberPrizeCmdService, ApplicationListener {

	@Autowired
	private MemberPrizeCmdServiceImpl prizeCmdServiceImpl;

	@Override
	public SignPrizeOpportunityValueObject addSignPrizeOpportunity(String memberId, Integer continuousSignDays,
			Long signTime, VIPEnum vipLevel) {
		CommonCommand cmd = new CommonCommand(MemberPrizeCmdServiceImpl.class.getName(), "addSignPrizeOpportunity",
				memberId, continuousSignDays, signTime);
		DeferredResult<SignPrizeOpportunityValueObject> result = publishEvent(
				this.disruptorFactory.getCoreCmdDisruptor(), cmd, new Callable<SignPrizeOpportunityValueObject>() {
					@Override
					public SignPrizeOpportunityValueObject call() throws Exception {
						return prizeCmdServiceImpl.addSignPrizeOpportunity(cmd.getParameter(), cmd.getParameter(),
								cmd.getParameter(), vipLevel);
					}
				});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public SigningRaffleOpportunity addSignRaffleOpportunity(String memberId, Integer continuousSignDays,
			Long signTime) {
		CommonCommand cmd = new CommonCommand(MemberPrizeCmdServiceImpl.class.getName(), "addSignRaffleOpportunity",
				memberId, continuousSignDays, signTime);
		DeferredResult<SigningRaffleOpportunity> deferredResult = publishEvent(
				this.disruptorFactory.getCoreCmdDisruptor(), cmd, new Callable<SigningRaffleOpportunity>() {
					@Override
					public SigningRaffleOpportunity call() throws Exception {
						return prizeCmdServiceImpl.addSignRaffleOpportunity(cmd.getParameter(), cmd.getParameter(),
								cmd.getParameter());
					}
				});
		try {
			return deferredResult.getResult();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public RaffleHistoryValueObject raffle(String memberId, Boolean first) throws Exception {
		CommonCommand cmd = new CommonCommand(MemberPrizeCmdServiceImpl.class.getName(), "raffle", memberId, first);
		DeferredResult<RaffleHistoryValueObject> deferredResult = publishEvent(
				this.disruptorFactory.getCoreCmdDisruptor(), cmd, new Callable<RaffleHistoryValueObject>() {
					@Override
					public RaffleHistoryValueObject call() throws Exception {
						return prizeCmdServiceImpl.raffle(cmd.getParameter(), cmd.getParameter());
					}
				});
		return deferredResult.getResult();
	}

	@Override
	public Boolean isRaffleTableInitalized() {
		CommonCommand cmd = new CommonCommand(MemberPrizeCmdServiceImpl.class.getName(), "isRaffleTableInitalized");
		DeferredResult<Boolean> deferredResult = publishEvent(this.disruptorFactory.getCoreCmdDisruptor(), cmd,
				new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						return prizeCmdServiceImpl.isRaffleTableInitalized();
					}
				});
		try {
			return deferredResult.getResult();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void inializeRaffleTable(ArrayList<Lottery> lotteryList) {
		CommonCommand cmd = new CommonCommand(MemberPrizeCmdServiceImpl.class.getName(), "inializeRaffleTable",
				lotteryList);
		DeferredResult<Object> deferredResult = publishEvent(this.disruptorFactory.getCoreCmdDisruptor(), cmd,
				new Callable<Object>() {

					@Override
					public Object call() throws Exception {
						prizeCmdServiceImpl.inializeRaffleTable(lotteryList);
						return null;
					}
				});
		try {
			deferredResult.getResult();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof SignEvent) {
			final SignHistoryValueObject signHistoryValueObject = (SignHistoryValueObject) event.getSource();
			this.addSignRaffleOpportunity(signHistoryValueObject.getMemberId(),
					signHistoryValueObject.getContinuousSignDays(), signHistoryValueObject.getTime());
			this.addSignPrizeOpportunity(signHistoryValueObject.getMemberId(),
					signHistoryValueObject.getContinuousSignDays(), signHistoryValueObject.getTime(),
					VIPEnum.of(signHistoryValueObject.getVipLevel()));
		}
	}

}
