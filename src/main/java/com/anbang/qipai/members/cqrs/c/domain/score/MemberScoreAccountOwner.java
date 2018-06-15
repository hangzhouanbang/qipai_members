package com.anbang.qipai.members.cqrs.c.domain.score;

import com.dml.accounting.AccountOwner;

public class MemberScoreAccountOwner implements AccountOwner {

	private String memberId;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

}
