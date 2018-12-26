package com.anbang.qipai.members.msg.service;

import com.anbang.qipai.members.cqrs.c.domain.prize.ObatinSigningPrizeRecord;
import com.anbang.qipai.members.cqrs.q.dbo.MemberRaffleHistoryDbo;
import com.anbang.qipai.members.cqrs.q.dbo.ScoreExchangeRecordDbo;
import com.anbang.qipai.members.msg.channel.source.SignInPrizeLogSource;
import com.anbang.qipai.members.msg.msjobj.CommonMO;
import com.anbang.qipai.members.web.vo.EntityExchangeDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

@EnableBinding(SignInPrizeLogSource.class)
public class PrizeLogMsgService {

    @Autowired
    private SignInPrizeLogSource signInPrizeLogSource;

    Logger logger = LoggerFactory.getLogger(PrizeLogMsgService.class);

    public void sendRaffleRecord(MemberRaffleHistoryDbo memberRaffleHistoryDbo) {
        System.out.println(">>>发送消息:" + memberRaffleHistoryDbo);
        CommonMO commonMO = new CommonMO();
        commonMO.setMsg(SignInPrizeLogSource.RAFFLE_HISTORY);
        commonMO.setData(memberRaffleHistoryDbo);
        this.signInPrizeLogSource.signInPrizeLog().send(MessageBuilder.withPayload(commonMO).build());
    }

    public void sendObtainSignPrize(ObatinSigningPrizeRecord obtainSignPrizeRecordDbo) {
        System.out.println(">>>发送消息:" + obtainSignPrizeRecordDbo);
        CommonMO commonMO = new CommonMO();
        commonMO.setMsg(SignInPrizeLogSource.OBTAIN_SIGN_PRIZE);
        commonMO.setData(obtainSignPrizeRecordDbo);
        this.signInPrizeLogSource.signInPrizeLog().send(MessageBuilder.withPayload(commonMO).build());
    }

    public void sendExchangeLog(ScoreExchangeRecordDbo scoreExchangeRecordDbo) {
        System.out.println(">>>发送消息:" + scoreExchangeRecordDbo);
        CommonMO commonMO = new CommonMO();
        commonMO.setMsg(SignInPrizeLogSource.PRIZE_EXCHANGE);
        commonMO.setData(scoreExchangeRecordDbo);
        this.signInPrizeLogSource.signInPrizeLog().send(MessageBuilder.withPayload(commonMO).build());
    }

    public void sendEntityExchangeLog(EntityExchangeDO entityExchangeDO) {
        logger.info(">>>发送消息:" + entityExchangeDO.toString());
        CommonMO commonMO = new CommonMO();
        commonMO.setMsg(SignInPrizeLogSource.RAFFLE_HISTORY_ADDRESS);
        commonMO.setData(entityExchangeDO);
        this.signInPrizeLogSource.signInPrizeLog().send(MessageBuilder.withPayload(commonMO).build());
    }


}
