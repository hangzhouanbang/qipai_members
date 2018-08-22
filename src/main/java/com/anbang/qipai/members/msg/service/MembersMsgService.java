package com.anbang.qipai.members.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.msg.channel.MembersSource;
import com.anbang.qipai.members.msg.msjobj.CommonMO;

@EnableBinding(MembersSource.class)
public class MembersMsgService {

	@Autowired
	private MembersSource membersSource;

	public void createMember(MemberDbo member) {
		CommonMO mo = new CommonMO();
		mo.setMsg("newMember");
		mo.setData(member);
		membersSource.members().send(MessageBuilder.withPayload(mo).build());
	}

	public void updateMemberPhone(MemberDbo member) {
		CommonMO mo = new CommonMO();
		mo.setMsg("update member phone");
		mo.setData(member);
		membersSource.members().send(MessageBuilder.withPayload(mo).build());
	}

	public void updateMemberVip(MemberDbo member) {
		CommonMO mo = new CommonMO();
		mo.setMsg("update member vip");
		mo.setData(member);
		membersSource.members().send(MessageBuilder.withPayload(mo).build());
	}

	public void resetMemberVip(MemberDbo member) {
		CommonMO mo = new CommonMO();
		mo.setMsg("reset member vip");
		mo.setData(member);
		membersSource.members().send(MessageBuilder.withPayload(mo).build());
	}

	public void updateMemberLogin(MemberDbo member) {
		CommonMO mo = new CommonMO();
		mo.setMsg("member login");
		mo.setData(member);
		membersSource.members().send(MessageBuilder.withPayload(mo).build());
	}

	public void verifyMember(MemberDbo member) {
		CommonMO mo = new CommonMO();
		mo.setMsg("verify member");
		mo.setData(member);
		membersSource.members().send(MessageBuilder.withPayload(mo).build());
	}
}
