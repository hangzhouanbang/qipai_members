package com.anbang.qipai.members.web.vo;

public class MemberClubCardVO {
	private String id;// 会员卡id
	private String name;// 会员卡名称
	private double price;// 会员卡价格
	private double discount;// 首次折扣
	private double originalPrice;// 原价
	private int gold;// 买会员卡赠送金币
	private int score;// 买会员卡赠送积分
	private String time;// 会员延长时间*天
	private boolean hasNotBuy;// 是否首次

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(double originalPrice) {
		this.originalPrice = originalPrice;
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public boolean isHasNotBuy() {
		return hasNotBuy;
	}

	public void setHasNotBuy(boolean hasNotBuy) {
		this.hasNotBuy = hasNotBuy;
	}

}
