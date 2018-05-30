package com.anbang.qipai.members.cqrs.c.domain.gold;

import com.dml.accounting.AccountOwner;

public class MemberGoldAccountOwner implements AccountOwner {

	private String memberId;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

}
