package com.anbang.qipai.members.cqrs.q.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.cqrs.q.dao.MemberGoldAccountDboDao;
import com.anbang.qipai.members.cqrs.q.dao.mongodb.repository.MemberGoldAccountDboRepository;
import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldAccountDbo;

@Component
public class MongodbMemberGoldAccountDboDao implements MemberGoldAccountDboDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private MemberGoldAccountDboRepository repository;

	@Override
	public void save(MemberGoldAccountDbo accountDbo) {
		repository.save(accountDbo);
	}

	@Override
	public void update(String id, int balance) {
		mongoTemplate.updateFirst(new Query(Criteria.where("id").is(id)), new Update().set("balance", balance),
				MemberGoldAccountDbo.class);
	}

}
