package com.anbang.qipai.members.cqrs.q.dao.mongodb;

import com.anbang.qipai.members.cqrs.q.dao.LotteryDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.LotteryDbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MongodbLotteryDboDao implements LotteryDboDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<LotteryDbo> findUndiscard() {
        Query query = new Query();
        return this.mongoTemplate.find(query, LotteryDbo.class);
    }

    @Override
    public void saveAll(List<LotteryDbo> list) {
        this.mongoTemplate.insertAll(list);
    }

    @Override
    public void discardAll() {
        this.mongoTemplate.dropCollection(LotteryDbo.class);
    }

}
