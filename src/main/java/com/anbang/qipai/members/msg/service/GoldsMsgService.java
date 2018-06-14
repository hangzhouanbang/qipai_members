package com.anbang.qipai.members.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldRecordDbo;
import com.anbang.qipai.members.msg.channel.GoldsSource;
import com.anbang.qipai.members.msg.msjobj.CommonMO;

@EnableBinding(GoldsSource.class)
public class GoldsMsgService {

	@Autowired
	private GoldsSource goldsSource;

	public void withdraw(MemberGoldRecordDbo dbo) {
		CommonMO mo = new CommonMO();

		mo.setMsg("accounting");
		mo.setData(dbo);
		goldsSource.golds().send(MessageBuilder.withPayload(mo).build());
	}

}
