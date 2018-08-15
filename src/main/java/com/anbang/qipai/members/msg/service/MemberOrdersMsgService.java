package com.anbang.qipai.members.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.members.msg.channel.MemberOrdersSource;
import com.anbang.qipai.members.msg.msjobj.CommonMO;
import com.anbang.qipai.members.plan.bean.MemberOrder;

@EnableBinding(MemberOrdersSource.class)
public class MemberOrdersMsgService {
	@Autowired
	private MemberOrdersSource ordersSource;

	public void createOrder(MemberOrder order) {
		CommonMO mo = new CommonMO();
		mo.setMsg("newOrder");
		mo.setData(order);
		ordersSource.memberOrders().send(MessageBuilder.withPayload(mo).build());
	}

	public void orderFinished(MemberOrder order) {
		CommonMO mo = new CommonMO();
		mo.setMsg("order finished");
		mo.setData(order);
		ordersSource.memberOrders().send(MessageBuilder.withPayload(mo).build());
	}
}
