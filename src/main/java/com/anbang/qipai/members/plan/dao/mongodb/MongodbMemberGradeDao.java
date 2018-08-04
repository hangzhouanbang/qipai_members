package com.anbang.qipai.members.plan.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.plan.bean.MemberGrade;
import com.anbang.qipai.members.plan.dao.MemberGradeDao;

@Component
public class MongodbMemberGradeDao implements MemberGradeDao{
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void insert_grade(MemberGrade memberGrade) {
		mongoTemplate.save(memberGrade);
	}

	@Override
	public MemberGrade find_grade(String id) {
		return mongoTemplate.findById(id, MemberGrade.class);
	}

}
