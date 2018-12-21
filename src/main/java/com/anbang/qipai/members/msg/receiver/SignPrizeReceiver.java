package com.anbang.qipai.members.msg.receiver;


import com.alibaba.fastjson.JSON;
import com.anbang.qipai.members.cqrs.c.domain.prize.Lottery;
import com.anbang.qipai.members.cqrs.c.domain.prize.LotteryTypeEnum;
import com.anbang.qipai.members.cqrs.c.service.MemberPrizeCmdService;
import com.anbang.qipai.members.cqrs.q.dbo.LotteryDbo;
import com.anbang.qipai.members.cqrs.q.service.LotteryQueryService;
import com.anbang.qipai.members.msg.channel.sink.SignInPrizeSink;
import com.anbang.qipai.members.msg.msjobj.AdminLotteryMo;
import com.anbang.qipai.members.msg.msjobj.CommonMO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EnableBinding(SignInPrizeSink.class)
public class SignPrizeReceiver {

    private static final String SIGNING_PRIZE = "SIGNING_PRIZE";

    private static final String PUBLISH_LOTTERY = "releaseSignInPrize";

    private static final Logger logger = Logger.getLogger(SignPrizeReceiver.class);

    @Autowired
    private MemberPrizeCmdService memberPrizeCmdService;

    @Autowired
    private LotteryQueryService lotteryQueryService;

    private Gson gson = new Gson();

    @StreamListener(SignInPrizeSink.CHANNEL)
    public void signInPrize(CommonMO mo) {
        System.out.println(">>> 监听到消息:" + mo);
        String msg = mo.getMsg();
        if (msg.equals(PUBLISH_LOTTERY)) {
            String json = gson.toJson(mo.getData());

            List<AdminLotteryMo> list = JSON.parseArray(json, AdminLotteryMo.class);

            List<Lottery> lotteryList = new ArrayList<>();
            for (AdminLotteryMo lotteryMo : list) {
                Lottery lottery = new Lottery(lotteryMo.getId(),
                        lotteryMo.getName(),
                        lotteryMo.getPrizeProb(),
                        lotteryMo.getFirstPrizeProb(),
                        this.ofAdvice(lotteryMo),
                        lotteryMo.getSingleNum(),
                        lotteryMo.getStoreNum(),
                        lotteryMo.getOverstep().equals("是"));
                lotteryList.add(lottery);
            }

            //更新转盘
            this.memberPrizeCmdService.inializeRaffleTable(lotteryList);

            //保存进数据库
            List<LotteryDbo> lotteryDboList = new ArrayList<>();
            for (AdminLotteryMo lotteryMo : list) {
                LotteryDbo lotteryDbo = new LotteryDbo();
                lotteryDbo.setId(lotteryMo.getId());
                lotteryDbo.setName(lotteryMo.getName());
                lotteryDbo.setIcon(lotteryMo.getIconUrl());
                lotteryDbo.setProp(lotteryMo.getPrizeProb());
                lotteryDbo.setFirstProp(lotteryMo.getFirstPrizeProb());
                lotteryDbo.setSingleNum(lotteryMo.getSingleNum());
                lotteryDbo.setStock(lotteryMo.getStoreNum());
                boolean overstep = lotteryMo.getOverstep().equals("是");
                lotteryDbo.setOverStep(overstep);
                LotteryTypeEnum type = this.ofAdvice(lotteryMo);
                lotteryDbo.setType(type);
                if(lotteryMo.getType().equals("会员卡")){
                 lotteryDbo.setCardType(lotteryMo.getCardType());
                }
                lotteryDboList.add(lotteryDbo);
            }
            this.lotteryQueryService.discardBeforeAndSaveAll(lotteryDboList);
        }
    }

    private LotteryTypeEnum ofAdvice(AdminLotteryMo lotteryMo) {
        if (lotteryMo.getType().equals("红包")) {
            return LotteryTypeEnum.HONGBAO;
        }
        if (lotteryMo.getType().equals("玉石")) {
            return LotteryTypeEnum.GOLD;
        }
        if (lotteryMo.getType().equals("实物")) {
            return LotteryTypeEnum.ENTIRY;
        }
        if (lotteryMo.getType().equals("话费")) {
            return LotteryTypeEnum.PHONE_FEE;
        }
        if (lotteryMo.getType().equals("谢谢惠顾")) {
            return LotteryTypeEnum.NONE;
        }

        if (lotteryMo.getType().equals("会员卡")) {

            if (lotteryMo.getCardType().equals("日卡")) {
                return LotteryTypeEnum.MEMBER_CARD_DAY;
            }

            if (lotteryMo.getCardType().equals("周卡")) {
                return LotteryTypeEnum.MEMBER_CARD_WEAK;
            }

            if (lotteryMo.getCardType().equals("月卡")) {
                return LotteryTypeEnum.MEMBER_CARD_MONTH;
            }

            if (lotteryMo.getCardType().equals("季卡")) {
                return LotteryTypeEnum.MEMBER_CARD_SEASON;
            }
        }
        return null;
    }


    private LotteryTypeEnum of(String type) {
        switch (type) {
            case "红包":
                return LotteryTypeEnum.HONGBAO;
            case "日卡":
                return LotteryTypeEnum.MEMBER_CARD_DAY;
            case "周卡":
                return LotteryTypeEnum.MEMBER_CARD_WEAK;
            case "月卡":
                return LotteryTypeEnum.MEMBER_CARD_MONTH;
            case "季卡":
                return LotteryTypeEnum.MEMBER_CARD_SEASON;
            case "玉石":
                return LotteryTypeEnum.GOLD;
            case "实物":
                return LotteryTypeEnum.ENTIRY;
            case "话费":
                return LotteryTypeEnum.PHONE_FEE;
            case "谢谢惠顾":
                return LotteryTypeEnum.NONE;
            default:
                return null;
        }
    }

//    @StreamListener(SignInPrizeSink.CHANNEL)
//    public void consumer(@Payload CommonMO commonMO, @Headers Map headers) {
//        logger.info("已消费");
//
//    }
//

}
