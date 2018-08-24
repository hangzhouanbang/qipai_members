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
	public List<MemberDbo> findMemberByVip(int page, int size, boolean vip) {
		Query query = new Query(Criteria.where("").is(vip));
		query.skip((page - 1) * size);
		query.limit(size);
		return mongoTemplate.find(query, MemberDbo.class);
	}

	@Override
	public long getAmountByVip(boolean vip) {
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
		update.set("cost", member.getCost());
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
	public void agentUpdateMemberVip(MemberDbo member) {
		Query query = new Query(Criteria.where("id").is(member.getId()));
		Update update = new Update();
		update.set("vipEndTime", member.getVipEndTime());
		update.set("vip", member.getVip());
		mongoTemplate.updateFirst(query, update, MemberDbo.class);
	}

	@Override
	public void verifyUser(String memberId, String realName, String IDcard, boolean verify) {
		Query query = new Query(Criteria.where("id").is(memberId));
		Update update = new Update();
		update.set("realName", realName);
		update.set("IDcard", IDcard);
		update.set("verifyUser", verify);
		mongoTemplate.updateFirst(query, update, MemberDbo.class);
	}

	@Override
	public void updateMemberLogin(String memberId, String state,String loginIp, long loginTime) {
		Query query = new Query(Criteria.where("id").is(memberId));
		Update update = new Update();
		update.set("state", state);
		update.set("loginIp", loginIp);
		update.set("lastLoginTime", loginTime);
		mongoTemplate.updateFirst(query, update, MemberDbo.class);
	}

	@Override
	public void updateMemberLogout(String memberId, String state, long onlineTime) {
		Query query = new Query(Criteria.where("id").is(memberId));
		Update update = new Update();
		update.set("state", state);
		update.set("onlineTime", onlineTime);
		mongoTemplate.updateFirst(query, update, MemberDbo.class);
	}

}
