package com.anbang.qipai.members.cqrs.c.domain.phonefee;

import com.dml.accounting.AccountOwner;

public class MemberPhoneFeeAccountOwner implements AccountOwner {
    private String memberId;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}
