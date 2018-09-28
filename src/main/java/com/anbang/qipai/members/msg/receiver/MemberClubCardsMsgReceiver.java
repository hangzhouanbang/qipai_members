package com.anbang.qipai.members.msg.receiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.members.msg.channel.sink.MemberClubCardsSink;
import com.anbang.qipai.members.msg.msjobj.CommonMO;
import com.anbang.qipai.members.plan.bean.MemberClubCard;
import com.anbang.qipai.members.plan.service.ClubCardService;
import com.google.gson.Gson;

/**
*@author   created by hanzhuofan  2018.09.26
*/
@EnableBinding(MemberClubCardsSink.class)
public class MemberClubCardsMsgReceiver {
	
	@Autowired
	private ClubCardService clubCardService;
	
	private Gson gson = new Gson();
	
	@StreamListener(MemberClubCardsSink.channel)
	public void memberClubCard(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		if (MemberClubCardsSink.addClubCard.equals(msg)) {
			MemberClubCard card = gson.fromJson(json, MemberClubCard.class);
			clubCardService.addClubCard(card);
		}
		if (MemberClubCardsSink.deleteClubCard.equals(msg)) {
			String[] clubCardIds = gson.fromJson(json, String[].class);
			clubCardService.deleteClubCards(clubCardIds);
		}
		if (MemberClubCardsSink.updateClubCard.equals(msg)) {
			MemberClubCard card = gson.fromJson(json, MemberClubCard.class);
			clubCardService.updateClubCard(card);
		}
	}
}
