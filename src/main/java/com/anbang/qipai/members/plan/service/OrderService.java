package com.anbang.qipai.members.plan.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.plan.dao.ClubCardDao;
import com.anbang.qipai.members.plan.dao.MemberDao;
import com.anbang.qipai.members.plan.dao.OrderDao;
import com.anbang.qipai.members.plan.domain.ClubCard;
import com.anbang.qipai.members.plan.domain.Order;

@Service
public class OrderService {

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private MemberDao memberDao;

	@Autowired
	private ClubCardDao clubCardDao;

	public Order addOrder(String memberId, String clubCardId, Integer number, String pay_type, String reqIP) {
		Order order = new Order();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		order.setOut_trade_no(format.format(date) + memberId + UUID.randomUUID().toString().substring(22, 32));
		order.setPay_type(pay_type);
		order.setReqIP(reqIP);
		order.setStatus("WAIT_BUYER_PAY");
		order.setMemberId(memberId);
		MemberDbo member = memberDao.findMemberById(memberId);
		order.setNickname(member.getNickname());
		order.setClubCardId(clubCardId);
		ClubCard clubCard = clubCardDao.getClubCardById(clubCardId);
		order.setClubCardName(clubCard.getName());
		order.setClubCardPrice(clubCard.getPrice());
		order.setGold(clubCard.getGold());
		order.setScore(clubCard.getScore());
		order.setVipTime(clubCard.getTime());
		order.setNumber(number);
		order.setTotalamount(clubCard.getPrice() * number);
		// 测试代码
		/*
		 * order.setMemberId("001"); order.setNickname("test");
		 * order.setClubCardId("001"); order.setClubCardName("周卡");
		 * order.setClubCardPrice(12.01); order.setGold(1000); order.setScore(2000);
		 * order.setVipTime(Long.valueOf("12312")); order.setNumber(1);
		 * order.setTotalamount(0.01);
		 */
		order.setCreateTime(System.currentTimeMillis());
		orderDao.addOrder(order);
		return order;
	}

	public Order findOrderByOut_trade_no(String out_trade_no) {
		return orderDao.findOrderByOut_trade_no(out_trade_no);
	}

	public Boolean updateOrderStatus(String out_trade_no, String status) {
		return orderDao.updateOrderStatus(out_trade_no, status);
	}

	public Boolean updateTransaction_id(String out_trade_no, String transaction_id) {
		return orderDao.updateTransaction_id(out_trade_no, transaction_id);
	}

	public Boolean updateDeliveTime(String out_trade_no, Long deliveTime) {
		return orderDao.updateDeliveTime(out_trade_no, deliveTime);
	}
}
