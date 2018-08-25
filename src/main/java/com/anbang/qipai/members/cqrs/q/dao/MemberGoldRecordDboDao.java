package com.anbang.qipai.members.cqrs.q.dao;

import java.util.List;

import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldRecordDbo;

public interface MemberGoldRecordDboDao {

	void save(MemberGoldRecordDbo dbo);

	long getCountByMemberId(String memberId);

	List<MemberGoldRecordDbo> findMemberGoldRecordByMemberId(String memberId, int page, int size);
}
