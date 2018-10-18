package com.anbang.qipai.members.cqrs.q.dbo;

public class MemberRights {

	private int signGoldNumber;// 签到得金币数量

	private int goldForNewNember;// 新用户注册赠送的金币数量

	private int scoreForNewNember;// 新用户注册赠送的积分数量

	private int inviteIntegralNumber;// 邀请得积分数量

	private float growIntegralSpeed;// 积分增长速度

	private float growGradeSpeed;// 会员等级增长速度

	private int goldForAgentInvite;// 填写邀请码赠送玉石数

	public int getSignGoldNumber() {
		return signGoldNumber;
	}

	public void setSignGoldNumber(int signGoldNumber) {
		this.signGoldNumber = signGoldNumber;
	}

	public int getGoldForNewNember() {
		return goldForNewNember;
	}

	public void setGoldForNewNember(int goldForNewNember) {
		this.goldForNewNember = goldForNewNember;
	}

	public int getScoreForNewNember() {
		return scoreForNewNember;
	}

	public void setScoreForNewNember(int scoreForNewNember) {
		this.scoreForNewNember = scoreForNewNember;
	}

	public int getInviteIntegralNumber() {
		return inviteIntegralNumber;
	}

	public void setInviteIntegralNumber(int inviteIntegralNumber) {
		this.inviteIntegralNumber = inviteIntegralNumber;
	}

	public float getGrowIntegralSpeed() {
		return growIntegralSpeed;
	}

	public void setGrowIntegralSpeed(float growIntegralSpeed) {
		this.growIntegralSpeed = growIntegralSpeed;
	}

	public float getGrowGradeSpeed() {
		return growGradeSpeed;
	}

	public void setGrowGradeSpeed(float growGradeSpeed) {
		this.growGradeSpeed = growGradeSpeed;
	}

	public int getGoldForAgentInvite() {
		return goldForAgentInvite;
	}

	public void setGoldForAgentInvite(int goldForAgentInvite) {
		this.goldForAgentInvite = goldForAgentInvite;
	}

}
