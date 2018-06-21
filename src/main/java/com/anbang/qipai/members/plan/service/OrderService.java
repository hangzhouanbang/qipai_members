package com.anbang.qipai.members.plan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.members.plan.dao.ClubCardDao;
import com.anbang.qipai.members.plan.dao.MemberDao;
import com.anbang.qipai.members.plan.dao.OrderDao;
import com.anbang.qipai.members.plan.domain.Order;

@Service
public class OrderService {

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private MemberDao memberDao;

	@Autowired
	private ClubCardDao clubCardDao;

	private static long out_trade_no;// 商户流水号

	public Order addOrder(String memberId, String clubCardId, Integer number, String pay_type, String reqIP) {
		Order order = new Order();
		order.setOut_trade_no(String.valueOf(out_trade_no));
		out_trade_no++;
		order.setPay_type(pay_type);
		order.setReqIP(reqIP);
		order.setStatus(0);
		// order.setMemberId(memberId);
		// MemberDbo member = memberDao.findMemberById(memberId);
		// order.setNickname(member.getNickname());
		// order.setClubCardId(clubCardId);
		// ClubCard clubCard = clubCardDao.getClubCardById(clubCardId);
		// order.setClubCardName(clubCard.getName());
		// order.setClubCardPrice(clubCard.getPrice());
		// order.setGold(clubCard.getGold());
		// order.setScore(clubCard.getScore());
		// order.setVipTime(clubCard.getTime());
		// order.setNumber(number);
		// order.setTotalamount(clubCard.getPrice() * number);
		order.setMemberId("001");
		order.setNickname("test");
		order.setClubCardId("001");
		order.setClubCardName("周卡");
		order.setClubCardPrice(12.01);
		order.setGold(1000);
		order.setScore(2000);
		order.setVipTime(Long.valueOf("12312"));
		order.setNumber(2);
		order.setTotalamount(0.01);
		order.setCreateTime(System.currentTimeMillis());
		orderDao.addOrder(order);
		return order;
	}

	public Order findOrderByOut_trade_no(String out_trade_no) {
		return orderDao.findOrderByOut_trade_no(out_trade_no);
	}

	public Boolean updateOrderStatus(String out_trade_no, int status) {
		Order order = orderDao.findOrderByOut_trade_no(out_trade_no);
		if (order.getStatus() == 0) {
			return orderDao.updateOrderStatus(out_trade_no, status);
		}
		return false;
	}

	public Boolean updateTransaction_id(String out_trade_no, String transaction_id) {
		Order order = orderDao.findOrderByOut_trade_no(out_trade_no);
		if (order.getStatus() == 0) {
			return orderDao.updateTransaction_id(out_trade_no, transaction_id);
		}
		return false;
	}
}
