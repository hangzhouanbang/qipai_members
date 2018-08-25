package com.anbang.qipai.members.cqrs.q.dbo;

public class MemberDbo {
	private String id;// 会员id
	private String nickname;// 会员昵称
	private String gender;// 会员性别:男:male,女:female
	private boolean vip;// 是否VIP
	private int vipLevel;// VIP等级
	private double vipScore;// VIP积分
	private String headimgurl;// 头像url
	private String phone;// 会员手机
	private long createTime;// 注册时间
	private long vipEndTime;// VIP时间
	private MemberRights rights;
	private String realName;// 真实姓名
	private String IDcard;// 身份证
	private boolean verifyUser;// 实名认证，true:通过认证,false:未通过认证
	private String onlineState;

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

	public boolean isVip() {
		return vip;
	}

	public void setVip(boolean vip) {
		this.vip = vip;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public double getVipScore() {
		return vipScore;
	}

	public void setVipScore(double vipScore) {
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

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getVipEndTime() {
		return vipEndTime;
	}

	public void setVipEndTime(long vipEndTime) {
		this.vipEndTime = vipEndTime;
	}

	public MemberRights getRights() {
		return rights;
	}

	public void setRights(MemberRights rights) {
		this.rights = rights;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIDcard() {
		return IDcard;
	}

	public void setIDcard(String iDcard) {
		IDcard = iDcard;
	}

	public boolean isVerifyUser() {
		return verifyUser;
	}

	public void setVerifyUser(boolean verifyUser) {
		this.verifyUser = verifyUser;
	}

	public String getOnlineState() {
		return onlineState;
	}

	public void setOnlineState(String onlineState) {
		this.onlineState = onlineState;
	}

}
