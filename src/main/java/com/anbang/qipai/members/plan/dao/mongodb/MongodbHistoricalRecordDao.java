package com.anbang.qipai.members.plan.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.plan.dao.HistoricalRecordDao;
import com.anbang.qipai.members.plan.domain.historicalrecord.MemberHistoricalRecord;

@Component
public class MongodbHistoricalRecordDao implements HistoricalRecordDao{
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void addrecord(MemberHistoricalRecord memberHistoricalRecord) {
		mongoTemplate.save(memberHistoricalRecord);
	}

	@Override
	public List<MemberHistoricalRecord> findallrecord(String memberid) {
		 Sort sort = new Sort(Sort.Direction.DESC, "endtime");
		 Pageable pageable= new PageRequest(0,20,sort);
		 Query query = new Query(Criteria.where("memberid").is(memberid));
		return mongoTemplate.find(query.with(pageable), MemberHistoricalRecord.class);
	}

	@Override
	public MemberHistoricalRecord findonerecord(String id) {
		Query query = new Query(Criteria.where("id").is(id));
		return mongoTemplate.findOne(query, MemberHistoricalRecord.class);
	}
}
