package com.anbang.qipai.members.cqrs.q.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.cqrs.q.dao.MemberDboDao;
import com.anbang.qipai.members.cqrs.q.dao.mongodb.repository.MemberDboRepository;
import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberRights;

@Component
public class MongodbMemberDboDao implements MemberDboDao {

	@Autowired
	private MongoTemplate mongoTemplate;

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

	@Override
	public MemberDbo findById(String id) {
		return repository.findOne(id);
	}

	@Override
	public void updatePlanMembersRights(MemberRights memberRights) {
		mongoTemplate.updateMulti(new Query(Criteria.where("vip").is(false)), new Update().set("rights", memberRights),
				MemberDbo.class);
	}

	@Override
	public void updateVipMembersRights(MemberRights memberRights) {
		mongoTemplate.updateMulti(new Query(Criteria.where("vip").is(true)), new Update().set("rights", memberRights),
				MemberDbo.class);
	}

}
