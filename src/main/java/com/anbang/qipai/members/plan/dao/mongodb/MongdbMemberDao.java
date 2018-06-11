package com.anbang.qipai.members.plan.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.plan.dao.MemberDao;
import com.anbang.qipai.members.plan.domain.Member;

@Component
public class MongdbMemberDao implements MemberDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Member findMember(Query query) {
		return mongoTemplate.findOne(query, Member.class);
	}

	@Override
	public void updateMember(Query query, Update update) {
		mongoTemplate.updateFirst(query, update, Member.class);
	}

}
