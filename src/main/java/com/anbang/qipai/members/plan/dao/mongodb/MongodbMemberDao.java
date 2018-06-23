package com.anbang.qipai.members.plan.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.plan.dao.MemberDao;

@Component
public class MongodbMemberDao implements MemberDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<MemberDbo> findMember(int page, int size) {
		Query query = new Query();
		query.skip((page - 1) * size);
		query.limit(size);
		return mongoTemplate.find(query, MemberDbo.class);
	}

	@Override
	public long getAmount() {
		Query query = new Query();
		return mongoTemplate.count(query, MemberDbo.class);
	}

	@Override
	public MemberDbo findMemberById(String memberId) {
		Query query = new Query(Criteria.where("id").is(memberId));
		return mongoTemplate.findOne(query, MemberDbo.class);
	}

	@Override
	public void updateMemberPhone(String memberId, String phone) {
		Query query = new Query(Criteria.where("id").is(memberId));
		Update update = new Update();
		update.set("phone", phone);
		mongoTemplate.updateFirst(query, update, MemberDbo.class);
	}

	@Override
	public void updateMemberVIP(MemberDbo member) {
		Query query = new Query(Criteria.where("id").is(member.getId()));
		Update update = new Update();
		update.set("vipEndTime", member.getVipEndTime());
		update.set("vip", member.getVip());
		update.set("vipLevel", member.getVipLevel());
		update.set("vipScore", member.getVipScore());
		update.set("RMB", member.getRMB());
		mongoTemplate.updateFirst(query, update, MemberDbo.class);
	}

	@Override
	public void resetVip(MemberDbo member) {
		Query query = new Query(Criteria.where("id").is(member.getId()));
		Update update = new Update();
		update.set("vip", member.getVip());
		mongoTemplate.updateFirst(query, update, MemberDbo.class);
	}

	@Override
	public void update_score_gold(String memberid,MemberDbo memberDbo) {
		mongoTemplate.updateMulti(new Query(Criteria.where("id").is(memberid)),new Update().set("score",memberDbo.getScore())
				.set("gold",memberDbo.getGold()), MemberDbo.class);
	}

}
