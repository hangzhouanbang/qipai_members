package com.anbang.qipai.members.cqrs.c.service.impl;

import com.anbang.qipai.members.cqrs.c.domain.sign.SignCountManager;
import com.anbang.qipai.members.cqrs.c.domain.sign.SignHistory;
import com.anbang.qipai.members.cqrs.c.domain.sign.SignHistoryValueObject;
import com.anbang.qipai.members.cqrs.c.domain.vip.VIPEnum;
import com.anbang.qipai.members.cqrs.c.service.SignCmdService;
import org.springframework.stereotype.Component;

@Component
public class SignCmdServiceImpl extends CmdServiceBase implements SignCmdService {

    @Override
    public SignHistoryValueObject sign(String memberId, int vipLevel) {
        final SignCountManager signCountManager = singletonEntityRepository.getEntity(SignCountManager.class);
        if (signCountManager.isSignedToday(memberId)) {
            return null;
        } else {
            final long signTime = signCountManager.signCurrentTime(memberId);
            final int continuousSignDays = signCountManager.continuousSignDays(memberId);
            final SignHistory signHistory = new SignHistory(memberId, signTime, continuousSignDays, VIPEnum.of(vipLevel));
            return new SignHistoryValueObject(signHistory);
        }
    }

}