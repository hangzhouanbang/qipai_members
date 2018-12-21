package com.anbang.qipai.members.cqrs.c.service;

import com.anbang.qipai.members.cqrs.c.domain.prize.Lottery;
import com.anbang.qipai.members.cqrs.c.domain.prize.RaffleHistoryValueObject;
import com.anbang.qipai.members.cqrs.c.domain.sign.SignPrizeOpportunityValueObject;
import com.anbang.qipai.members.cqrs.c.domain.sign.SigningRaffleOpportunity;
import com.anbang.qipai.members.cqrs.c.domain.vip.VIPEnum;

import java.util.List;

public interface MemberPrizeCmdService {

    SignPrizeOpportunityValueObject addSignPrizeOpportunity(String memberId, Integer continuousSignDays, Long signTime, VIPEnum vipLevel);

    SigningRaffleOpportunity addSignRaffleOpportunity(String memberId, Integer continuousSignDays, Long signTime);

    RaffleHistoryValueObject raffle(String memberId, Boolean first) throws Exception;

    boolean isRaffleTableInitalized();

    void inializeRaffleTable(List<Lottery> lotterySet);

}
