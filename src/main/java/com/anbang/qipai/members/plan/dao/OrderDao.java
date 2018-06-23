package com.anbang.qipai.members.plan.dao;

import com.anbang.qipai.members.plan.domain.Order;

public interface OrderDao {

	public Order findOrderByOut_trade_no(String out_trade_no);

	public void addOrder(Order order);

	public Boolean updateOrderStatus(String out_trade_no, String status);

	public Boolean updateTransaction_id(String out_trade_no, String transaction_id);
	
	public Boolean updateDeliveTime(String out_trade_no, Long deliveTime);
}
