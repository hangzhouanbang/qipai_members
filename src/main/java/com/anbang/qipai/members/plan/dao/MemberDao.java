package com.anbang.qipai.members.plan.dao;

import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;

public interface MemberDao {

	MemberDbo findMemberById(String memberId);

	void updateMemberPhone(String memberId, String phone);
	
	void updateMemberVIP(String memberId, long vipTime);
}
