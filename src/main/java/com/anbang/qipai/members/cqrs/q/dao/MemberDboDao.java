package com.anbang.qipai.members.cqrs.q.dao;

import java.util.List;

import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberRights;

public interface MemberDboDao {

	void save(MemberDbo memberDbo);

	void update(String memberId, String nickname, String headimgurl, String gender);

	void updatePlanMembersRights(MemberRights memberRights);

	void updateVipMembersRights(MemberRights memberRights);

	List<MemberDbo> findMemberByVip(int page, int size, boolean vip);

	long getAmountByVip(boolean vip);

	MemberDbo findMemberById(String memberId);

	void updateMemberPhone(String memberId, String phone);

	void updateMemberVIP(String memberId, boolean vip);

	void updateMemberBindAgent(String memberId, String agentId, boolean bindAgent);

	void removeMemberBindAgent(String memberId);

	void updateMemberVipScore(String memberId, double vipScore);

	void updateMemberVipLevel(String memberId, int vipLevel);

	void updateMemberVipEndTime(String memberId, long vipEndTime);

	void updateMemberRealUser(String memberId, String realName, String IDcard, boolean verify);

	void updateMemberCost(String memberId, double cost);

	void updateMemberGold(String memberId, int gold);

	void updateMemberScore(String memberId, int score);
}
