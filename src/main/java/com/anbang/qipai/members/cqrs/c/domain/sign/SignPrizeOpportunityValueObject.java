package com.anbang.qipai.members.cqrs.c.domain.sign;

public class SignPrizeOpportunityValueObject {

    private final String memberId;
    private final SignTypeEnum signPrizeType;
    private final long createTime;
    private final boolean used;

    public SignPrizeOpportunityValueObject(ObtainSignPrizeOpportunity obtainSignPrizeOpportunity) {
        this.memberId = obtainSignPrizeOpportunity.getMemberId();
        this.signPrizeType = obtainSignPrizeOpportunity.getSignPrizeType();
        this.createTime = obtainSignPrizeOpportunity.getCreateTime();
        this.used = obtainSignPrizeOpportunity.isUsed();
    }

    public String getMemberId() {
        return memberId;
    }

    public SignTypeEnum getSignPrizeType() {
        return signPrizeType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public boolean isUsed() {
        return used;
    }


}
