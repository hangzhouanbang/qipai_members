package com.anbang.qipai.members.cqrs.q.dao.mongodb;

import com.anbang.qipai.members.cqrs.q.dao.MemberHongBaoAccountDao;
import com.anbang.qipai.members.cqrs.q.dbo.HongBaoAccountDbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class MongodbMemberHongBaoAccountDao implements MemberHongBaoAccountDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public HongBaoAccountDbo find(String memberId) {
        Query query = new Query(Criteria.where("memberId").is(memberId));
        return this.mongoTemplate.findOne(query, HongBaoAccountDbo.class);
    }

    @Override
    public void save(HongBaoAccountDbo hongBaoAccountDbo) {
        this.mongoTemplate.save(hongBaoAccountDbo);
    }

    @Override
    public void update(String id, double balance) {
        this.mongoTemplate.updateFirst(new Query(Criteria.where("id").is(id)), new Update().set("balance", balance), HongBaoAccountDbo.class);
    }

}
