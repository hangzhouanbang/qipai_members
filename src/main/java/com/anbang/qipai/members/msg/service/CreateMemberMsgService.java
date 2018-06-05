package com.anbang.qipai.members.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.members.msg.channel.CreateMemberSource;
import com.anbang.qipai.members.msg.msjobj.CommonMO;
import com.anbang.qipai.members.plan.domain.CreateMemberConfiguration;

@EnableBinding(CreateMemberSource.class)
public class CreateMemberMsgService {
	
	@Autowired
	private CreateMemberSource createMemberSource;
	
	public void createMember(CreateMemberConfiguration member) {
		CommonMO mo = new CommonMO();
		mo.setMsg("newCreateMember");
		mo.setData(member);
		createMemberSource.createmember().send(MessageBuilder.withPayload(mo).build());
	}

}
