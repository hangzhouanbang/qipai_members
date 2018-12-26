package com.anbang.qipai.members.cqrs.q.service;

import com.anbang.qipai.members.cqrs.q.dao.MemberRaffleHistoryDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.MemberRaffleHistoryDbo;
import com.anbang.qipai.members.plan.bean.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemberRaffleQueryService {

    @Autowired
    private MemberRaffleHistoryDboDao raffleHistoryDboDao;

    public List<MemberRaffleHistoryDbo> findHistoriesWithoutPage(String memberId) {
        List<MemberRaffleHistoryDbo> histories = raffleHistoryDboDao.findHistoriesWithoutPage(memberId);
        return histories;
    }


    public List<MemberRaffleHistoryDbo> findHistories(String memberId, int page, int size) {
        List<MemberRaffleHistoryDbo> histories = raffleHistoryDboDao.findHistories(memberId, page, size);
        return histories;
    }

    public boolean isFirstRaffle(String memberId) {
        List<MemberRaffleHistoryDbo> list = this.raffleHistoryDboDao.findHistories(memberId, 1, 5);
        if (list == null || list.isEmpty()) {
            return true;
        }
        return false;
    }

    public void save(MemberRaffleHistoryDbo dbo) {
        this.raffleHistoryDboDao.save(dbo);
    }

    public MemberRaffleHistoryDbo find(String id) {
        return this.raffleHistoryDboDao.findById(id);
    }


    public MemberRaffleHistoryDbo findByMemberId(String memberId) {
        return this.raffleHistoryDboDao.findByMemberId(memberId);
    }

    public MemberRaffleHistoryDbo findshareTimeByMemberId(String memberId) {
        return this.raffleHistoryDboDao.shareTimeByMemberId(memberId);
    }


    public MemberRaffleHistoryDbo setRaffleAddress(String id, String memberId, Address address) {
        MemberRaffleHistoryDbo memberRaffleHistoryDbo = this.find(id);
        if (memberRaffleHistoryDbo != null && memberRaffleHistoryDbo.getAddress() == null && memberRaffleHistoryDbo.getMemberId().equals(memberId)) {
            memberRaffleHistoryDbo.setAddress(address);
            this.save(memberRaffleHistoryDbo);
        }
        return memberRaffleHistoryDbo;
    }
}
