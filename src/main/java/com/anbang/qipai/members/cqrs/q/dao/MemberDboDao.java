package com.anbang.qipai.members.cqrs.q.dao;

import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberRights;

public interface MemberDboDao {

	void save(MemberDbo memberDbo);

	void update(String memberId, String nickname, String headimgurl);

	MemberDbo findById(String id);

	void updatePlanMembersRights(MemberRights memberRights);

	void updateVipMembersRights(MemberRights memberRights);

}
