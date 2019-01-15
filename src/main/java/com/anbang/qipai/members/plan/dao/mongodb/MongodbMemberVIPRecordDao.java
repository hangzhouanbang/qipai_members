package com.anbang.qipai.members.plan.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.plan.bean.MemberVIPRecord;
import com.anbang.qipai.members.plan.dao.MemberVIPRecordDao;

@Component
public class MongodbMemberVIPRecordDao implements MemberVIPRecordDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void addMemberVIPRecord(MemberVIPRecord record) {
		mongoTemplate.insert(record);
	}

}
