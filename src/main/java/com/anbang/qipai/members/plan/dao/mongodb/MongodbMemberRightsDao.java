package com.anbang.qipai.members.plan.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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

}
