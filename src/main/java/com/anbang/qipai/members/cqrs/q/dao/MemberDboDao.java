package com.anbang.qipai.members.cqrs.q.dao;

import java.util.List;

import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberRights;
import com.anbang.qipai.members.plan.bean.AgentBindWay;

public interface MemberDboDao {

	void save(MemberDbo memberDbo);

	void update(String memberId, String nickname, String headimgurl, String gender);

	void updatePlanMembersRights(MemberRights memberRights);

	void updateVipMembersRights(MemberRights memberRights);

	List<MemberDbo> findMemberByVip(int page, int size, boolean vip);

	long getAmountByVip(boolean vip);

	MemberDbo findMemberById(String memberId);

	MemberDbo findMemberByPhone(String phone);

	void updateMemberPhone(String memberId, String phone);

	void updateMemberReqIP(String memberId, String reqIP);

	void updateMemberVIP(String memberId, boolean vip);

	void updateMemberBindAgent(String memberId, String agentId, boolean bindAgent, AgentBindWay agentBindWay);

	void removeMemberBindAgent(String memberId);

	void updateMemberVipScore(String memberId, double vipScore);

	void updateMemberVipLevel(String memberId, int vipLevel);

	void updateMemberVipEndTime(String memberId, long vipEndTime);

	void updateMemberRealUser(String memberId, String realName, String IDcard, boolean verify);

	void updateMemberCost(String memberId, double cost);

	void updateMemberGold(String memberId, int gold);

	void updateMemberScore(String memberId, int score);

	/**
	 * 修改hasBindAgent(是否绑定过推广员) 设置bindAgent为true
	 * 
	 * @param memberId
	 * @param agentId
	 * @param hasBindAgent
	 */
	void updateMemberHasBindAgent(String memberId, String agentId, boolean hasBindAgent);
}
