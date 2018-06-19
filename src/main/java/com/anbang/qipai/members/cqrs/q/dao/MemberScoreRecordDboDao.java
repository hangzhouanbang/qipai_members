package com.anbang.qipai.members.cqrs.q.dao;

import java.util.List;

import org.springframework.data.domain.PageRequest;

import com.anbang.qipai.members.cqrs.q.dbo.MemberScoreRecordDbo;

public interface MemberScoreRecordDboDao {
	void save(MemberScoreRecordDbo dbo);

	long getCount();

	List<MemberScoreRecordDbo> findMemberScoreRecords(String accountId, PageRequest pageRequest);
}
