package com.anbang.qipai.members.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.members.msg.channel.source.MemberLoginLimitRecordSource;
import com.anbang.qipai.members.msg.msjobj.CommonMO;
import com.anbang.qipai.members.plan.bean.MemberLoginLimitRecord;

@EnableBinding(MemberLoginLimitRecordSource.class)
public class MemberLoginLimitRecordMsgService {
	@Autowired
	private MemberLoginLimitRecordSource memberLoginLimitRecordSource;

	public void addrecord(MemberLoginLimitRecord record) {
		CommonMO mo = new CommonMO();

		mo.setMsg("add record");
		mo.setData(record);
		memberLoginLimitRecordSource.memberLoginLimitRecord().send(MessageBuilder.withPayload(mo).build());
	}

	public void deleterecords(String[] recordIds) {
		CommonMO mo = new CommonMO();

		mo.setMsg("delete records");
		mo.setData(recordIds);
		memberLoginLimitRecordSource.memberLoginLimitRecord().send(MessageBuilder.withPayload(mo).build());
	}
}
