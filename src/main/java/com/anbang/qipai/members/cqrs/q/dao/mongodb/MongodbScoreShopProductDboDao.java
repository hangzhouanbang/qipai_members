package com.anbang.qipai.members.cqrs.q.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.cqrs.q.dao.ScoreShopProductDboDao;
import com.anbang.qipai.members.cqrs.q.dao.mongodb.repository.ScoreShopProductDboRepository;
import com.anbang.qipai.members.cqrs.q.dbo.ScoreShopProductDbo;

@Component
public class MongodbScoreShopProductDboDao implements ScoreShopProductDboDao {

	@Autowired
	private ScoreShopProductDboRepository repository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void save(ScoreShopProductDbo dbo) {
		repository.save(dbo);
	}

	@Override
	public void update(ScoreShopProductDbo dbo) {
		repository.save(dbo);

	}

	@Override
	public void removeByIds(String[] ids) {
		Object[] temIds = ids;
		Query query = new Query();
		query.addCriteria(Criteria.where("id").in(temIds));
		mongoTemplate.remove(query, ScoreShopProductDbo.class);
	}

	@Override
	public ScoreShopProductDbo findById(String id) {
		return repository.findOne(id);
	}

	@Override
	public List<ScoreShopProductDbo> findByType(int page, int size, String type) {
		Query query = new Query();
		query.addCriteria(Criteria.where("type").is(type));
		query.skip((page - 1) * size);
		query.limit(size);
		return mongoTemplate.find(query, ScoreShopProductDbo.class);
	}

	@Override
	public void removeAll() {
		repository.deleteAll();
	}

	@Override
	public void saveAll(List<ScoreShopProductDbo> products) {
		mongoTemplate.insert(products, ScoreShopProductDbo.class);
	}

	@Override
	public void incRemainById(String id, int amount) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		Update update = new Update();
		update.inc("remain", amount);
		mongoTemplate.updateFirst(query, update, ScoreShopProductDbo.class);
	}

	@Override
	public long countByType(String type) {
		Query query = new Query();
		query.addCriteria(Criteria.where("type").is(type));
		return mongoTemplate.count(query, ScoreShopProductDbo.class);
	}

}
