package com.anbang.qipai.members.plan.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.plan.dao.MemberRightsDao;
import com.anbang.qipai.members.plan.domain.MemberRights;

@Component
public class MongodbMemberRightsDao implements MemberRightsDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void savePlanShipConfiguration(MemberRights createMemberConfiguration) {
		mongoTemplate.save(createMemberConfiguration);
	}

	@Override
	public MemberRights find() {
		return mongoTemplate.findById("1", MemberRights.class);
	}

	@Override
	public MemberRights savevipuser(MemberRights createMemberConfiguration) {
		mongoTemplate.save(createMemberConfiguration);
		return createMemberConfiguration;
	}

	@Override
	public void setPlanMembersRights(float planGrowIntegralSpeed, int goldForNewNember) {
		mongoTemplate.updateFirst(new Query(Criteria.where("id").is("1")), new Update()
				.set("planGrowIntegralSpeed", planGrowIntegralSpeed).set("goldForNewNember", goldForNewNember),
				MemberRights.class);
	}

	@Override
	public void setVipMembersRights(float vipGrowGradeSpeed) {
		mongoTemplate.updateFirst(new Query(Criteria.where("id").is("1")), new Update()
				.set("vipGrowGradeSpeed", vipGrowGradeSpeed),
				MemberRights.class);
	}

	@Override
	public void updatePlanMembersRights(int signGoldNumber, int goldForNewNember,int inviteIntegralNumber, float planGrowIntegralSpeed) {
		mongoTemplate.updateMulti(new Query(Criteria.where("id").is("1")),
				new Update().set("signGoldNumber", signGoldNumber)
						.set("goldForNewNember", goldForNewNember)
						.set("inviteIntegralNumber", inviteIntegralNumber)
						.set("planGrowIntegralSpeed", planGrowIntegralSpeed),
						MemberRights.class);
	}
	
	@Override
	public void updateVipMembersRights(int signGoldNumber,int inviteIntegralNumber,float vipGrowIntegralSpeed,float vipGrowGradeSpeed) {
		mongoTemplate.updateMulti(new Query(Criteria.where("id").is("1")),
				new Update().set("signGoldNumber", signGoldNumber)
						.set("inviteIntegralNumber", inviteIntegralNumber)
						.set("vipGrowIntegralSpeed", vipGrowIntegralSpeed)
						.set("inviteIntegralNumber", inviteIntegralNumber)
						.set("vipGrowGradeSpeed", vipGrowGradeSpeed),
						MemberRights.class);
	}

}
