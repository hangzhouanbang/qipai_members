package com.anbang.qipai.members.plan.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.plan.dao.RefundOrderDao;
import com.anbang.qipai.members.plan.domain.RefundOrder;
import com.mongodb.WriteResult;

@Component
public class MongodbRefundOrderDao implements RefundOrderDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void addRefundOrder(RefundOrder refund) {
		mongoTemplate.insert(refund);
	}

	@Override
	public RefundOrder findRefundOrderById(String out_refund_no) {
		Query query = new Query(Criteria.where("out_refund_no").is(out_refund_no));
		return mongoTemplate.findOne(query, RefundOrder.class);
	}

	@Override
	public Boolean updateRefund_id(String out_refund_no, String refund_id) {
		Query query = new Query(Criteria.where("out_refund_no").is(out_refund_no));
		Update update = new Update();
		update.set("refund_id", refund_id);
		WriteResult writeResult = mongoTemplate.updateFirst(query, update, RefundOrder.class);
		return writeResult.getN() > 0;
	}

}
