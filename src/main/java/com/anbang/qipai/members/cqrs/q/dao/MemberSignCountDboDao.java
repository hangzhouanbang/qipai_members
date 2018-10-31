package com.anbang.qipai.members.cqrs.q.dao;

import com.anbang.qipai.members.cqrs.q.dbo.MemberSignCountDbo;

public interface MemberSignCountDboDao {
    MemberSignCountDbo find(String memberId);

    void save(MemberSignCountDbo memberSignCountDbo);

}
