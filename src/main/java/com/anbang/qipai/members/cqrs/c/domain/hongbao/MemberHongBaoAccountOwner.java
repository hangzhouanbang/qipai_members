package com.anbang.qipai.members.cqrs.c.domain.hongbao;

import com.dml.accounting.AccountOwner;

public class MemberHongBaoAccountOwner implements AccountOwner {
    private String memberId;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }


}
