package com.anbang.qipai.members.plan.dao;

import java.util.List;

import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;

public interface MemberDao {
	List<MemberDbo> findMemberByVip(int page, int size,boolean vip);

	long getAmountByVip(boolean vip);

	MemberDbo findMemberById(String memberId);

	boolean updateMemberPhone(String memberId, String phone);

	boolean updateMemberVIP(MemberDbo member);

	boolean agentUpdateMemberVip(MemberDbo member);

	boolean resetVip(MemberDbo member);
	
	void verifyUser(String memberId, String realName, String IDcard, boolean verify);
}
