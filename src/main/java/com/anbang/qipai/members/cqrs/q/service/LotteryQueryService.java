package com.anbang.qipai.members.cqrs.q.service;

import com.anbang.qipai.members.cqrs.q.dao.LotteryDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.LotteryDbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LotteryQueryService {

    @Autowired
    private LotteryDboDao lotteryDboDao;

    public List<LotteryDbo> findAll() {
        return lotteryDboDao.findUndiscard();
    }

    public void discardAll() {
        this.lotteryDboDao.discardAll();
    }

    public void discardBeforeAndSaveAll(List<LotteryDbo> lotteryDboList) {
        this.lotteryDboDao.discardAll();
        this.lotteryDboDao.saveAll(lotteryDboList);
    }

}