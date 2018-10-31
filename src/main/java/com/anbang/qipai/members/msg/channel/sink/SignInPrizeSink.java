package com.anbang.qipai.members.msg.channel.sink;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 签到与抽奖通道
 */
public interface SignInPrizeSink {

    String CHANNEL = "signInPrize";

    @Input
    SubscribableChannel signInPrize();

}
