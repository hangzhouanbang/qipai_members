package com.anbang.qipai.members.web.vo;

import com.anbang.qipai.members.plan.bean.MemberClubCard;

public class MemberClubCardVO extends MemberClubCard {
	private boolean hasNotBuy;
	private double distCountPrice;

	public boolean isHasNotBuy() {
		return hasNotBuy;
	}

	public void setHasNotBuy(boolean hasNotBuy) {
		this.hasNotBuy = hasNotBuy;
	}

	public double getDistCountPrice() {
		return distCountPrice;
	}

	public void setDistCountPrice(double distCountPrice) {
		this.distCountPrice = distCountPrice;
	}

}
