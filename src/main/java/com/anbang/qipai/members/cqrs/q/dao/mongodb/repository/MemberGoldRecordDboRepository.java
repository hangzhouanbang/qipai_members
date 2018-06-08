package com.anbang.qipai.members.cqrs.q.dao.mongodb.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldRecordDbo;

public interface MemberGoldRecordDboRepository extends MongoRepository<MemberGoldRecordDbo, String> {

	public List<MemberGoldRecordDbo> findByAccountId(String accountId,PageRequest pageRequest);
}
