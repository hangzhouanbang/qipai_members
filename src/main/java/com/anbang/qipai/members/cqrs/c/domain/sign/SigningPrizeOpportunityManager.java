package com.anbang.qipai.members.cqrs.c.domain.sign;


import com.anbang.qipai.members.cqrs.c.domain.vip.VIPEnum;

import java.util.HashMap;
import java.util.Map;

public class SigningPrizeOpportunityManager {

    private final Map<String, Map<SignTypeEnum, ObtainSignPrizeOpportunity>> memberOpportunitiesMap = new HashMap<>();

    private Map<SignTypeEnum, ObtainSignPrizeOpportunity> getOrCreateOpportunitySet(String memberId) {
        Map<SignTypeEnum, ObtainSignPrizeOpportunity> opportunitySet = this.memberOpportunitiesMap.get(memberId);
        if (opportunitySet == null) {
            opportunitySet = new HashMap<>();
            this.memberOpportunitiesMap.put(memberId, opportunitySet);
        }
        return opportunitySet;
    }

    /**
     * @param memberId
     * @param continuousSignDays 连续签到的天数
     */
    public ObtainSignPrizeOpportunity addMemberOpportunity(String memberId, int continuousSignDays, long signTime, VIPEnum vipLevel) {
        if (continuousSignDays == 1) {
            this.removeBeforeOpportunity(memberId);
        }
        ObtainSignPrizeOpportunity obtainSignPrizeOpportunity = null;
        SignTypeEnum type = SignTypeEnum.of(continuousSignDays);
        if (type != null) {
            final Map<SignTypeEnum, ObtainSignPrizeOpportunity> memberOpporMap = this.getOrCreateOpportunitySet(memberId);
            obtainSignPrizeOpportunity = new ObtainSignPrizeOpportunity(memberId, type, signTime, vipLevel);
            memberOpporMap.put(type, obtainSignPrizeOpportunity);
        }
        return obtainSignPrizeOpportunity;
    }

    private void removeBeforeOpportunity(String memberId) {
        Map<SignTypeEnum, ObtainSignPrizeOpportunity> memberOpporMap = this.getOrCreateOpportunitySet(memberId);
        memberOpporMap.clear();
    }


    public ObtainSignPrizeOpportunity useSignPrizeOpportunity(String memberId, SignTypeEnum signPrizeType) throws OpportunityInvalidUsedException, OpportunityNotExistsExcetion {
        Map<SignTypeEnum, ObtainSignPrizeOpportunity> memberOpporMap = this.getOrCreateOpportunitySet(memberId);
        ObtainSignPrizeOpportunity obtainSignPrizeOpportunity = memberOpporMap.get(signPrizeType);
        if (obtainSignPrizeOpportunity == null) {
            throw new OpportunityNotExistsExcetion("您没有领取该奖励的机会");
        }
        obtainSignPrizeOpportunity.use();
        return obtainSignPrizeOpportunity;
    }


}
