package com.anbang.qipai.members.cqrs.q.dao;


import com.anbang.qipai.members.cqrs.c.domain.prize.ObatinSigningPrizeRecord;

import java.util.List;

public interface ObtainPrizeRecordDao {

    List<ObatinSigningPrizeRecord> find(String memberId,int page, int size);

    void save(ObatinSigningPrizeRecord record);

}
