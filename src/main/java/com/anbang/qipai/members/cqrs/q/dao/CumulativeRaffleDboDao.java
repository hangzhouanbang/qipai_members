package com.anbang.qipai.members.cqrs.q.dao;

import com.anbang.qipai.members.cqrs.q.dbo.CumulativeRaffleDbo;

/**
 * @Author: 吴硕涵
 * @Date: 2018/12/24 3:08 PM
 * @Version 1.0
 */
public interface CumulativeRaffleDboDao {
    void save(CumulativeRaffleDbo dbo);

    void add(CumulativeRaffleDbo dbo);

    CumulativeRaffleDbo findByMemberIdAndTime(String memebrId,String time);
}
