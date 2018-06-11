package com.anbang.qipai.members.web.vo;

public class DetailsVo {
	private boolean success = true;
	private String msg;
	private Integer vipLevel;// VIP等级
	private String phone;// 手机号
	private String vipEndTime;// VIP剩余天数

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

	public String getVipEndTime() {
		return vipEndTime;
	}

	public void setVipEndTime(String vipEndTime) {
		this.vipEndTime = vipEndTime;
	}

}
