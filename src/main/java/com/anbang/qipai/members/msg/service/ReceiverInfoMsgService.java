package com.anbang.qipai.members.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.members.cqrs.q.dbo.ReceiverInfoDbo;
import com.anbang.qipai.members.msg.channel.source.ReceiverInfoSource;
import com.anbang.qipai.members.msg.msjobj.CommonMO;

@EnableBinding(ReceiverInfoSource.class)
public class ReceiverInfoMsgService {

	@Autowired
	private ReceiverInfoSource receiverInfoSource;

	public void recordReceiverInfo(ReceiverInfoDbo dbo) {
		CommonMO mo = new CommonMO();
		mo.setMsg("add info");
		mo.setData(dbo);
		receiverInfoSource.receiverInfo().send(MessageBuilder.withPayload(mo).build());
	}

	public void updateReceiverInfo(ReceiverInfoDbo dbo) {
		CommonMO mo = new CommonMO();
		mo.setMsg("update info");
		mo.setData(dbo);
		receiverInfoSource.receiverInfo().send(MessageBuilder.withPayload(mo).build());
	}
}
