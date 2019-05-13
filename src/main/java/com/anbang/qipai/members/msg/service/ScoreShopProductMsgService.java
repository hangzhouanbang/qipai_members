package com.anbang.qipai.members.msg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.members.cqrs.q.dbo.ScoreShopProductDbo;
import com.anbang.qipai.members.msg.channel.source.ScoreShopProductDboSource;
import com.anbang.qipai.members.msg.msjobj.CommonMO;

@EnableBinding(ScoreShopProductDboSource.class)
public class ScoreShopProductMsgService {

	@Autowired
	private ScoreShopProductDboSource scoreShopProductDboSource;

	public void release(List<ScoreShopProductDbo> list) {
		CommonMO mo = new CommonMO();
		mo.setMsg("release");
		mo.setData(list);
		scoreShopProductDboSource.scoreShopProductDbo().send(MessageBuilder.withPayload(mo).build());
	}

	public void update(ScoreShopProductDbo dbo) {
		CommonMO mo = new CommonMO();
		mo.setMsg("update");
		mo.setData(dbo);
		scoreShopProductDboSource.scoreShopProductDbo().send(MessageBuilder.withPayload(mo).build());
	}
}
