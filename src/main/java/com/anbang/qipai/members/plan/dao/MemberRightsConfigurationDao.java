package com.anbang.qipai.members.plan.dao;

import com.anbang.qipai.members.plan.bean.MemberRightsConfiguration;

public interface MemberRightsConfigurationDao {

	void save(MemberRightsConfiguration createMemberConfiguration);

	MemberRightsConfiguration find();

	void setPlanMembersRights(float planGrowIntegralSpeed, int goldForAgentInvite, int goldForNewNember);

	void setVipMembersRights(float vipGrowGradeSpeed);

	void updatePlanMembersRights(int signGoldNumber, int goldForNewNember, int inviteIntegralNumber,
			int goldForAgentInvite, float planGrowIntegralSpeed);

	void updateVipMembersRights(int signGoldNumber, int inviteIntegralNumber, float vipGrowIntegralSpeed,
			float vipGrowGradeSpeed);
}
