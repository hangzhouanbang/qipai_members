package com.anbang.qipai.members.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.members.msg.channel.source.ProductTypeSource;
import com.anbang.qipai.members.msg.msjobj.CommonMO;
import com.anbang.qipai.members.plan.bean.ProductType;

@EnableBinding(ProductTypeSource.class)
public class ProductTypeMsgService {

	@Autowired
	private ProductTypeSource productTypeSource;

	public void addType(ProductType type) {
		CommonMO mo = new CommonMO();
		mo.setMsg("add type");
		mo.setData(type);
		productTypeSource.productType().send(MessageBuilder.withPayload(mo).build());
	}

	public void removeType(String[] ids) {
		CommonMO mo = new CommonMO();
		mo.setMsg("remove type");
		mo.setData(ids);
		productTypeSource.productType().send(MessageBuilder.withPayload(mo).build());
	}

	public void updateType(ProductType type) {
		CommonMO mo = new CommonMO();
		mo.setMsg("update type");
		mo.setData(type);
		productTypeSource.productType().send(MessageBuilder.withPayload(mo).build());
	}
}
