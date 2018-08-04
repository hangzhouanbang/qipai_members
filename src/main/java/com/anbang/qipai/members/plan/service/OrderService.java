package com.anbang.qipai.members.plan.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.plan.bean.MemberClubCard;
import com.anbang.qipai.members.plan.bean.MemberOrder;
import com.anbang.qipai.members.plan.dao.ClubCardDao;
import com.anbang.qipai.members.plan.dao.MemberDao;
import com.anbang.qipai.members.plan.dao.OrderDao;

@Service
public class OrderService {

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private MemberDao memberDao;

	@Autowired
	private ClubCardDao clubCardDao;

	public MemberOrder addOrder(String memberId, String clubCardId, Integer number, String pay_type, String reqIP) {
		MemberOrder order = new MemberOrder();
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
		MemberClubCard clubCard = clubCardDao.getClubCardById(clubCardId);
		order.setClubCardName(clubCard.getName());
		order.setClubCardPrice(clubCard.getPrice());
		order.setGold(clubCard.getGold());
		order.setScore(clubCard.getScore());
		order.setVipTime(clubCard.getTime());
		order.setNumber(number);
		order.setTotalamount(clubCard.getPrice() * number);
		order.setCreateTime(System.currentTimeMillis());
		orderDao.addOrder(order);
		return order;
	}

	public MemberOrder findOrderByOut_trade_no(String out_trade_no) {
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
