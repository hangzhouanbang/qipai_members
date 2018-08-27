package com.anbang.qipai.members.plan.bean;

/**
 * 管理员设置的vip等级需要的金额
 * 
 * @author 程佳 2018.6.21
 **/
public class MemberGrade {

	private String id;

	private double vip1;// vip1需要的金额

	private double vip2;// vip2需要的金额

	private double vip3;// vip3需要的金额

	private double vip4;// vip4需要的金额

	private double vip5;// vip5需要的金额

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getVip1() {
		return vip1;
	}

	public void setVip1(double vip1) {
		this.vip1 = vip1;
	}

	public double getVip2() {
		return vip2;
	}

	public void setVip2(double vip2) {
		this.vip2 = vip2;
	}

	public double getVip3() {
		return vip3;
	}

	public void setVip3(double vip3) {
		this.vip3 = vip3;
	}

	public double getVip4() {
		return vip4;
	}

	public void setVip4(double vip4) {
		this.vip4 = vip4;
	}

	public double getVip5() {
		return vip5;
	}

	public void setVip5(double vip5) {
		this.vip5 = vip5;
	}

	public double getLevel(int level) {
		switch (level) {
		case 0:
			return vip1;
		case 1:
			return vip2;
		case 2:
			return vip3;
		case 3:
			return vip4;
		case 4:
			return vip5;
		default:
			return Double.MAX_VALUE;
		}

	}
}
