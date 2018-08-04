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
import com.anbang.qipai.members.plan.bean.historicalresult.MajiangJuPlayerResult;
import com.anbang.qipai.members.plan.service.MajiangHistoricalResultService;
import com.google.gson.Gson;

@EnableBinding(RuianMajiangJuResultSink.class)
public class RuianMajiangJuResultMsgReceiver {
	@Autowired
	private MajiangHistoricalResultService majiangHistoricalResultService;

	private Gson gson = new Gson();

	@StreamListener(RuianMajiangJuResultSink.RUIANMAJIANGJURESULT)
	public void recordMember(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		Map map = gson.fromJson(json, Map.class);
		if ("ruianmajiang juresult".equals(msg)) {
			MajiangHistoricalResult result = new MajiangHistoricalResult();
			result.setId((String) map.get("id"));
			result.setGameId((String) map.get("gameId"));
			Map resultMap=(Map) map.get("juResult");
			result.setDayingjiaId((String) resultMap.get("dayingjiaId"));
			result.setDatuhaoId((String) resultMap.get("datuhaoId"));
			List<MajiangJuPlayerResult> playerResultList=new ArrayList<>();
			List<Map> mapList=(List<Map>) resultMap.get("playerResultList");
//			mapList.forEach(()->playerResultList.add(new MajiangJuPlayerResult()));
			majiangHistoricalResultService.addMajiangHistoricalResult(result);
		}
	}
}
