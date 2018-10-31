package com.anbang.qipai.members.cqrs.q.service;

import com.anbang.qipai.members.cqrs.c.domain.sign.Constant;
import com.anbang.qipai.members.cqrs.q.dao.MemberSignCountDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.MemberSignCountDbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberSignQueryService {

    @Autowired
    private MemberSignCountDboDao memberSignCountDboDao;

    public void saveOrUpdateMemberSignCount(MemberSignCountDbo memberSignCountDbo) {
        this.memberSignCountDboDao.save(memberSignCountDbo);
    }

    public MemberSignCountDbo find(String memberId) {
        return this.memberSignCountDboDao.find(memberId);
    }

    public boolean isSignedToday(String memberId) {
        MemberSignCountDbo memberSignCountDbo = this.find(memberId);
        if (memberSignCountDbo == null) {
            return false;
        } else {
            final long lastSignTimeAsDay = memberSignCountDbo.getLastSignTime() / Constant.ONE_DAY_MS;
            final long currentSignTimeAsDay = System.currentTimeMillis() / Constant.ONE_DAY_MS;
            return lastSignTimeAsDay == currentSignTimeAsDay;
        }
    }

}
