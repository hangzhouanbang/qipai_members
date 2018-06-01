package com.anbang.qipai.members.cqrs.q.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldAccountDbo;

public interface MemberGoldAccountDboRepository extends MongoRepository<MemberGoldAccountDbo, String> {

	MemberGoldAccountDbo findOneByMemberId(String memberId);

}
