package com.anbang.qipai.members.cqrs.q.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.cqrs.q.dao.MemberScoreAccountDboDao;
import com.anbang.qipai.members.cqrs.q.dao.mongodb.repository.MemberScoreAccountDboRepository;
import com.anbang.qipai.members.cqrs.q.dbo.MemberScoreAccountDbo;

@Component
public class MongodbMemberScoreAccountDboDao implements MemberScoreAccountDboDao {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private MemberScoreAccountDboRepository repository;

	@Override
	public void save(MemberScoreAccountDbo accountDbo) {
		repository.save(accountDbo);
	}

	@Override
	public void update(String id, int balance) {
		mongoTemplate.updateFirst(new Query(Criteria.where("id").is(id)), new Update().set("balance", balance),
				MemberScoreAccountDbo.class);
	}

	@Override
	public MemberScoreAccountDbo findByMemberId(String memberId) {
		return repository.findOneByMemberId(memberId);
	}

}
