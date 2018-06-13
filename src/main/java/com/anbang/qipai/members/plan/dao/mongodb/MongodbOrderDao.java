package com.anbang.qipai.members.plan.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.plan.dao.OrderDao;
import com.anbang.qipai.members.plan.domain.Order;
import com.mongodb.WriteResult;

@Component
public class MongodbOrderDao implements OrderDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void addOrder(Order order) {
		mongoTemplate.insert(order);
	}

	@Override
	public Boolean updateOrderStatus(String out_trade_no, int status) {
		Query query = new Query(Criteria.where("out_trade_no").is(out_trade_no));
		Update update = new Update();
		update.set("status", status);
		WriteResult writeResult = mongoTemplate.updateFirst(query, update, Order.class);
		return writeResult.getN() > 0;
	}

	@Override
	public Order findOrderByOut_trade_no(String out_trade_no) {
		Query query = new Query(Criteria.where("out_trade_no").is(out_trade_no));
		return mongoTemplate.findOne(query, Order.class);
	}

	@Override
	public Boolean updateTransaction_id(String out_trade_no, String transaction_id) {
		Query query = new Query(Criteria.where("out_trade_no").is(out_trade_no));
		Update update = new Update();
		update.set("transaction_id", transaction_id);
		WriteResult writeResult = mongoTemplate.updateFirst(query, update, Order.class);
		return writeResult.getN() > 0;
	}

}
