package com.anbang.qipai.members.cqrs.c.domain.sign;

import java.util.HashMap;
import java.util.Map;

public class SigningRaffleOpportunityManager {

    private Map<String, SigningRaffleOpportunity> memberOpportunityMap = new HashMap<>();

    private static final int DAY_MS = 60 * 60 * 24 * 1000;

    public SigningRaffleOpportunity addMemberOpportunity(String memberId) {
        this.removeExpiredOpportunity(memberId);
        SigningRaffleOpportunity opportunity = memberOpportunityMap.get(memberId);
        if (opportunity == null) {
            SigningRaffleOpportunity signingRaffleOpportunity = new SigningRaffleOpportunity(memberId);
            this.memberOpportunityMap.put(memberId, signingRaffleOpportunity);
            return signingRaffleOpportunity;
        }
        return null;
    }

    private SigningRaffleOpportunity removeExpiredOpportunity(String memberId) {
        SigningRaffleOpportunity opportunity = memberOpportunityMap.get(memberId);
        if (opportunity != null) {
            final long currentTime = System.currentTimeMillis();
            final long lastOpportunityDay = opportunity.getCreateTime() / DAY_MS;
            final long currentOpportunityDay = currentTime / DAY_MS;
            if (currentOpportunityDay > lastOpportunityDay) {
                memberOpportunityMap.remove(memberId);
                return opportunity;
            }
        }
        return null;
    }

    public void useOpportunity(String memberId) throws OpportunityInvalidUsedException, NotSignedException {
        this.removeExpiredOpportunity(memberId);
        SigningRaffleOpportunity opportunity = this.memberOpportunityMap.get(memberId);
        if (opportunity == null) {
            throw new NotSignedException("请先签到才有抽奖机会");
        }
        opportunity.useOpportunity();
    }


}
