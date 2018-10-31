package com.anbang.qipai.members.plan.dao;

import com.anbang.qipai.members.plan.bean.Address;

public interface AddressDao {

    Address findOneByMemberId(String memberId);

    void saveOrUpdate(Address address);

}
