package com.anbang.qipai.members.plan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.members.plan.dao.OrderDao;
import com.anbang.qipai.members.plan.dao.RefundOrderDao;
import com.anbang.qipai.members.plan.domain.Order;
import com.anbang.qipai.members.plan.domain.RefundOrder;

@Service
public class RefundOrderService {

	@Autowired
	private RefundOrderDao refundOrderDao;

	@Autowired
	private OrderDao orderDao;

	public RefundOrder addRefundOrder(String out_trade_no, String refund_fee, String refund_desc) {
		RefundOrder refundOrder = new RefundOrder();
		refundOrder.setOut_refund_no("");
		refundOrder.setOut_trade_no(out_trade_no);
		refundOrder.setStatus(0);
		refundOrder.setRefund_fee(refund_fee);
		refundOrder.setRefund_desc(refund_desc);
		Order order = orderDao.findOrderByOut_trade_no(out_trade_no);
		refundOrder.setTransaction_id(order.getTransaction_id());
		refundOrder.setMemberId(order.getMemberId());
		refundOrder.setNickname(order.getNickname());
		refundOrder.setTotal_fee(Integer.toString((int) (100 * order.getClubCardPrice() * order.getNumber())));
		refundOrder.setCreateTime(System.currentTimeMillis());
		refundOrderDao.addRefundOrder(refundOrder);
		return refundOrder;
	}

	public RefundOrder findRefundOrderById(String out_refund_no) {
		return refundOrderDao.findRefundOrderById(out_refund_no);
	}

	public Boolean updateRefundOrderStatus(String out_refund_no, String refund_id) {
		RefundOrder refund = refundOrderDao.findRefundOrderById(out_refund_no);
		if (refund.getStatus() == 0) {
			return refundOrderDao.updateRefund_id(out_refund_no, refund_id);
		}
		return false;
	}
}
