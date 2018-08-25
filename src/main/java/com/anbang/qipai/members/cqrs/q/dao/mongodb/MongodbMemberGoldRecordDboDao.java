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

import com.anbang.qipai.members.cqrs.q.dao.MemberGoldRecordDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldRecordDbo;

@Component
public class MongodbMemberGoldRecordDboDao implements MemberGoldRecordDboDao {

	@Autowired
	private MongoTemplate mongoTempalte;

	@Override
	public void save(MemberGoldRecordDbo dbo) {
		mongoTempalte.insert(dbo);
	}

	@Override
	public long getCountByMemberId(String memberId) {
		Query query = new Query(Criteria.where("memberId").is(memberId));
		return mongoTempalte.count(query, MemberGoldRecordDbo.class);
	}

	@Override
	public List<MemberGoldRecordDbo> findMemberGoldRecordByMemberId(String memberId, int page, int size) {
		Query query = new Query(Criteria.where("memberId").is(memberId));
		query.with(new Sort(new Order(Direction.DESC, "accountingTime")));
		query.skip((page - 1) * size);
		query.limit(size);
		return mongoTempalte.find(query, MemberGoldRecordDbo.class);
	}

}
