package com.anbang.qipai.members.msg.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface RuianMajiangJuResultSink {
	String RUIANMAJIANGJURESULT = "ruianMajiangJuResult";

	@Input
	SubscribableChannel ruianMajiangJuResult();
}
