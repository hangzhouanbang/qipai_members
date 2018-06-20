package com.anbang.qipai.members.cqrs.q.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.members.cqrs.q.dbo.MemberScoreAccountDbo;

public interface MemberScoreAccountDboRepository extends MongoRepository<MemberScoreAccountDbo, String> {
	MemberScoreAccountDbo findOneByMemberId(String memberId);
}
