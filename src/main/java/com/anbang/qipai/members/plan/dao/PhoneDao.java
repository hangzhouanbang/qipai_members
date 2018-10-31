package com.anbang.qipai.members.plan.dao;

import com.anbang.qipai.members.plan.bean.Phone;

public interface PhoneDao {

    void save(Phone phone);

    Phone findOneByMemberId(String memberId);

}
