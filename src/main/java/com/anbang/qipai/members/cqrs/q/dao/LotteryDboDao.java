package com.anbang.qipai.members.cqrs.q.dao;

import com.anbang.qipai.members.cqrs.q.dbo.LotteryDbo;

import java.util.List;

public interface LotteryDboDao {

    List<LotteryDbo> findUndiscard();

    void saveAll(List<LotteryDbo> list);

    void discardAll();

    LotteryDbo findLotteryById(String id);
}
