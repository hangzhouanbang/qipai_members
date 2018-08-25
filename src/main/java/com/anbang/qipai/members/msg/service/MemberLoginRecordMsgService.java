package com.anbang.qipai.members.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.members.msg.channel.source.MemberLoginRecordSource;
import com.anbang.qipai.members.msg.msjobj.CommonMO;
import com.anbang.qipai.members.plan.bean.MemberLoginRecord;

@EnableBinding(MemberLoginRecordSource.class)
public class MemberLoginRecordMsgService {

	@Autowired
	private MemberLoginRecordSource memberLoginRecordSource;

	public void memberLoginRecord(MemberLoginRecord record) {
		CommonMO mo = new CommonMO();
		mo.setMsg("member login");
		mo.setData(record);
		memberLoginRecordSource.memberLoginRecord().send(MessageBuilder.withPayload(mo).build());
	}

	public void memberLogoutRecord(MemberLoginRecord record) {
		CommonMO mo = new CommonMO();
		mo.setMsg("member logout");
		mo.setData(record);
		memberLoginRecordSource.memberLoginRecord().send(MessageBuilder.withPayload(mo).build());
	}
}
