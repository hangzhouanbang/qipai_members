package com.anbang.qipai.members.plan.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.plan.dao.MemberDao;

@Component
public class MongdbMemberDao implements MemberDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public MemberDbo findMember(Query query) {
		return mongoTemplate.findOne(query, MemberDbo.class);
	}

	@Override
	public void updateMember(Query query, Update update) {
		mongoTemplate.updateFirst(query, update, MemberDbo.class);
	}

}
