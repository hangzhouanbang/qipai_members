package com.anbang.qipai.members.cqrs.q.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldRecordDbo;

public interface MemberGoldRecordDboRepository extends MongoRepository<MemberGoldRecordDbo, String> {

}
