package com.anbang.qipai.members.cqrs.q.service;

import com.anbang.qipai.members.cqrs.q.dao.mongodb.MongodbReceiverInfoDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.ReceiverInfoDbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: 吴硕涵
 * @Date: 2018/12/25 2:11 PM
 * @Version 1.0
 */
@Service
public class ReceiverInfoQueryService {

    @Autowired
    private MongodbReceiverInfoDboDao receiverInfoDboDao;

    public ReceiverInfoDbo findReceiverByMemberId(String memberId) {
        return receiverInfoDboDao.findByMemberId(memberId);
    }

    public void addReceiverInfo(ReceiverInfoDbo dbo) {
        receiverInfoDboDao.add(dbo);
    }
}
