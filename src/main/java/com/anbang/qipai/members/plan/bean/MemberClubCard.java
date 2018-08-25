package com.anbang.qipai.members.plan.bean;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "clubcard")
public class MemberClubCard {
	private String id;// 会员卡id
	private String name;// 会员卡名称
	private double price;// 会员卡价格
	private int gold;// 买会员卡赠送金币
	private int score;// 买会员卡赠送积分
	private long time;// 会员延长时间

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

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
