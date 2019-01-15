package com.anbang.qipai.members.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.members.msg.channel.source.MemberVIPRecordSource;
import com.anbang.qipai.members.msg.msjobj.CommonMO;
import com.anbang.qipai.members.plan.bean.MemberVIPRecord;

@EnableBinding(MemberVIPRecordSource.class)
public class MemberVIPRecordMsgService {
	@Autowired
	private MemberVIPRecordSource memberVIPRecordSource;

	public void record(MemberVIPRecord record) {
		CommonMO mo = new CommonMO();
		mo.setMsg("recharge vip");
		mo.setData(record);
		memberVIPRecordSource.memberVIPRecord().send(MessageBuilder.withPayload(mo).build());
	}
}
