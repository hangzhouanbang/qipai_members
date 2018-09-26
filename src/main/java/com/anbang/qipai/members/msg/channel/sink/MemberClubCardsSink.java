package com.anbang.qipai.members.msg.channel.sink;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
*@author   created by hanzhuofan  2018.09.26
*/
public interface MemberClubCardsSink {
	String channel = "memberClubCard";
	String addClubCard = "addClubCard";
	String deleteClubCard = "deleteClubCard";
	String updateClubCard = "updateClubCard";
	
	@Input
	SubscribableChannel memberClubCard();
}
