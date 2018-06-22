package com.anbang.qipai.members.cqrs.q.dbo;

import java.math.BigDecimal;


public class MemberRechargeRecordDbo {
	
	private String id;
	
	private String memberId;//会员id
	
	private String memberGrade;//会员等级
	
	private long shortage;//缺少多少钱到下一个等级
	
	private BigDecimal ProgressBar;//进度条，页面显示需要的数据
	
	private long rechargeAmount;//累积充值金额,充值最低都是整数

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberGrade() {
		return memberGrade;
	}

	public void setMemberGrade(String memberGrade) {
		this.memberGrade = memberGrade;
	}

	public long getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(long rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	public long getShortage() {
		return shortage;
	}

	public void setShortage(long shortage) {
		this.shortage = shortage;
	}

	public BigDecimal getProgressBar() {
		return ProgressBar;
	}

	public void setProgressBar(BigDecimal progressBar) {
		ProgressBar = progressBar;
	}

	

}
