package com.anbang.qipai.members.cqrs.q.dao;

import com.anbang.qipai.members.cqrs.q.dbo.HongBaoAccountDbo;

public interface MemberHongBaoAccountDao {

    HongBaoAccountDbo find(String memberId);

    void save(HongBaoAccountDbo hongBaoAccountDbo);

    void update(String id, double balance);

}
