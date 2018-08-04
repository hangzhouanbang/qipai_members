package com.anbang.qipai.members.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.members.msg.channel.OrdersSource;
import com.anbang.qipai.members.msg.msjobj.CommonMO;
import com.anbang.qipai.members.plan.bean.MemberOrder;

@EnableBinding(OrdersSource.class)
public class OrdersMsgService {
	@Autowired
	private OrdersSource ordersSource;

	public void createOrder(MemberOrder order) {
		CommonMO mo = new CommonMO();
		mo.setMsg("newOrder");
		mo.setData(order);
		ordersSource.orders().send(MessageBuilder.withPayload(mo).build());
	}

	public void updateOrderDeliveTime(MemberOrder order) {
		CommonMO mo = new CommonMO();
		mo.setMsg("trade over");
		mo.setData(order);
		ordersSource.orders().send(MessageBuilder.withPayload(mo).build());
	}
}
