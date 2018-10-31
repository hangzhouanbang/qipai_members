package com.anbang.qipai.members.cqrs.q.dao;

import com.anbang.qipai.members.cqrs.q.dbo.PhoneFeeAccountDbo;

public interface MemberPhoneFeeAccountDao {

    void save(PhoneFeeAccountDbo phoneFeeAccountDbo);

    void update(String id, double balance);

    PhoneFeeAccountDbo findByMemberId(String memberId);
}
