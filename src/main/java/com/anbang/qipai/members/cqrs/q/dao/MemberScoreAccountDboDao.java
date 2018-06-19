package com.anbang.qipai.members.cqrs.q.dao;

import com.anbang.qipai.members.cqrs.q.dbo.MemberScoreAccountDbo;

public interface MemberScoreAccountDboDao {
	void save(MemberScoreAccountDbo accountDbo);

	void update(String id, int balance);

	MemberScoreAccountDbo findByMemberId(String memberId);

}
