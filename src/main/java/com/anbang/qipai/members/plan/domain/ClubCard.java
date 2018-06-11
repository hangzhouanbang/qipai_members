package com.anbang.qipai.members.plan.domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="clubcard")
public class ClubCard {

	private String id;// 会员卡id
	private String name;// 会员卡名称
	private Double price;// 会员卡价格
	private Long time;// 会员延长时间

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

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "ClubCard [id=" + id + ", name=" + name + ", price=" + price + ", time=" + time + "]";
	}

}
