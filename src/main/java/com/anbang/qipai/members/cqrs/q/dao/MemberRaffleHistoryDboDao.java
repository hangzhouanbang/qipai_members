package com.anbang.qipai.members.cqrs.q.dao;

import com.anbang.qipai.members.cqrs.q.dbo.MemberRaffleHistoryDbo;

import java.util.List;

public interface MemberRaffleHistoryDboDao {

    List<MemberRaffleHistoryDbo> findHistories(String memberId, int page, int size);

    void save(MemberRaffleHistoryDbo memberRaffleHistoryDbo);


    MemberRaffleHistoryDbo findById(String id);

    MemberRaffleHistoryDbo findByMemberId(String memberId);
}