package com.anbang.qipai.members.cqrs.q.dao;

import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldAccountDbo;

public interface MemberGoldAccountDboDao {

    void save(MemberGoldAccountDbo accountDbo);

    void update(String id, int balance);

    MemberGoldAccountDbo findByMemberId(String memberId);

    void updateByMemberId(String memberId, int balance);

    MemberGoldAccountDbo findGoldByMemberId(String memberId);

}
