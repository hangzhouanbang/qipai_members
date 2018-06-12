package com.anbang.qipai.members.plan.domain;

public class Order {
	private String id;
	private String out_trade_no;
	private String transaction_id;
	private Integer status;// 0:未支付,1:支付成功,-1:支付失敗
	private String memberId;
	private String nickname;
	private String clubCardId;
	private String clubCardName;
	private Double clubCardPrice;// 会员卡单价
	private Integer number;// 购买数量
	private Integer gold;// 单张会员卡赠送的金币
	private Integer score;// 单张会员卡赠送的积分
	private Long vipTime;// 单张会员卡赠送的VIP时间
	private Long createTime;// 订单创建时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getClubCardId() {
		return clubCardId;
	}

	public void setClubCardId(String clubCardId) {
		this.clubCardId = clubCardId;
	}

	public String getClubCardName() {
		return clubCardName;
	}

	public void setClubCardName(String clubCardName) {
		this.clubCardName = clubCardName;
	}

	public Double getClubCardPrice() {
		return clubCardPrice;
	}

	public void setClubCardPrice(Double clubCardPrice) {
		this.clubCardPrice = clubCardPrice;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getGold() {
		return gold;
	}

	public void setGold(Integer gold) {
		this.gold = gold;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Long getVipTime() {
		return vipTime;
	}

	public void setVipTime(Long vipTime) {
		this.vipTime = vipTime;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
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

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

}
