package com.anbang.qipai.members.cqrs.q.dao.mongodb;

import com.anbang.qipai.members.cqrs.q.dao.MemberSignCountDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.MemberSignCountDbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;


@Component
public class MongodbMemberSignCountDboDao implements MemberSignCountDboDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public MemberSignCountDbo find(String memberId) {
        Query query = new Query(Criteria.where("memberId").is(memberId));
        return this.mongoTemplate.findOne(query, MemberSignCountDbo.class);
    }

    @Override
    public void save(MemberSignCountDbo memberSignCountDbo) {
        this.mongoTemplate.save(memberSignCountDbo);
    }

}
