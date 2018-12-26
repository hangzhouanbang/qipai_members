package com.anbang.qipai.members.cqrs.q.dao.mongodb;

import com.anbang.qipai.members.cqrs.q.dao.ReceiverInfoDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.ReceiverInfoDbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


/**
 * @Author: 吴硕涵
 * @Date: 2018/12/25 2:07 PM
 * @Version 1.0
 */
@Component
public class MongodbReceiverInfoDboDao implements ReceiverInfoDboDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(ReceiverInfoDbo dbo) {
        mongoTemplate.save(dbo);
    }

    @Override
    public void add(ReceiverInfoDbo dbo) {
        mongoTemplate.insert(dbo);
    }

    @Override
    public ReceiverInfoDbo findByMemberId(String memebrId) {
        Query query = new Query(Criteria.where("memberId").is(memebrId));
        return mongoTemplate.findOne(query, ReceiverInfoDbo.class);
    }

}
