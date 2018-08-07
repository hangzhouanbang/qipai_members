package com.anbang.qipai.members.plan.bean.historicalresult;

import java.util.List;

public class MajiangHistoricalResult {
	private String dayingjiaId;
	private String datuhaoId;
	private List<MajiangJuPlayerResultVO> playerResultList;

	private long finishTime;

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

	public List<MajiangJuPlayerResultVO> getPlayerResultList() {
		return playerResultList;
	}

	public void setPlayerResultList(List<MajiangJuPlayerResultVO> playerResultList) {
		this.playerResultList = playerResultList;
	}

	public long getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}

}
