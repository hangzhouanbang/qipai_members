package com.anbang.qipai.members.cqrs.q.dao;

import java.util.List;

import org.springframework.data.domain.PageRequest;

import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldRecordDbo;

public interface MemberGoldRecordDboDao {

	void save(MemberGoldRecordDbo dbo);
	
	long getCount();

	List<MemberGoldRecordDbo> findMemberGoldRecords(String memberId,PageRequest pageRequest);
}
