package com.anbang.qipai.members.cqrs.c.service.impl;

import com.anbang.qipai.members.cqrs.c.domain.prize.Lottery;
import com.anbang.qipai.members.cqrs.c.domain.prize.LotteryTable;
import com.anbang.qipai.members.cqrs.c.domain.prize.RaffleHistory;
import com.anbang.qipai.members.cqrs.c.domain.prize.RaffleHistoryValueObject;
import com.anbang.qipai.members.cqrs.c.domain.sign.*;
import com.anbang.qipai.members.cqrs.c.domain.vip.VIPEnum;
import com.anbang.qipai.members.cqrs.c.service.MemberPrizeCmdService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemberPrizeCmdServiceImpl extends CmdServiceBase implements MemberPrizeCmdService {

    @Override
    public SignPrizeOpportunityValueObject addSignPrizeOpportunity(String memberId, Integer continuousSignDays, Long signTime, VIPEnum vipLevel) {
        SigningPrizeOpportunityManager signingPrizeOpportunityManager = this.singletonEntityRepository.getEntity(SigningPrizeOpportunityManager.class);
        ObtainSignPrizeOpportunity opportunity = signingPrizeOpportunityManager.addMemberOpportunity(memberId, continuousSignDays, signTime, vipLevel);
        return opportunity == null ? null : new SignPrizeOpportunityValueObject(opportunity);
    }

    @Override
    public SigningRaffleOpportunity addSignRaffleOpportunity(String memberId, Integer continuousSignDays, Long signTime) {
        SigningRaffleOpportunityManager signingRaffleOpportunityManager = this.singletonEntityRepository.getEntity(SigningRaffleOpportunityManager.class);
        return signingRaffleOpportunityManager.addMemberOpportunity(memberId);
    }

    @Override
    public RaffleHistoryValueObject raffle(String memberId, Boolean first) throws Exception {
        SigningRaffleOpportunityManager signingRaffleOpportunityManager = this.singletonEntityRepository.getEntity(SigningRaffleOpportunityManager.class);
        signingRaffleOpportunityManager.useOpportunity(memberId);
        LotteryTable lotteryTable = this.singletonEntityRepository.getEntity(LotteryTable.class);
        final RaffleHistory raffleHistory = lotteryTable.raffle(memberId, first);
        return new RaffleHistoryValueObject(raffleHistory);
    }

    @Override
    public boolean isRaffleTableInitalized() {
        LotteryTable lotteryTable = this.singletonEntityRepository.getEntity(LotteryTable.class);
        List<Lottery> lotteryList = lotteryTable.list();
        return (lotteryList != null && !lotteryList.isEmpty());
    }

    @Override
    public void inializeRaffleTable(List<Lottery> lotteryList) {
        LotteryTable lotteryTable = this.singletonEntityRepository.getEntity(LotteryTable.class);
        lotteryTable.updateLotterySet(lotteryList);
    }


}
