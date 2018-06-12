package com.anbang.qipai.members.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.members.msg.channel.GoldsSource;
import com.anbang.qipai.members.msg.msjobj.CommonMO;
import com.dml.accounting.AccountingRecord;

@EnableBinding(GoldsSource.class)
public class GoldsMsgService {

	@Autowired
	private GoldsSource goldsSource;

	public void withdraw(AccountingRecord record) {
		CommonMO mo = new CommonMO();
		mo.setMsg("accounting");
		mo.setData(record);
		goldsSource.golds().send(MessageBuilder.withPayload(mo).build());
	}

}
