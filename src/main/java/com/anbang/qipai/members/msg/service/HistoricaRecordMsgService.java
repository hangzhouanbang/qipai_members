package com.anbang.qipai.members.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.members.msg.channel.HistoricaRecordSoure;
import com.anbang.qipai.members.plan.domain.historicalrecord.MemberHistoricalRecord;
import com.anbang.qipai.members.web.vo.CommonVO;

@EnableBinding(HistoricaRecordSoure.class)
public class HistoricaRecordMsgService {
	
	@Autowired
	private HistoricaRecordSoure historicaRecordSoure;
	
	public void createHistoricaRecord(MemberHistoricalRecord memberHistoricalRecord) {
		CommonVO vo = new CommonVO();
		vo.setMsg("createHistoricaRecord");
		vo.setData(memberHistoricalRecord);
		historicaRecordSoure.historicarecord().send(MessageBuilder.withPayload(vo).build());
	}
	
}
