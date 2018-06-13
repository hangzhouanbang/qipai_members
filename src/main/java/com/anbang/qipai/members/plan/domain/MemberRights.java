package com.anbang.qipai.members.plan.domain;

/**
 * 创建新会员的设置
 * 
 * @author neo
 *
 */
public class MemberRights {

	/**
	 * 永远为1，只是为了数据库查询方便
	 */
	private String id;
	
	private Integer signGoldNumber;//普通用户签到得金币数量
	
	private Integer goldForNewNember;//新用户注册赠送的金币数量
	
	private Integer shareIntegralNumber;//分享得积分数量
	
	private Integer shareGoldNumber;//分享得金币数量
	
	private Integer inviteIntegralNumber;//邀请得积分数量
	
	private float vipGrowIntegralSpeed;//会员积分增长速度
	private float planGrowIntegralSpeed;//普通会员增长速度
	
	private float vipGrowGradeSpeed;//会员等级增长速度

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getSignGoldNumber() {
		return signGoldNumber;
	}

	public void setSignGoldNumber(Integer signGoldNumber) {
		this.signGoldNumber = signGoldNumber;
	}

	public Integer getGoldForNewNember() {
		return goldForNewNember;
	}

	public void setGoldForNewNember(Integer goldForNewNember) {
		this.goldForNewNember = goldForNewNember;
	}

	public Integer getShareIntegralNumber() {
		return shareIntegralNumber;
	}

	public void setShareIntegralNumber(Integer shareIntegralNumber) {
		this.shareIntegralNumber = shareIntegralNumber;
	}

	public Integer getShareGoldNumber() {
		return shareGoldNumber;
	}

	public void setShareGoldNumber(Integer shareGoldNumber) {
		this.shareGoldNumber = shareGoldNumber;
	}

	public Integer getInviteIntegralNumber() {
		return inviteIntegralNumber;
	}

	public void setInviteIntegralNumber(Integer inviteIntegralNumber) {
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

	
}