package com.anbang.qipai.members.plan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.members.cqrs.q.dao.MemberDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.plan.bean.MemberVIPRecord;
import com.anbang.qipai.members.plan.dao.MemberVIPRecordDao;

@Service
public class MemberVIPRecordService {

	@Autowired
	private MemberDboDao memberDboDao;

	@Autowired
	private MemberVIPRecordDao memberVIPRecordDao;

	public MemberVIPRecord addMemberVIPRecord(String memberId, long vipTime, String summary) {
		MemberDbo member = memberDboDao.findMemberById(memberId);
		MemberVIPRecord record = new MemberVIPRecord();
		record.setMemberId(memberId);
		record.setNickname(member.getNickname());
		record.setRechargeTime(System.currentTimeMillis());
		record.setSummary(summary);
		record.setVipTime(vipTime);
		memberVIPRecordDao.addMemberVIPRecord(record);
		return record;
	}
}
