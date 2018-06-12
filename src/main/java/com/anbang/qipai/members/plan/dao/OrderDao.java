package com.anbang.qipai.members.plan.dao;

import com.anbang.qipai.members.plan.domain.Order;

public interface OrderDao {

	public void addOrder(Order order);

	public Boolean updateOrder(String orderId, int status);
}
