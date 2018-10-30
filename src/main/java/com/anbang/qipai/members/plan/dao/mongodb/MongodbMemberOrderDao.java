package com.anbang.qipai.members.plan.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.plan.bean.MemberOrder;
import com.anbang.qipai.members.plan.dao.MemberOrderDao;

@Component
public class MongodbMemberOrderDao implements MemberOrderDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void addMemberOrder(MemberOrder order) {
		mongoTemplate.insert(order);
	}

	@Override
	public void orderFinished(String id, String transaction_id, String status, long deliveTime) {
		Query query = new Query(Criteria.where("id").is(id));
		Update update = new Update();
		update.set("transaction_id", transaction_id);
		update.set("status", status);
		update.set("deliveTime", deliveTime);
		mongoTemplate.updateFirst(query, update, MemberOrder.class);
	}

	@Override
	public MemberOrder findMemberOrderById(String id) {
		return mongoTemplate.findOne(new Query(Criteria.where("id").is(id)), MemberOrder.class);
	}

	@Override
	public MemberOrder findMemberOrderByPayerIdAndProductName(String payerId, String productName) {
		Query query = new Query();
		query.addCriteria(Criteria.where("payerId").is(payerId));
		query.addCriteria(Criteria.where("productName").is(productName));
		return mongoTemplate.findOne(query, MemberOrder.class);
	}

}
