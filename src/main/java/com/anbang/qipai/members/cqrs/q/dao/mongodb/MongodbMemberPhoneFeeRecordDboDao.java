package com.anbang.qipai.members.cqrs.q.dao.mongodb;

import com.anbang.qipai.members.cqrs.q.dao.MemberPhoneFeeRecordDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.PhoneFeeRecordDbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongodbMemberPhoneFeeRecordDboDao implements MemberPhoneFeeRecordDboDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(PhoneFeeRecordDbo phoneFeeRecordDbo) {
        this.mongoTemplate.save(phoneFeeRecordDbo);
    }

}
