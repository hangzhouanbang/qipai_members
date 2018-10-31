package com.anbang.qipai.members.cqrs.c.domain.sign;

public class SigningRaffleOpportunity {
    private final String memberId;
    private final long createTime;
    private boolean used;

    public SigningRaffleOpportunity(String memberId) {
        this.memberId = memberId;
        this.createTime = System.currentTimeMillis();
        this.used = false;
    }

    public String getMemberId() {
        return memberId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public boolean isUsed() {
        return used;
    }

    public void useOpportunity() throws OpportunityInvalidUsedException {
        if (isExpired()) {
            throw new OpportunityInvalidUsedException("奖励已过期");
        }
        if (this.used) {
            throw new OpportunityInvalidUsedException("您今天已经抽奖过");
        }
        this.used = true;
    }

    private boolean isExpired() {
        final long currentTimeAsDay = System.currentTimeMillis() / Constant.ONE_DAY_MS;
        final long createTimeAsDay = this.createTime / Constant.ONE_DAY_MS;
        return currentTimeAsDay != createTimeAsDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SigningRaffleOpportunity)) return false;

        SigningRaffleOpportunity that = (SigningRaffleOpportunity) o;

        if (createTime != that.createTime) return false;
        if (used != that.used) return false;
        return memberId.equals(that.memberId);
    }

    @Override
    public int hashCode() {
        int result = memberId.hashCode();
        result = 31 * result + (int) (createTime ^ (createTime >>> 32));
        result = 31 * result + (used ? 1 : 0);
        return result;
    }
}
