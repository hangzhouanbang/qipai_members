package com.anbang.qipai.members.cqrs.q.dao.mongodb;

import com.anbang.qipai.members.cqrs.q.dao.LotteryDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.LotteryDbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    public List<LotteryDbo> findExtraLottey() {
        List<String> list = new ArrayList<>();
        list.add("11");
        list.add("12");
        list.add("13");
        list.add("14");
        list.add("15");
        Query query = new Query(Criteria.where("index").in(list));
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

    @Override
    public LotteryDbo findLotteryById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, LotteryDbo.class);
    }

}
