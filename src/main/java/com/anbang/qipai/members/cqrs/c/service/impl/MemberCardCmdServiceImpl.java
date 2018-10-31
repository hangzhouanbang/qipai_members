package com.anbang.qipai.members.cqrs.c.service.impl;

import com.anbang.qipai.members.cqrs.c.domain.prize.ObatinSigningPrizeRecord;
import com.anbang.qipai.members.cqrs.c.domain.prize.PrizeEnum;
import com.anbang.qipai.members.cqrs.c.domain.prize.SignPrizeManager;
import com.anbang.qipai.members.cqrs.c.domain.sign.*;
import com.anbang.qipai.members.cqrs.c.domain.vip.VIPEnum;
import com.anbang.qipai.members.cqrs.c.domain.vip.VipSignGiftScoreManager;
import com.anbang.qipai.members.cqrs.c.service.MemberCardCmdService;
import org.springframework.stereotype.Component;

@Component
public class MemberCardCmdServiceImpl extends CmdServiceBase implements MemberCardCmdService {

    @Override
    public ObatinSigningPrizeRecord obtainMemberCardSignPrize(String memberId, SignTypeEnum signType) throws OpportunityInvalidUsedException, OpportunityNotExistsExcetion {
        SignPrizeManager signPrizeManager = this.singletonEntityRepository.getEntity(SignPrizeManager.class);
        VipSignGiftScoreManager vipSignGiftScoreManager = this.singletonEntityRepository.getEntity(VipSignGiftScoreManager.class);
        final PrizeEnum prize = signPrizeManager.getPrize(signType);
        if (!PrizeEnum.isMemberCardType(prize)) {
            throw new IllegalArgumentException("prize type must be member card");
        }
        SigningPrizeOpportunityManager signingPrizeOpportunityManager = this.singletonEntityRepository.getEntity(SigningPrizeOpportunityManager.class);
        ObtainSignPrizeOpportunity opportunity = signingPrizeOpportunityManager.useSignPrizeOpportunity(memberId, signType);
        final VIPEnum vipLevel = opportunity.getVipLevel();
        if (vipLevel != null) {
            final int score = vipSignGiftScoreManager.getVipGiftScore(vipLevel).getScore();
            return new ObatinSigningPrizeRecord(memberId, prize, System.currentTimeMillis(), vipLevel.getLevel(), score);
        }
        return new ObatinSigningPrizeRecord(memberId, prize, System.currentTimeMillis(), 0, 0);
    }

}
