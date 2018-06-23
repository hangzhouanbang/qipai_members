package com.anbang.qipai.members.plan.dao;

import java.util.List;

import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;

public interface MemberDao {
	List<MemberDbo> findMember(int page, int size);
	
	void update_score_gold(String memberid,MemberDbo memberDbo);

	long getAmount();

	MemberDbo findMemberById(String memberId);

	void updateMemberPhone(String memberId, String phone);

	void updateMemberVIP(MemberDbo member);
	
	void resetVip(MemberDbo member);
}
