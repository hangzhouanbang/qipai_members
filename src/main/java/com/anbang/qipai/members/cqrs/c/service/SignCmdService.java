package com.anbang.qipai.members.cqrs.c.service;

import com.anbang.qipai.members.cqrs.c.domain.sign.SignHistoryValueObject;

public interface SignCmdService {

    SignHistoryValueObject sign(String memberId, Integer vipLevel);


}
