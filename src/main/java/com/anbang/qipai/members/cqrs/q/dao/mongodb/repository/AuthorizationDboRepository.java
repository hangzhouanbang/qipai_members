package com.anbang.qipai.members.cqrs.q.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.members.cqrs.q.dbo.AuthorizationDbo;

public interface AuthorizationDboRepository extends MongoRepository<AuthorizationDbo, String> {

	AuthorizationDbo findOneByThirdAuthAndPublisherAndUuid(boolean thirdAuth, String publisher, String uuid);

	AuthorizationDbo findOneByThirdAuthAndPublisherAndMemberId(boolean thirdAuth, String publisher, String memberId);

}
