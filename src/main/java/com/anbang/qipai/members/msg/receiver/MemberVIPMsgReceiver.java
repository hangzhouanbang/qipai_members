package com.anbang.qipai.members.msg.receiver;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.members.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.members.msg.channel.sink.MemberVIPSink;
import com.anbang.qipai.members.msg.msjobj.CommonMO;
import com.anbang.qipai.members.msg.service.MemberVIPRecordMsgService;
import com.anbang.qipai.members.plan.bean.MemberVIPRecord;
import com.anbang.qipai.members.plan.service.MemberVIPRecordService;
import com.google.gson.Gson;

@EnableBinding(MemberVIPSink.class)
public class MemberVIPMsgReceiver {

	@Autowired
	private MemberVIPRecordService memberVIPRecordService;

	@Autowired
	private MemberVIPRecordMsgService memberVIPRecordMsgService;

	@Autowired
	private MemberAuthQueryService memberAuthQueryService;

	private Gson gson = new Gson();

	@StreamListener(MemberVIPSink.MEMBERVIPACCOUNTING)
	public void memberScoreAccounting(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		if ("recharge vip".equals(msg)) {
			Map data = gson.fromJson(json, Map.class);
			String memberId = (String) data.get("memberId");
			long vipTime = ((Long) data.get("vipTime")).intValue();
			String summary = (String) data.get("summary");
			memberAuthQueryService.rechargeVip(memberId, vipTime);
			MemberVIPRecord record = memberVIPRecordService.addMemberVIPRecord(memberId, vipTime, summary);
			memberVIPRecordMsgService.record(record);
		}
	}
}
