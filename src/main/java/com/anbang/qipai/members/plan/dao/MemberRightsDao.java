package com.anbang.qipai.members.plan.dao;

import com.anbang.qipai.members.plan.domain.MemberRights;

public interface MemberRightsDao {

	void savePlanShipConfiguration(MemberRights createMemberConfiguration);

	MemberRights find();
	
	MemberRights savevipuser(MemberRights vipuser);
	
	void setPlanMembersRights(float planGrowIntegralSpeed, int goldForNewNember);

	void setVipMembersRights(float vipGrowGradeSpeed);
	
	void updatePlanMembersRights(int signGoldNumber, int goldForNewNember, int shareIntegralNumber,
			int shareGoldNumber,int inviteIntegralNumber,float planGrowIntegralSpeed);

	void updateVipMembersRights(int signGoldNumber,int shareIntegralNumber,int shareGoldNumber,
			int inviteIntegralNumber,float vipGrowIntegralSpeed,float vipGrowGradeSpeed);
}
