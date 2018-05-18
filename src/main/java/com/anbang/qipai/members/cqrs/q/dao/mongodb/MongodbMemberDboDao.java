package com.anbang.qipai.members.cqrs.q.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.anbang.qipai.members.cqrs.q.dao.MemberDboDao;
import com.anbang.qipai.members.cqrs.q.dao.mongodb.repository.MemberDboRepository;
import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.highto.framework.data.mongodb.MongodbDaoBase;

public class MongodbMemberDboDao extends MongodbDaoBase implements MemberDboDao {

	@Autowired
	private MemberDboRepository repository;

	@Override
	public void save(MemberDbo memberDbo) {
		repository.save(memberDbo);
	}

	@Override
	public void update(String memberId, String nickname, String headimgurl) {
		mongoTemplate.updateFirst(new Query(Criteria.where("id").is(memberId)),
				new Update().set("nickname", nickname).set("headimgurl", headimgurl), MemberDbo.class);
	}

}
