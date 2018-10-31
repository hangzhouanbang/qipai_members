package com.anbang.qipai.members.cqrs.c.domain.sign;

import com.anbang.qipai.members.cqrs.c.domain.vip.VIPEnum;

public class ObtainSignPrizeOpportunity {
    private String memberId;
    private SignTypeEnum signPrizeType;
    private long createTime;
    private VIPEnum vipLevel;
    private boolean used;

    public ObtainSignPrizeOpportunity(String memberId, SignTypeEnum signPrizeType, long createTime, VIPEnum vipLevel) {
        this.memberId = memberId;
        this.signPrizeType = signPrizeType;
        this.createTime = createTime;
        this.vipLevel = vipLevel;
        this.used = false;
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

    public VIPEnum getVipLevel() {
        return vipLevel;
    }

    public void use() throws OpportunityInvalidUsedException {
        if (used) {
            throw new OpportunityInvalidUsedException("您已经领取过该奖品");
        }
        this.used = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ObtainSignPrizeOpportunity)) return false;

        ObtainSignPrizeOpportunity that = (ObtainSignPrizeOpportunity) o;

        if (createTime != that.createTime) return false;
        if (used != that.used) return false;
        if (!memberId.equals(that.memberId)) return false;
        return signPrizeType == that.signPrizeType;
    }

    @Override
    public int hashCode() {
        int result = memberId.hashCode();
        result = 31 * result + signPrizeType.hashCode();
        result = 31 * result + (int) (createTime ^ (createTime >>> 32));
        result = 31 * result + (used ? 1 : 0);
        return result;
    }


}
