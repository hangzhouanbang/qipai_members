package com.anbang.qipai.members.msg.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MembersSource {

	@Output
	MessageChannel members();

}
