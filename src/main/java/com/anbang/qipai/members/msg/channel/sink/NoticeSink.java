package com.anbang.qipai.members.msg.channel.sink;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface NoticeSink {
	String NOTICE = "notice";

	@Input
	SubscribableChannel notice();
}
