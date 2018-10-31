package com.anbang.qipai.members.cqrs.c.service;

import com.anbang.qipai.members.cqrs.c.domain.prize.ObatinSigningPrizeRecord;
import com.anbang.qipai.members.cqrs.c.domain.sign.OpportunityInvalidUsedException;
import com.anbang.qipai.members.cqrs.c.domain.sign.OpportunityNotExistsExcetion;
import com.anbang.qipai.members.cqrs.c.domain.sign.SignTypeEnum;

public interface MemberCardCmdService {

    ObatinSigningPrizeRecord obtainMemberCardSignPrize(String memberId, SignTypeEnum signPrizeType)
            throws OpportunityInvalidUsedException, OpportunityNotExistsExcetion;

}
