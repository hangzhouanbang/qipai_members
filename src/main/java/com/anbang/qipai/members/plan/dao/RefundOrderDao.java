package com.anbang.qipai.members.plan.dao;

import com.anbang.qipai.members.plan.domain.RefundOrder;

public interface RefundOrderDao {

	void addRefundOrder(RefundOrder refund);

	RefundOrder findRefundOrderById(String out_refund_no);

	Boolean updateRefund_id(String out_refund_no, String refund_id);
}
