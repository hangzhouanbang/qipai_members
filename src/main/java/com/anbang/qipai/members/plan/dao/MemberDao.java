package com.anbang.qipai.members.plan.dao;

import java.util.List;

import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;

public interface MemberDao {
	List<MemberDbo> findMemberByVip(int page, int size,boolean vip);

	long getAmountByVip(boolean vip);

	MemberDbo findMemberById(String memberId);

	void updateMemberPhone(String memberId, String phone);

	void updateMemberVIP(MemberDbo member);

	void agentUpdateMemberVip(MemberDbo member);

	void resetVip(MemberDbo member);
	
	void verifyUser(String memberId, String realName, String IDcard, boolean verify);
	
	void updateMemberLogin(String memberId,String state,String loginIp,long loginTime);
	
	void updateMemberLogout(String memberId,String state,long onlineTime);
}
