package com.anbang.qipai.members.msg.receiver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.members.msg.channel.RuianMajiangJuResultSink;
import com.anbang.qipai.members.msg.msjobj.CommonMO;
import com.anbang.qipai.members.plan.bean.historicalresult.MajiangHistoricalResult;
import com.anbang.qipai.members.plan.bean.historicalresult.MajiangJuPlayerResultVO;
import com.anbang.qipai.members.plan.service.MajiangHistoricalResultService;
import com.google.gson.Gson;

@EnableBinding(RuianMajiangJuResultSink.class)
public class RuianMajiangJuResultMsgReceiver {
	@Autowired
	private MajiangHistoricalResultService majiangHistoricalResultService;

	private Gson gson = new Gson();

	@StreamListener(RuianMajiangJuResultSink.RUIANMAJIANGJURESULT)
	public void recordMajiangHistoricalResult(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		Map map = gson.fromJson(json, Map.class);
		if ("ruianmajiang juresult".equals(msg)) {
			MajiangHistoricalResult majiangHistoricalResult = new MajiangHistoricalResult();
			majiangHistoricalResult.setDayingjiaId((String) map.get("dayingjiaId"));
			majiangHistoricalResult.setDatuhaoId((String) map.get("datuhaoId"));

			List<MajiangJuPlayerResultVO> juPlayerResultList = new ArrayList<>();
			((List) map.get("playerResultList")).forEach(
					(juPlayerResult) -> juPlayerResultList.add(new MajiangJuPlayerResultVO((Map) juPlayerResult)));
			majiangHistoricalResult.setPlayerResultList(juPlayerResultList);

			Map lastPanResultMap = (Map) map.get("lastPanResult");
			majiangHistoricalResult.setFinishTime(((Double) lastPanResultMap.get("finishTime")).longValue());

			majiangHistoricalResultService.addMajiangHistoricalResult(majiangHistoricalResult);
		}
	}
}
