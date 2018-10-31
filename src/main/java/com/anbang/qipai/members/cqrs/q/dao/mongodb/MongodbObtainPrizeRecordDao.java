package com.anbang.qipai.members.cqrs.q.dao.mongodb;

import com.anbang.qipai.members.cqrs.c.domain.prize.ObatinSigningPrizeRecord;
import com.anbang.qipai.members.cqrs.q.dao.ObtainPrizeRecordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MongodbObtainPrizeRecordDao implements ObtainPrizeRecordDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<ObatinSigningPrizeRecord> find(String memberId, int page, int size) {
        Query query = new Query(Criteria.where("memberId").is(memberId));
        query.skip((page - 1) * size);
        query.limit(size);
        return mongoTemplate.find(query, ObatinSigningPrizeRecord.class);
    }

    @Override
    public void save(ObatinSigningPrizeRecord record) {
        this.mongoTemplate.save(record);
    }

}
