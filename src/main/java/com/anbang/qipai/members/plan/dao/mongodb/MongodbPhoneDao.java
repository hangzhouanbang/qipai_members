package com.anbang.qipai.members.plan.dao.mongodb;

import com.anbang.qipai.members.plan.bean.Phone;
import com.anbang.qipai.members.plan.dao.PhoneDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class MongodbPhoneDao implements PhoneDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(Phone phone) {
        mongoTemplate.save(mongoTemplate);
    }

    @Override
    public Phone findOneByMemberId(String memberId) {
        Query query = new Query(Criteria.where("memberId").is(memberId));
        return mongoTemplate.findOne(query, Phone.class);
    }
}
