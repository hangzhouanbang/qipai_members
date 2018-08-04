package com.anbang.qipai.members.plan.service;

import org.springframework.stereotype.Service;

import com.anbang.qipai.members.plan.bean.historicalresult.MajiangHistoricalResult;
import com.anbang.qipai.members.plan.dao.MajiangHistoricalResultDao;

@Service
public class MajiangHistoricalResultService {

	private MajiangHistoricalResultDao majiangHistoricalResultDao;

	public void addMajiangHistoricalResult(MajiangHistoricalResult result) {
		majiangHistoricalResultDao.addMajiangHistoricalResult(result);
	}
}
