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
	private String idCard;// 身份证
	private boolean verifyUser;// 实名认证，true:通过认证,false:未通过认证
	private boolean bindAgent;// 绑定推广员，true:绑定,false:未绑定
	private String agentId;// 推广员id
	private int gold;// 金币
	private int score;// 积分
	private double cost;// 累计消费

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

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public boolean isVerifyUser() {
		return verifyUser;
	}

	public void setVerifyUser(boolean verifyUser) {
		this.verifyUser = verifyUser;
	}

	public boolean isBindAgent() {
		return bindAgent;
	}

	public void setBindAgent(boolean bindAgent) {
		this.bindAgent = bindAgent;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
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

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}
}
