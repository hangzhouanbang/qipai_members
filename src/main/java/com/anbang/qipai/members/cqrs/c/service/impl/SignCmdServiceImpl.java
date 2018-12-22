package com.anbang.qipai.members.cqrs.c.service.impl;

import org.springframework.stereotype.Component;

import com.anbang.qipai.members.cqrs.c.domain.sign.SignCountManager;
import com.anbang.qipai.members.cqrs.c.domain.sign.SignHistory;
import com.anbang.qipai.members.cqrs.c.domain.sign.SignHistoryValueObject;
import com.anbang.qipai.members.cqrs.c.domain.vip.VIPEnum;
import com.anbang.qipai.members.cqrs.c.service.SignCmdService;

@Component
public class SignCmdServiceImpl extends CmdServiceBase implements SignCmdService {

	@Override
	public SignHistoryValueObject sign(String memberId, Integer vipLevel, Long signCurrentTime) {
		final SignCountManager signCountManager = singletonEntityRepository.getEntity(SignCountManager.class);
		// 如果 今天已经签到过了 不执行
		if (signCountManager.isSignedToday(memberId)) {
			return null;
		} else {
			final long signTime = signCountManager.signCurrentTime(memberId, signCurrentTime);
			final int continuousSignDays = signCountManager.continuousSignDays(memberId);
			final SignHistory signHistory = new SignHistory(memberId, signTime, continuousSignDays,
					VIPEnum.of(vipLevel));
			return new SignHistoryValueObject(signHistory);
		}
	}

}