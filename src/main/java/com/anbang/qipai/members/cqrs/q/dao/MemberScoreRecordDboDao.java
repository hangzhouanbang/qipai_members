package com.anbang.qipai.members.cqrs.q.dao;

import java.util.List;

import com.anbang.qipai.members.cqrs.q.dbo.MemberScoreRecordDbo;

public interface MemberScoreRecordDboDao {

	void save(MemberScoreRecordDbo dbo);

	long getCountByMemberId(String memberId);

	List<MemberScoreRecordDbo> findMemberScoreRecordByMemberId(String memberId, int page, int size);
}
