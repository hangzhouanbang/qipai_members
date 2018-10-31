package com.anbang.qipai.members.cqrs.c.service.disruptor;

import com.anbang.qipai.members.cqrs.c.domain.prize.ObatinSigningPrizeRecord;
import com.anbang.qipai.members.cqrs.c.domain.sign.OpportunityInvalidUsedException;
import com.anbang.qipai.members.cqrs.c.domain.sign.OpportunityNotExistsExcetion;
import com.anbang.qipai.members.cqrs.c.domain.sign.SignTypeEnum;
import com.anbang.qipai.members.cqrs.c.service.MemberCardCmdService;
import com.anbang.qipai.members.cqrs.c.service.impl.MemberCardCmdServiceImpl;
import com.highto.framework.concurrent.DeferredResult;
import com.highto.framework.ddd.CommonCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

@Component("memberCardCmdService")
public class DisruptorMemberCardCmdService extends DisruptorCmdServiceBase implements MemberCardCmdService {

    @Autowired
    private MemberCardCmdServiceImpl memberCardCmdService;

    @Override
    public ObatinSigningPrizeRecord obtainMemberCardSignPrize(String memberId, SignTypeEnum signPrizeType) throws OpportunityInvalidUsedException, OpportunityNotExistsExcetion {
        CommonCommand cmd = new CommonCommand(MemberCardCmdServiceImpl.class.getName(), "obtainMemberCardSignPrize", memberId, signPrizeType);
        DeferredResult<ObatinSigningPrizeRecord> deferredResult = publishEvent(this.disruptorFactory.getCoreCmdDisruptor(), cmd, new Callable<ObatinSigningPrizeRecord>() {
            @Override
            public ObatinSigningPrizeRecord call() throws Exception {
                return memberCardCmdService.obtainMemberCardSignPrize(cmd.getParameter(), cmd.getParameter());
            }
        });
        try {
            return deferredResult.getResult();
        } catch (Exception e) {
            if (e instanceof OpportunityInvalidUsedException) {
                throw (OpportunityInvalidUsedException) e;
            } else if (e instanceof OpportunityNotExistsExcetion) {
                throw (OpportunityNotExistsExcetion) e;
            } else {
                throw new RuntimeException(e);
            }
        }
    }


}
