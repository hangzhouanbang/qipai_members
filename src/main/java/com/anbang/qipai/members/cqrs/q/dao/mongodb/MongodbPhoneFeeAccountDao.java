package com.anbang.qipai.members.cqrs.q.dao.mongodb;

import com.anbang.qipai.members.cqrs.q.dao.MemberPhoneFeeAccountDao;
import com.anbang.qipai.members.cqrs.q.dbo.PhoneFeeAccountDbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class MongodbPhoneFeeAccountDao implements MemberPhoneFeeAccountDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(PhoneFeeAccountDbo phoneFeeAccountDbo) {
        this.mongoTemplate.save(phoneFeeAccountDbo);
    }

    @Override
    public void update(String id, double balance) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().set("balance", balance);
        this.mongoTemplate.updateFirst(query, update, PhoneFeeAccountDbo.class);
    }

    @Override
    public PhoneFeeAccountDbo findByMemberId(String memberId) {
        Query query = new Query(Criteria.where("memberId").is(memberId));
        return this.mongoTemplate.findOne(query,PhoneFeeAccountDbo.class);
    }

}
