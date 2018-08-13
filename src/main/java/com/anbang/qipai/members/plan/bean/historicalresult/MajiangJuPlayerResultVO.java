package com.anbang.qipai.members.plan.bean.historicalresult;

import java.util.Map;

public class MajiangJuPlayerResultVO {
	private String playerId;
	private String nickname;
	private String headimgurl;
	private int huCount;
	private int caishenCount;
	private int dapaoCount;
	private int maxHushu;
	private int totalScore;

	public MajiangJuPlayerResultVO(Map juPlayerResult) {
		this.playerId = (String) juPlayerResult.get("playerId");
		this.nickname = (String) juPlayerResult.get("nickname");
		this.headimgurl = (String) juPlayerResult.get("headimgurl");
		this.huCount = ((Double) juPlayerResult.get("huCount")).intValue();
		this.caishenCount = ((Double) juPlayerResult.get("caishenCount")).intValue();
		this.dapaoCount = ((Double) juPlayerResult.get("dapaoCount")).intValue();
		this.maxHushu = ((Double) juPlayerResult.get("maxHushu")).intValue();
		this.totalScore = ((Double) juPlayerResult.get("totalScore")).intValue();
	}

	public MajiangJuPlayerResultVO() {

	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
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

	public int getHuCount() {
		return huCount;
	}

	public void setHuCount(int huCount) {
		this.huCount = huCount;
	}

	public int getCaishenCount() {
		return caishenCount;
	}

	public void setCaishenCount(int caishenCount) {
		this.caishenCount = caishenCount;
	}

	public int getDapaoCount() {
		return dapaoCount;
	}

	public void setDapaoCount(int dapaoCount) {
		this.dapaoCount = dapaoCount;
	}

	public int getMaxHushu() {
		return maxHushu;
	}

	public void setMaxHushu(int maxHushu) {
		this.maxHushu = maxHushu;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

}
