package com.anbang.qipai.members.msg.receiver;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.members.cqrs.c.service.MemberGoldCmdService;
import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldRecordDbo;
import com.anbang.qipai.members.cqrs.q.service.MemberGoldQueryService;
import com.anbang.qipai.members.msg.channel.sink.MemberGoldsSink;
import com.anbang.qipai.members.msg.msjobj.CommonMO;
import com.anbang.qipai.members.msg.service.GoldsMsgService;
import com.dml.accounting.AccountingRecord;
import com.google.gson.Gson;

@EnableBinding(MemberGoldsSink.class)
public class MemberGoldsMsgReceiver {
	@Autowired
	private MemberGoldCmdService memberGoldCmdService;

	@Autowired
	private MemberGoldQueryService memberGoldQueryService;

	@Autowired
	private GoldsMsgService goldsMsgService;

	private Gson gson = new Gson();

	@StreamListener(MemberGoldsSink.MEMBERGOLDSACCOUNTING)
	public void memberGoldAccounting(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		if ("withdraw".equals(msg)) {
			Map data = gson.fromJson(json, Map.class);
			String memberId = (String) data.get("memberId");
			int amount = ((Double) data.get("amount")).intValue();
			String textSummary = (String) data.get("textSummary");
			try {
				AccountingRecord rcd = memberGoldCmdService.withdraw(memberId, amount, textSummary,
						System.currentTimeMillis());
				MemberGoldRecordDbo dbo = memberGoldQueryService.withdraw(memberId, rcd);
				// rcd发kafka
				goldsMsgService.withdraw(dbo);
			} catch (Exception e) {

			}
		}
		if ("givegoldtomember".equals(msg)) {
			Map data = gson.fromJson(json, Map.class);
			String memberId = (String) data.get("memberId");
			int amount = ((Double) data.get("amount")).intValue();
			String textSummary = (String) data.get("textSummary");
			try {
				AccountingRecord rcd = memberGoldCmdService.giveGoldToMember(memberId, amount, textSummary,
						System.currentTimeMillis());
				MemberGoldRecordDbo dbo = memberGoldQueryService.withdraw(memberId, rcd);
				// rcd发kafka
				goldsMsgService.withdraw(dbo);
			} catch (Exception e) {

			}
		}
	}
}
