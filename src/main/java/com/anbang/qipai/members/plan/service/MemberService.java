package com.anbang.qipai.members.plan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.plan.dao.MemberDao;

@Service
public class MemberService {

	@Autowired
	private MemberDao memberDao;

	public MemberDbo findMemberById(String memberId) {
		return memberDao.findMemberById(memberId);
	}

	public void registerPhone(String memberId, String phone) {
		memberDao.updateMemberPhone(memberId, phone);
	}

	public void updateVIPTime(String memberId, long vipTime) {
		vipTime += System.currentTimeMillis();
		memberDao.updateMemberVIP(memberId, vipTime);
	}
}
