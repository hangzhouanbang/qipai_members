package com.anbang.qipai.members.cqrs.c.domain.giftscore;

import com.dml.accounting.AccountOwner;

public class MemberGiftScoreAccountOwner implements AccountOwner {
    private String memberId;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}
