package com.anbang.qipai.members.cqrs.q.dao.mongodb;

import com.anbang.qipai.members.cqrs.q.dao.MemberHongBaoRecordDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.MemberHongBaoRecordDbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongodbHongBaoRecordDboDao implements MemberHongBaoRecordDboDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(MemberHongBaoRecordDbo memberHongBaoRecordDbo) {
        this.mongoTemplate.save(memberHongBaoRecordDbo);
    }



}
