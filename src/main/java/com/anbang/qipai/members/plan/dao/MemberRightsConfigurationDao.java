package com.anbang.qipai.members.plan.dao;

import com.anbang.qipai.members.plan.domain.MemberRightsConfiguration;

public interface MemberRightsConfigurationDao {

	void save(MemberRightsConfiguration createMemberConfiguration);

	MemberRightsConfiguration find();

	void setPlanMembersRights(float planGrowIntegralSpeed, int goldForNewNember);

	void setVipMembersRights(float vipGrowGradeSpeed);

	void updatePlanMembersRights(int signGoldNumber, int goldForNewNember, int inviteIntegralNumber,
			float planGrowIntegralSpeed);

	void updateVipMembersRights(int signGoldNumber, int inviteIntegralNumber, float vipGrowIntegralSpeed,
			float vipGrowGradeSpeed);
}
