package com.anbang.qipai.members.plan.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.plan.bean.MemberRightsConfiguration;
import com.anbang.qipai.members.plan.dao.MemberRightsConfigurationDao;

@Component
public class MongodbMemberRightsConfigurationDao implements MemberRightsConfigurationDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void save(MemberRightsConfiguration createMemberConfiguration) {
		mongoTemplate.save(createMemberConfiguration);
	}

	@Override
	public MemberRightsConfiguration find() {
		return mongoTemplate.findById("1", MemberRightsConfiguration.class);
	}

	@Override
	public void setPlanMembersRights(float planGrowIntegralSpeed, int goldForAgentInvite, int goldForNewNember) {
		mongoTemplate.updateFirst(new Query(Criteria.where("id").is("1")),
				new Update().set("planGrowIntegralSpeed", planGrowIntegralSpeed)
						.set("goldForNewNember", goldForNewNember).set("goldForAgentInvite", goldForAgentInvite),
				MemberRightsConfiguration.class);
	}

	@Override
	public void setVipMembersRights(float vipGrowGradeSpeed) {
		mongoTemplate.updateFirst(new Query(Criteria.where("id").is("1")),
				new Update().set("vipGrowGradeSpeed", vipGrowGradeSpeed), MemberRightsConfiguration.class);
	}

	@Override
	public void updatePlanMembersRights(int signGoldNumber, int goldForNewNember, int inviteIntegralNumber,
			int goldForAgentInvite, float planGrowIntegralSpeed) {
		mongoTemplate.updateMulti(new Query(Criteria.where("id").is("1")),
				new Update().set("signGoldNumber", signGoldNumber).set("goldForNewNember", goldForNewNember)
						.set("inviteIntegralNumber", inviteIntegralNumber).set("goldForAgentInvite", goldForAgentInvite)
						.set("planGrowIntegralSpeed", planGrowIntegralSpeed),
				MemberRightsConfiguration.class);
	}

	@Override
	public void updateVipMembersRights(int signGoldNumber, int inviteIntegralNumber, float vipGrowIntegralSpeed,
			float vipGrowGradeSpeed) {
		mongoTemplate.updateMulti(new Query(Criteria.where("id").is("1")),
				new Update().set("signGoldNumber", signGoldNumber).set("inviteIntegralNumber", inviteIntegralNumber)
						.set("vipGrowIntegralSpeed", vipGrowIntegralSpeed)
						.set("inviteIntegralNumber", inviteIntegralNumber).set("vipGrowGradeSpeed", vipGrowGradeSpeed),
				MemberRightsConfiguration.class);
	}

}
