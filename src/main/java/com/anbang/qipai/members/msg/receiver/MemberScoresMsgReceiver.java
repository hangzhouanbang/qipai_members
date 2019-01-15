package com.anbang.qipai.members.msg.receiver;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.members.cqrs.c.service.MemberScoreCmdService;
import com.anbang.qipai.members.cqrs.q.dbo.MemberScoreRecordDbo;
import com.anbang.qipai.members.cqrs.q.service.MemberScoreQueryService;
import com.anbang.qipai.members.msg.channel.sink.MemberScoresSink;
import com.anbang.qipai.members.msg.msjobj.CommonMO;
import com.anbang.qipai.members.msg.service.ScoresMsgService;
import com.dml.accounting.AccountingRecord;
import com.google.gson.Gson;

@EnableBinding(MemberScoresSink.class)
public class MemberScoresMsgReceiver {

	@Autowired
	private MemberScoreCmdService memberScoreCmdService;

	@Autowired
	private MemberScoreQueryService memberScoreQueryService;

	@Autowired
	private ScoresMsgService scoresMsgService;

	private Gson gson = new Gson();

	@StreamListener(MemberScoresSink.MEMBERSCORESACCOUNTING)
	public void memberScoreAccounting(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		if ("withdraw".equals(msg)) {
			Map data = gson.fromJson(json, Map.class);
			String memberId = (String) data.get("memberId");
			int amount = ((Double) data.get("amount")).intValue();
			String textSummary = (String) data.get("textSummary");
			try {
				AccountingRecord rcd = memberScoreCmdService.withdraw(memberId, amount, textSummary,
						System.currentTimeMillis());
				MemberScoreRecordDbo dbo = memberScoreQueryService.withdraw(memberId, rcd);
				// rcd发kafka
				scoresMsgService.withdraw(dbo);
			} catch (Exception e) {

			}
		}
		if ("givescoretomember".equals(msg)) {
			Map data = gson.fromJson(json, Map.class);
			String memberId = (String) data.get("memberId");
			int amount = ((Double) data.get("amount")).intValue();
			String textSummary = (String) data.get("textSummary");
			try {
				AccountingRecord rcd = memberScoreCmdService.giveScoreToMember(memberId, amount, textSummary,
						System.currentTimeMillis());
				MemberScoreRecordDbo dbo = memberScoreQueryService.withdraw(memberId, rcd);
				// rcd发kafka
				scoresMsgService.withdraw(dbo);
			} catch (Exception e) {

			}
		}
	}
}
