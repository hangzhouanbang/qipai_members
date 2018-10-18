package com.anbang.qipai.members.plan.bean;

import com.anbang.qipai.members.cqrs.q.dbo.MemberRights;

/**
 * 创建新会员的设置
 * 
 * @author neo
 *
 */
public class MemberRightsConfiguration {

	/**
	 * 永远为1，只是为了数据库查询方便
	 */
	private String id;

	private int signGoldNumber;// 普通用户签到得金币数量

	private int goldForNewNember;// 新用户注册赠送的金币数量

	private int scoreForNewNember;// 新用户注册赠送的积分数量

	private int inviteIntegralNumber;// 邀请得积分数量

	private float vipGrowIntegralSpeed;// 会员积分增长速度
	private float planGrowIntegralSpeed;// 普通会员积分增长速度

	private int goldForAgentInvite;// 填写邀请码赠送玉石数

	private float vipGrowGradeSpeed;// 会员等级增长速度

	public MemberRights generateRightsForPlanMembers() {
		MemberRights rights = new MemberRights();
		rights.setGoldForNewNember(goldForNewNember);
		rights.setGrowIntegralSpeed(planGrowIntegralSpeed);
		rights.setInviteIntegralNumber(inviteIntegralNumber);
		rights.setScoreForNewNember(scoreForNewNember);
		rights.setSignGoldNumber(signGoldNumber);
		rights.setGoldForAgentInvite(goldForAgentInvite);
		return rights;
	}

	public MemberRights generateRightsForVipMembers() {
		MemberRights rights = new MemberRights();
		rights.setGoldForNewNember(goldForNewNember);
		rights.setGrowGradeSpeed(vipGrowGradeSpeed);
		rights.setGrowIntegralSpeed(vipGrowIntegralSpeed);
		rights.setInviteIntegralNumber(inviteIntegralNumber);
		rights.setScoreForNewNember(scoreForNewNember);
		rights.setSignGoldNumber(signGoldNumber);
		rights.setGoldForAgentInvite(goldForAgentInvite);
		return rights;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public int getInviteIntegralNumber() {
		return inviteIntegralNumber;
	}

	public void setInviteIntegralNumber(int inviteIntegralNumber) {
		this.inviteIntegralNumber = inviteIntegralNumber;
	}

	public float getVipGrowIntegralSpeed() {
		return vipGrowIntegralSpeed;
	}

	public void setVipGrowIntegralSpeed(float vipGrowIntegralSpeed) {
		this.vipGrowIntegralSpeed = vipGrowIntegralSpeed;
	}

	public float getPlanGrowIntegralSpeed() {
		return planGrowIntegralSpeed;
	}

	public void setPlanGrowIntegralSpeed(float planGrowIntegralSpeed) {
		this.planGrowIntegralSpeed = planGrowIntegralSpeed;
	}

	public float getVipGrowGradeSpeed() {
		return vipGrowGradeSpeed;
	}

	public void setVipGrowGradeSpeed(float vipGrowGradeSpeed) {
		this.vipGrowGradeSpeed = vipGrowGradeSpeed;
	}

	public int getGoldForAgentInvite() {
		return goldForAgentInvite;
	}

	public void setGoldForAgentInvite(int goldForAgentInvite) {
		this.goldForAgentInvite = goldForAgentInvite;
	}

	public int getScoreForNewNember() {
		return scoreForNewNember;
	}

	public void setScoreForNewNember(int scoreForNewNember) {
		this.scoreForNewNember = scoreForNewNember;
	}

}