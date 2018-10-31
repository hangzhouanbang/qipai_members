package com.anbang.qipai.members.cqrs.q.service;

import com.anbang.qipai.members.cqrs.c.domain.prize.ObatinSigningPrizeRecord;
import com.anbang.qipai.members.cqrs.q.dao.ObtainPrizeRecordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ObtainPrizeQueryService {

    @Autowired
    private ObtainPrizeRecordDao obtainPrizeRecordDao;

    public List<ObatinSigningPrizeRecord> find(String memberId, int page, int size) {
        return this.obtainPrizeRecordDao.find(memberId, page, size);
    }

    public void save(ObatinSigningPrizeRecord obatinSigningPrizeRecord) {
        this.obtainPrizeRecordDao.save(obatinSigningPrizeRecord);
    }

}
