package com.anbang.qipai.members.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.members.cqrs.q.dbo.MemberScoreRecordDbo;
import com.anbang.qipai.members.msg.channel.source.MemberScoresSource;
import com.anbang.qipai.members.msg.msjobj.CommonMO;

@EnableBinding(MemberScoresSource.class)
public class ScoresMsgService {
	@Autowired
	private MemberScoresSource memberScoresSource;

	public void withdraw(MemberScoreRecordDbo dbo) {
		CommonMO mo = new CommonMO();

		mo.setMsg("accounting");
		mo.setData(dbo);
		memberScoresSource.memberScores().send(MessageBuilder.withPayload(mo).build());
	}

}
