package com.anbang.qipai.members.msg.channel.source;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface SignInPrizeLogSource {

    String RAFFLE_HISTORY = "RAFFLE_HISTORY";
    String PRIZE_EXCHANGE = "EXCHANGE_HONGBAO_PHONEFEE";
    String OBTAIN_SIGN_PRIZE = "OBTAIN_SIGN_PRIZE_LOG";
    String RAFFLE_HISTORY_ADDRESS = "RAFFLE_HISTORY_ADDRESS";//发送实体

    @Output
    MessageChannel signInPrizeLog();

}
