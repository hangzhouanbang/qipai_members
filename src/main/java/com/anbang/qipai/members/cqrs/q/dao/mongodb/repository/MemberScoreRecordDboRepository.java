package com.anbang.qipai.members.cqrs.q.dao.mongodb.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.members.cqrs.q.dbo.MemberScoreRecordDbo;

public interface MemberScoreRecordDboRepository extends MongoRepository<MemberScoreRecordDbo, String> {
	public List<MemberScoreRecordDbo> findByMemberId(String accountId, Pageable pageRequest);
}
