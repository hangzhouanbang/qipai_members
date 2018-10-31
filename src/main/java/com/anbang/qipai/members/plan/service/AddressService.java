package com.anbang.qipai.members.plan.service;

import com.anbang.qipai.members.plan.bean.Address;
import com.anbang.qipai.members.plan.dao.AddressDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddressService {

    @Autowired
    private AddressDao addressDao;

    public Address findOneByMemberId(String memberId) {
        return addressDao.findOneByMemberId(memberId);
    }

    public void saveOrEdit(Address address) {
        this.addressDao.saveOrUpdate(address);
    }

}
