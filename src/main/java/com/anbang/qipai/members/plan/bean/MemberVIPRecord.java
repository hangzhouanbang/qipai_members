package com.anbang.qipai.members.plan.bean;

public class MemberVIPRecord {
	private String id;
	private String memberId;// 玩家id
	private String nickname;// 玩家昵称
	private long vipTime;// VIP时间
	private String summary;// 摘要
	private long rechargeTime;// 充值时间

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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public long getVipTime() {
		return vipTime;
	}

	public void setVipTime(long vipTime) {
		this.vipTime = vipTime;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public long getRechargeTime() {
		return rechargeTime;
	}

	public void setRechargeTime(long rechargeTime) {
		this.rechargeTime = rechargeTime;
	}

}
