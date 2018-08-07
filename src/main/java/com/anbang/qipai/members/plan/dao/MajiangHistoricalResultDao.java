package com.anbang.qipai.members.plan.dao;

import java.util.List;

import com.anbang.qipai.members.plan.bean.historicalresult.MajiangHistoricalResult;

public interface MajiangHistoricalResultDao {

	void addMajiangHistoricalResult(MajiangHistoricalResult result);

	List<MajiangHistoricalResult> findMajiangHistoricalResultByMemberId(int page, int size, String memberId);

	long getAmountByMemberId(String memberId);

	MajiangHistoricalResult findMajiangHistoricalResultById(String id);
}
