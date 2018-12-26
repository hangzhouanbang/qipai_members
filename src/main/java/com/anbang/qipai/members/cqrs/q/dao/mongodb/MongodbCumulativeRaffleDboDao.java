package com.anbang.qipai.members.cqrs.q.dao.mongodb;

import com.anbang.qipai.members.cqrs.q.dao.CumulativeRaffleDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.CumulativeRaffleDbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

/**
 * @Author: 吴硕涵
 * @Date: 2018/12/24 3:09 PM
 * @Version 1.0
 */

@Component
public class MongodbCumulativeRaffleDboDao implements CumulativeRaffleDboDao {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public void save(CumulativeRaffleDbo dbo) {
        mongoTemplate.save(dbo);
    }

    @Override
    public void add(CumulativeRaffleDbo dbo) {
        mongoTemplate.insert(dbo);
    }

    @Override
    public CumulativeRaffleDbo findByMemberIdAndTime(String memebrId, String time) {
        Query query = new Query();
        query.addCriteria(Criteria.where("memberId").is(memebrId))
                .addCriteria(Criteria.where("time").is(time));
        return mongoTemplate.findOne(query, CumulativeRaffleDbo.class);
    }
}

