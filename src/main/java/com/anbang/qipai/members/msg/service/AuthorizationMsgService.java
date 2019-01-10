package com.anbang.qipai.members.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.members.cqrs.q.dbo.AuthorizationDbo;
import com.anbang.qipai.members.msg.channel.source.AuthorizationSource;
import com.anbang.qipai.members.msg.msjobj.CommonMO;

@EnableBinding(AuthorizationSource.class)
public class AuthorizationMsgService {

	@Autowired
	private AuthorizationSource authorizationSource;

	public void newAuthorization(AuthorizationDbo authDbo) {
		CommonMO mo = new CommonMO();
		mo.setMsg("new authorization");
		mo.setData(authDbo);
		authorizationSource.authorization().send(MessageBuilder.withPayload(mo).build());
	}
}
