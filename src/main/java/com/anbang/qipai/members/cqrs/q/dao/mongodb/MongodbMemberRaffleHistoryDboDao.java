package com.anbang.qipai.members.cqrs.q.dao.mongodb;

import com.anbang.qipai.members.cqrs.q.dao.MemberRaffleHistoryDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.MemberRaffleHistoryDbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MongodbMemberRaffleHistoryDboDao implements MemberRaffleHistoryDboDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<MemberRaffleHistoryDbo> findHistories(String memberId, int page, int size) {
        Query query = new Query(Criteria.where("memberId").is(memberId));
        query.skip((page - 1) * size);
        query.limit(size);
        return this.mongoTemplate.find(query, MemberRaffleHistoryDbo.class);
    }

    @Override
    public void save(MemberRaffleHistoryDbo memberRaffleHistoryDbo) {
        this.mongoTemplate.save(memberRaffleHistoryDbo);
    }

    @Override
    public MemberRaffleHistoryDbo findById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        return this.mongoTemplate.findOne(query, MemberRaffleHistoryDbo.class);
    }

}
