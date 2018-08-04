package com.anbang.qipai.members.plan.bean.historicalresult;

import java.util.List;

public class MajiangHistoricalResult {

	private String id;

	private String gameId;

	private String dayingjiaId;

	private String datuhaoId;

	private List<MajiangJuPlayerResult> playerResultList;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getDayingjiaId() {
		return dayingjiaId;
	}

	public void setDayingjiaId(String dayingjiaId) {
		this.dayingjiaId = dayingjiaId;
	}

	public String getDatuhaoId() {
		return datuhaoId;
	}

	public void setDatuhaoId(String datuhaoId) {
		this.datuhaoId = datuhaoId;
	}

	public List<MajiangJuPlayerResult> getPlayerResultList() {
		return playerResultList;
	}

	public void setPlayerResultList(List<MajiangJuPlayerResult> playerResultList) {
		this.playerResultList = playerResultList;
	}

}
