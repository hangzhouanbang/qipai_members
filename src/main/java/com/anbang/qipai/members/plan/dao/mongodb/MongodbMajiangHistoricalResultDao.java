package com.anbang.qipai.members.plan.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.plan.bean.historicalresult.MajiangHistoricalResult;
import com.anbang.qipai.members.plan.dao.MajiangHistoricalResultDao;

@Component
public class MongodbMajiangHistoricalResultDao implements MajiangHistoricalResultDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void addMajiangHistoricalResult(MajiangHistoricalResult result) {
		mongoTemplate.insert(result);
	}

}
