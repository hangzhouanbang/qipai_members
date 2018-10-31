package com.anbang.qipai.members.cqrs.c.domain.vip;

public class VIPAccount {
    private final String memberId;
    private final VIPEnum vipLevel;

    public VIPAccount(String memberId, VIPEnum vipLevel) {
        this.memberId = memberId;
        this.vipLevel = vipLevel;
    }

    public String getMemberId() {
        return memberId;
    }

    public VIPEnum getVipLevel() {
        return vipLevel;
    }

}
