package com.anbang.qipai.members.web.vo;

public class DetailsVo {
	private boolean success = true;
	private String msg;
	private int gold;
	private int score;
	private Integer vipLevel;// VIP等级
	private String phone;// 手机号
	private long vipEndTime;// VIP剩余天数

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(Integer vipLevel) {
		this.vipLevel = vipLevel;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public long getVipEndTime() {
		return vipEndTime;
	}

	public void setVipEndTime(long vipEndTime) {
		this.vipEndTime = vipEndTime;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
