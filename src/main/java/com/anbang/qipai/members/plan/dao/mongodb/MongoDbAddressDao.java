package com.anbang.qipai.members.plan.dao.mongodb;

import com.anbang.qipai.members.plan.bean.Address;
import com.anbang.qipai.members.plan.dao.AddressDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class MongoDbAddressDao implements AddressDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Address findOneByMemberId(String memberId) {
        Query query = new Query(Criteria.where("memberId").is(memberId));
        return mongoTemplate.findOne(query, Address.class);
    }

    @Override
    public void saveOrUpdate(Address address) {
        if (address != null) {
            mongoTemplate.save(address);
        }
    }

}
