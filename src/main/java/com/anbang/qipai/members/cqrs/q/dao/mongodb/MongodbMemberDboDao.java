package com.anbang.qipai.members.cqrs.q.dao.mongodb;

import java.util.List;

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
	public void update(String memberId, String nickname, String headimgurl, String gender) {
		mongoTemplate.updateFirst(new Query(Criteria.where("id").is(memberId)),
				new Update().set("nickname", nickname).set("headimgurl", headimgurl).set("gender", gender),
				MemberDbo.class);
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

	@Override
	public List<MemberDbo> findMemberByVip(int page, int size, boolean vip) {
		Query query = new Query(Criteria.where("vip").is(vip));
		query.skip((page - 1) * size);
		query.limit(size);
		return mongoTemplate.find(query, MemberDbo.class);
	}

	@Override
	public long getAmountByVip(boolean vip) {
		Query query = new Query(Criteria.where("vip").is(vip));
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
	public void updateMemberVIP(String memberId, boolean vip) {
		Query query = new Query(Criteria.where("id").is(memberId));
		Update update = new Update();
		update.set("vip", vip);
		mongoTemplate.updateFirst(query, update, MemberDbo.class);
	}

	@Override
	public void updateMemberRealUser(String memberId, String realName, String IDcard, boolean verify) {
		Query query = new Query(Criteria.where("id").is(memberId));
		Update update = new Update();
		update.set("realName", realName);
		update.set("IDcard", IDcard);
		update.set("verifyUser", verify);
		mongoTemplate.updateFirst(query, update, MemberDbo.class);
	}

	@Override
	public void updateMemberVipEndTime(String memberId, long vipEndTime) {
		Query query = new Query(Criteria.where("id").is(memberId));
		Update update = new Update();
		update.set("vipEndTime", vipEndTime);
		mongoTemplate.updateFirst(query, update, MemberDbo.class);
	}

	@Override
	public void updateMemberVipScore(String memberId, double vipScore) {
		Query query = new Query(Criteria.where("id").is(memberId));
		Update update = new Update();
		update.set("vipScore", vipScore);
		mongoTemplate.updateFirst(query, update, MemberDbo.class);
	}

	@Override
	public void updateMemberVipLevel(String memberId, int vipLevel) {
		Query query = new Query(Criteria.where("id").is(memberId));
		Update update = new Update();
		update.set("vipLevel", vipLevel);
		mongoTemplate.updateFirst(query, update, MemberDbo.class);
	}

	@Override
	public void updateMemberCost(String memberId, double cost) {
		Query query = new Query(Criteria.where("id").is(memberId));
		Update update = new Update();
		update.set("cost", cost);
		mongoTemplate.updateFirst(query, update, MemberDbo.class);
	}

	@Override
	public void updateMemberGold(String memberId, int gold) {
		Query query = new Query(Criteria.where("id").is(memberId));
		Update update = new Update();
		update.set("gold", gold);
		mongoTemplate.updateFirst(query, update, MemberDbo.class);
	}

	@Override
	public void updateMemberScore(String memberId, int score) {
		Query query = new Query(Criteria.where("id").is(memberId));
		Update update = new Update();
		update.set("score", score);
		mongoTemplate.updateFirst(query, update, MemberDbo.class);
	}

}
