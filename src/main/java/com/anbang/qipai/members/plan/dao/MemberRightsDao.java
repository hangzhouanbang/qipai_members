package com.anbang.qipai.members.plan.dao;

import com.anbang.qipai.members.plan.domain.MemberRights;

public interface MemberRightsDao {

	void savePlanShipConfiguration(MemberRights createMemberConfiguration);

	MemberRights find();
	
	MemberRights savevipuser(MemberRights vipuser);

}
