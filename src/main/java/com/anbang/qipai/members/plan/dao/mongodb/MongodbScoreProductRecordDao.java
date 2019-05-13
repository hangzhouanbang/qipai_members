package com.anbang.qipai.members.plan.dao.mongodb;

import java.util.List;

import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.plan.bean.ScoreProductRecord;
import com.anbang.qipai.members.plan.dao.ScoreProductRecordDao;

@Component
public class MongodbScoreProductRecordDao implements ScoreProductRecordDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void save(ScoreProductRecord record) {
		mongoTemplate.insert(record);
	}

	@Override
	public ScoreProductRecord findById(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		return mongoTemplate.findOne(query, ScoreProductRecord.class);
	}

	@Override
	public void updateStatusById(String id, String status) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		Update update = new Update();
		update.set("status", status);
		mongoTemplate.updateFirst(query, update, ScoreProductRecord.class);
	}

	@Override
	public long countByMemberIdAndStatus(String memberId, String status) {
		Query query = new Query();
		if (StringUtil.isNotBlank(memberId)) {
			query.addCriteria(Criteria.where("memberId").is(memberId));
		}
		if (StringUtil.isNotBlank(status)) {
			query.addCriteria(Criteria.where("status").is(status));
		}
		return mongoTemplate.count(query, ScoreProductRecord.class);
	}

	@Override
	public List<ScoreProductRecord> findByMemberIdAndStatus(int page, int size, String memberId, String status) {
		Query query = new Query();
		if (StringUtil.isNotBlank(memberId)) {
			query.addCriteria(Criteria.where("memberId").is(memberId));
		}
		if (StringUtil.isNotBlank(status)) {
			query.addCriteria(Criteria.where("status").is(status));
		}
		query.skip((page - 1) * size);
		query.limit(size);
		return mongoTemplate.find(query, ScoreProductRecord.class);
	}

	@Override
	public void updateDeliverTimeById(String id, long deliverTime) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		Update update = new Update();
		update.set("deliverTime", deliverTime);
		mongoTemplate.updateFirst(query, update, ScoreProductRecord.class);
	}

}
