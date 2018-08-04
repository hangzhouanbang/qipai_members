package com.anbang.qipai.members.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.members.msg.channel.MemberRightsConfigurationSource;
import com.anbang.qipai.members.msg.msjobj.CommonMO;
import com.anbang.qipai.members.plan.bean.MemberRightsConfiguration;

@EnableBinding(MemberRightsConfigurationSource.class)
public class MemberRightsConfigurationMsgService {
	@Autowired
	private MemberRightsConfigurationSource memberRightsConfigurationSource;

	public void saveConfiguration(MemberRightsConfiguration memberRightsConfiguration) {
		CommonMO mo = new CommonMO();
		mo.setMsg("qipai_members_conf");
		mo.setData(memberRightsConfiguration);
		memberRightsConfigurationSource.memberrightsconfiguration().send(MessageBuilder.withPayload(mo).build());
	}

}
