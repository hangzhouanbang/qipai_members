package com.anbang.qipai.members.plan.dao;

import java.util.List;

import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;

public interface MemberDao {
	List<MemberDbo> findMember(int page, int size);

	long getAmount();

	MemberDbo findMemberById(String memberId);

	boolean updateMemberPhone(String memberId, String phone);

	boolean updateMemberVIP(MemberDbo member);

	boolean updateMemberVipEndTime(String memberId, long vipEndTime);

	boolean resetVip(MemberDbo member);
}
