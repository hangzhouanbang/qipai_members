package com.anbang.qipai.members.plan.dao;

import com.anbang.qipai.members.plan.domain.Order;

public interface OrderDao {

	public void addOrder(Order order);

	public Boolean updateOrder(String out_trade_no, int status);
}
