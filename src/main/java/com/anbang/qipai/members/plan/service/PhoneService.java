package com.anbang.qipai.members.plan.service;

import com.anbang.qipai.members.plan.bean.Phone;
import com.anbang.qipai.members.plan.dao.PhoneDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PhoneService {

    @Autowired
    private PhoneDao phoneDao;

    public void save(Phone phone) {
        phoneDao.save(phone);
    }

    public Phone findOneByMemberId(String memberId){
        return phoneDao.findOneByMemberId(memberId);
    }


}
