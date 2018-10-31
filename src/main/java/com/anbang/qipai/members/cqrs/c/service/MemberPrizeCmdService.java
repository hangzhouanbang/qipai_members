package com.anbang.qipai.members.cqrs.c.service;

import com.anbang.qipai.members.cqrs.c.domain.prize.Lottery;
import com.anbang.qipai.members.cqrs.c.domain.prize.RaffleHistoryValueObject;
import com.anbang.qipai.members.cqrs.c.domain.sign.SignPrizeOpportunityValueObject;
import com.anbang.qipai.members.cqrs.c.domain.sign.SigningRaffleOpportunity;
import com.anbang.qipai.members.cqrs.c.domain.vip.VIPEnum;

import java.util.List;

public interface MemberPrizeCmdService {

    SignPrizeOpportunityValueObject addSignPrizeOpportunity(String memberId, int continuousSignDays, long signTime, VIPEnum vipLevel);

    SigningRaffleOpportunity addSignRaffleOpportunity(String memberId, int continuousSignDays, long signTime);

    RaffleHistoryValueObject raffle(String memberId, boolean first) throws Exception;

    boolean isRaffleTableInitalized();

    void inializeRaffleTable(List<Lottery> lotterySet);

}
