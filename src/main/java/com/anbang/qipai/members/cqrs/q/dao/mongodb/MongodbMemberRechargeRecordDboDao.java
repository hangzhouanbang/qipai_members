package com.anbang.qipai.members.cqrs.q.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.cqrs.q.dao.MemberRechargeRecordDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.MemberRechargeRecordDbo;

@Component
public class MongodbMemberRechargeRecordDboDao implements MemberRechargeRecordDboDao{

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public MemberRechargeRecordDbo find_grade(String memberId) {
		return mongoTemplate.findById(memberId, MemberRechargeRecordDbo.class);
	}

	@Override
	public void saveMemberRechargeAmount(MemberRechargeRecordDbo memberRechargeRecordDbo) {
		mongoTemplate.insert(memberRechargeRecordDbo);
	}

}
