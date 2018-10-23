package com.anbang.qipai.members.plan.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.plan.bean.MemberOrder;
import com.anbang.qipai.members.plan.dao.MemberOrderDao;

@Component
public class MemberOrderService {

	@Autowired
	private MemberOrderDao memberOrderDao;

	public MemberOrder addMemberOrder(String payerId, String payerName, String receiverId, String receiverName,
			String productId, String productName, double productPrice, int gold, int score, long time, int number,
			String payType, String reqIp) {
		String id = UUID.randomUUID().toString().replace("-", "");
		MemberOrder order = new MemberOrder();
		order.setId(id);
		order.setPay_type(payType);
		order.setStatus("WAIT_BUYER_PAY");
		order.setPayerId(payerId);
		order.setPayerName(payerName);
		order.setReceiverId(receiverId);
		order.setReceiverName(receiverName);
		order.setProductId(productId);
		order.setProductName(productName);
		order.setProductPrice(new BigDecimal(productPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		order.setNumber(number);
		order.setGold(gold);
		order.setScore(score);
		order.setVipTime(time);
		order.setTotalamount(order.getProductPrice() * number);
		order.setReqIP(reqIp);
		order.setCreateTime(System.currentTimeMillis());
		memberOrderDao.addMemberOrder(order);
		return order;
	}

	public MemberOrder orderFinished(String id, String transaction_id, String status, long deliveTime) {
		memberOrderDao.orderFinished(id, transaction_id, status, deliveTime);
		return memberOrderDao.findMemberOrderById(id);
	}

	public MemberOrder findMemberOrderById(String id) {
		return memberOrderDao.findMemberOrderById(id);
	}
}
