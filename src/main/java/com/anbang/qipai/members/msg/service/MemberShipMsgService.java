package com.anbang.qipai.members.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.members.msg.channel.CreateMemberSource;
import com.anbang.qipai.members.msg.msjobj.CommonMO;
import com.anbang.qipai.members.plan.domain.MemberRights;

@EnableBinding(CreateMemberSource.class)
public class MemberShipMsgService {
	@Autowired
	private CreateMemberSource createMemberSource;
	
	public void saveCommonUser(MemberRights createMemberConfiguration) {
		CommonMO mo = new CommonMO();
		mo.setMsg("newCreateMemberConfiguration");
		mo.setData(createMemberConfiguration);
		createMemberSource.membershiprights().send(MessageBuilder.withPayload(mo).build());
	}
	
	public void savevipuser(MemberRights createMemberConfiguration) {
		CommonMO mo = new CommonMO();
		mo.setMsg("newCreateMemberConfiguration");
		mo.setData(createMemberConfiguration);
		createMemberSource.membershiprights().send(MessageBuilder.withPayload(mo).build());
	}

}
