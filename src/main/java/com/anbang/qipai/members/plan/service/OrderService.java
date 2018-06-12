package com.anbang.qipai.members.plan.service;

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

	public Order addOrder(String memberId, String clubCardId, Integer number) {
		Order order = new Order();
		order.setStatus(0);
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
		order.setCreateTime(System.currentTimeMillis());
		orderDao.addOrder(order);
		return order;
	}
}
