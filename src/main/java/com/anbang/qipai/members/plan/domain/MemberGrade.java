package com.anbang.qipai.members.plan.domain;

/** 管理员设置的vip等级需要的金额
 * 
 * @author 程佳 2018.6.21
 * **/
public class MemberGrade {
	
	private String id;
	
	private long vip1;//vip1需要的金额
	
	private long vip2;//vip2需要的金额
	
	private long vip3;//vip3需要的金额
	
	private long vip4;//vip4需要的金额
	
	private long vip5;//vip5需要的金额

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getVip1() {
		return vip1;
	}

	public void setVip1(long vip1) {
		this.vip1 = vip1;
	}

	public long getVip2() {
		return vip2;
	}

	public void setVip2(long vip2) {
		this.vip2 = vip2;
	}

	public long getVip3() {
		return vip3;
	}

	public void setVip3(long vip3) {
		this.vip3 = vip3;
	}

	public long getVip4() {
		return vip4;
	}

	public void setVip4(long vip4) {
		this.vip4 = vip4;
	}

	public long getVip5() {
		return vip5;
	}

	public void setVip5(long vip5) {
		this.vip5 = vip5;
	}

}
