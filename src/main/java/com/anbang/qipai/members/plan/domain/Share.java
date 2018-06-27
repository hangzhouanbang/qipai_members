package com.anbang.qipai.members.plan.domain;

public class Share {

	private String id;//这里的id等于会员id
	
	private Integer wxFriendsFrequency;//微信好友领取次数
	
	private Integer wxFirendsCircleFrequency;//微信朋友圈次数

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getWxFriendsFrequency() {
		return wxFriendsFrequency;
	}

	public void setWxFriendsFrequency(Integer wxFriendsFrequency) {
		this.wxFriendsFrequency = wxFriendsFrequency;
	}

	public Integer getWxFirendsCircleFrequency() {
		return wxFirendsCircleFrequency;
	}

	public void setWxFirendsCircleFrequency(Integer wxFirendsCircleFrequency) {
		this.wxFirendsCircleFrequency = wxFirendsCircleFrequency;
	}

	
}
