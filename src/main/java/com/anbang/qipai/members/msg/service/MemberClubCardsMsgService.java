package com.anbang.qipai.members.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.members.msg.channel.MemberClubCardsSoure;
import com.anbang.qipai.members.msg.msjobj.CommonMO;
import com.anbang.qipai.members.plan.bean.MemberClubCard;

@EnableBinding(MemberClubCardsSoure.class)
public class MemberClubCardsMsgService {

	@Autowired
	private MemberClubCardsSoure memberClubCardsSoure;

	public void addClubCard(MemberClubCard card) {
		CommonMO mo = new CommonMO();
		mo.setMsg("add memberclubcard");
		mo.setData(card);
		memberClubCardsSoure.memberClubCards().send(MessageBuilder.withPayload(mo).build());
	}

	public void deleteClubCards(String[] clubCardIds) {
		CommonMO mo = new CommonMO();
		mo.setMsg("delete memberclubcard");
		mo.setData(clubCardIds);
		memberClubCardsSoure.memberClubCards().send(MessageBuilder.withPayload(mo).build());
	}

	public void updateClubCards(MemberClubCard clubCard) {
		CommonMO mo = new CommonMO();
		mo.setMsg("update memberclubcard");
		mo.setData(clubCard);
		memberClubCardsSoure.memberClubCards().send(MessageBuilder.withPayload(mo).build());
	}
}
