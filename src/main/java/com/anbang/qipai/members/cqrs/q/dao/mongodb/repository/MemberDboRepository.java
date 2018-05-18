package com.anbang.qipai.members.cqrs.q.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;

public interface MemberDboRepository extends MongoRepository<MemberDbo, String> {

}
