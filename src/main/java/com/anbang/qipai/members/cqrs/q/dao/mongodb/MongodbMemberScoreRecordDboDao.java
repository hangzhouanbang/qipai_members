package com.anbang.qipai.members.cqrs.q.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.cqrs.q.dao.MemberScoreRecordDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.MemberScoreRecordDbo;

@Component
public class MongodbMemberScoreRecordDboDao implements MemberScoreRecordDboDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void save(MemberScoreRecordDbo dbo) {
		mongoTemplate.insert(dbo);
	}

	@Override
	public long getCountByMemberId(String memberId) {
		Query query = new Query(Criteria.where("memberId").is(memberId));
		return mongoTemplate.count(query, MemberScoreRecordDbo.class);
	}

	@Override
	public List<MemberScoreRecordDbo> findMemberScoreRecordByMemberId(String memberId, int page, int size) {
		Query query = new Query(Criteria.where("memberId").is(memberId));
		query.with(new Sort(new Order(Direction.DESC, "accountingTime")));
		query.skip((page - 1) * size);
		query.limit(size);
		return mongoTemplate.find(query, MemberScoreRecordDbo.class);
	}

}
