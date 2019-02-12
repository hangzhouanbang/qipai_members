package com.anbang.qipai.members.msg.receiver;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.members.msg.channel.sink.MemberVIPSink;
import com.anbang.qipai.members.msg.msjobj.CommonMO;
import com.anbang.qipai.members.msg.service.MemberVIPRecordMsgService;
import com.anbang.qipai.members.msg.service.MembersMsgService;
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

	@Autowired
	private MembersMsgService membersMsgService;

	private Gson gson = new Gson();

	@StreamListener(MemberVIPSink.MEMBERVIPACCOUNTING)
	public void memberVIP(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		if ("reward vip".equals(msg)) {
			Map data = gson.fromJson(json, Map.class);
			String memberId = (String) data.get("memberId");
			long vipTime = ((Double) data.get("vipTime")).longValue();
			String summary = (String) data.get("summary");
			MemberDbo member = memberAuthQueryService.rechargeVip(memberId, vipTime);
			membersMsgService.rechargeVip(member);
			MemberVIPRecord record = memberVIPRecordService.addMemberVIPRecord(memberId, vipTime, summary);
			memberVIPRecordMsgService.record(record);
		}
	}
}
