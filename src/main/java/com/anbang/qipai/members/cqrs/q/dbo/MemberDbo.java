package com.anbang.qipai.members.cqrs.q.dbo;

public class MemberDbo {
	private String id;// 会员id
	private String nickname;// 会员昵称
	private String gender;// 会员性别:男:male,女:female
	private Boolean vip;// 是否VIP
	private Integer vipLevel;// VIP等级
	private Double vipScore;// VIP积分
	private Double cost;// 消费总额RMB
	private String headimgurl;// 头像url
	private String phone;// 会员手机
	private Integer onlineTime;// 在线时长
	private String loginIp;// 登录ip
	private Long lastLoginTime;// 最后登录时间
	private Long createTime;// 注册时间
	private Long vipEndTime;// VIP时间
	private MemberRights rights;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Boolean getVip() {
		return vip;
	}

	public void setVip(Boolean vip) {
		this.vip = vip;
	}

	public Integer getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(Integer vipLevel) {
		this.vipLevel = vipLevel;
	}

	public Double getVipScore() {
		return vipScore;
	}

	public void setVipScore(Double vipScore) {
		this.vipScore = vipScore;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getVipEndTime() {
		return vipEndTime;
	}

	public void setVipEndTime(Long vipEndTime) {
		this.vipEndTime = vipEndTime;
	}

	public MemberRights getRights() {
		return rights;
	}

	public void setRights(MemberRights rights) {
		this.rights = rights;
	}

	public Integer getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(Integer onlineTime) {
		this.onlineTime = onlineTime;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Long getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
}
