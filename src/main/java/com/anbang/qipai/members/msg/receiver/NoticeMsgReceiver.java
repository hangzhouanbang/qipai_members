package com.anbang.qipai.members.msg.receiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.members.config.MemberOnlineState;
import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.members.msg.channel.sink.NoticeSink;
import com.anbang.qipai.members.msg.msjobj.CommonMO;
import com.anbang.qipai.members.msg.service.MemberLoginRecordMsgService;
import com.anbang.qipai.members.msg.service.MembersMsgService;
import com.anbang.qipai.members.plan.bean.MemberLoginRecord;
import com.anbang.qipai.members.plan.service.MemberLoginRecordService;
import com.google.gson.Gson;

@EnableBinding(NoticeSink.class)
public class NoticeMsgReceiver {

	@Autowired
	private MemberAuthQueryService memberAuthQueryService;

	@Autowired
	private MemberLoginRecordService memberLoginRecordService;

	@Autowired
	private MemberLoginRecordMsgService memberLoginRecordMsgService;

	@Autowired
	private MembersMsgService membersMsgService;

	private Gson gson = new Gson();

	@StreamListener(NoticeSink.NOTICE)
	public void logout(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		if ("member logout".equals(msg)) {
			String memberId = gson.fromJson(json, String.class);
			// 更新玩家在线状态
			MemberDbo member = memberAuthQueryService.updateMemberOnlineState(memberId, MemberOnlineState.OFFLINE);
			membersMsgService.updateMemberOnlineState(member);

			// 更新玩家登录记录
			MemberLoginRecord record = memberLoginRecordService.findRecentRecordByMemberId(memberId);
			long onlineTime = System.currentTimeMillis() - record.getLoginTime();
			record.setOnlineTime(onlineTime);
			memberLoginRecordService.updateOnlineTimeById(record.getId(), onlineTime);
			memberLoginRecordMsgService.updateMemberOnlineRecord(record);
		}

		if ("update member online".equals(msg)) {
			String memberId = gson.fromJson(json, String.class);
			// 更新玩家登录记录
			MemberLoginRecord record = memberLoginRecordService.findRecentRecordByMemberId(memberId);
			long onlineTime = System.currentTimeMillis() - record.getLoginTime();
			record.setOnlineTime(onlineTime);
			memberLoginRecordService.updateOnlineTimeById(record.getId(), onlineTime);
			memberLoginRecordMsgService.updateMemberOnlineRecord(record);
		}
	}
}
