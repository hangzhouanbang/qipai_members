package com.anbang.qipai.members.plan.domain;

public class Order {
	private String id;
	private Long out_trade_no;// 商户订单流水号
	private String pay_type;// 支付方式，支付宝或微信
	private String transaction_id;// 订单号
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
	private Double totalamount;// 总价
	private Long createTime;// 订单创建时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(Long out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Double getTotalamount() {
		return totalamount;
	}

	public void setTotalamount(Double totalamount) {
		this.totalamount = totalamount;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

}
