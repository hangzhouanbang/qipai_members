package com.anbang.qipai.members.web.vo;

/**
 * 拉取会员信息的view obj
 * 
 * @author neo
 *
 */
public class MemberVO {

	private boolean success;

	private String memberId;

	private String nickname;

	private String headimgurl;

	private int gold;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
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

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	@Override
	public String toString() {
		return "MemberVO [success=" + success + ", memberId=" + memberId + ", nickname=" + nickname + ", headimgurl="
				+ headimgurl + ", gold=" + gold + "]";
	}

}
