package com.anbang.qipai.members.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.members.msg.channel.source.ScoreProductRecordSource;
import com.anbang.qipai.members.msg.msjobj.CommonMO;
import com.anbang.qipai.members.plan.bean.ScoreProductRecord;

@EnableBinding(ScoreProductRecordSource.class)
public class ScoreProductRecordMsgService {

	@Autowired
	private ScoreProductRecordSource scoreProductRecordSource;

	public void addRecord(ScoreProductRecord record) {
		CommonMO mo = new CommonMO();
		mo.setMsg("add record");
		mo.setData(record);
		scoreProductRecordSource.scoreProductRecord().send(MessageBuilder.withPayload(mo).build());
	}

	public void finishRecord(ScoreProductRecord record) {
		CommonMO mo = new CommonMO();
		mo.setMsg("finish record");
		mo.setData(record);
		scoreProductRecordSource.scoreProductRecord().send(MessageBuilder.withPayload(mo).build());
	}
}
