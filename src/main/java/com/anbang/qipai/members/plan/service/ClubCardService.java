package com.anbang.qipai.members.plan.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.members.plan.bean.MemberClubCard;
import com.anbang.qipai.members.plan.dao.ClubCardDao;
import com.anbang.qipai.members.plan.dao.MemberOrderDao;
import com.anbang.qipai.members.web.vo.MemberClubCardVO;

@Service
public class ClubCardService {

	@Autowired
	private ClubCardDao clubCardDao;

	@Autowired
	private MemberOrderDao memberOrderDao;

	public List<MemberClubCardVO> showClubCard(String payerId) {
		List<MemberClubCardVO> volist = new ArrayList<>();
		List<MemberClubCard> cardList = clubCardDao.findAllClubCard();
		cardList.forEach((card) -> {
			MemberClubCardVO vo = new MemberClubCardVO();
			vo.setId(card.getId());
			vo.setName(card.getName());
			vo.setGold(card.getGold());
			vo.setScore(card.getScore());
			vo.setTime(card.getTime());
			vo.setPrice(card.getPrice());
			if (card.getName().equals("日卡")
					&& memberOrderDao.findMemberOrderByPayerIdAndProductName(payerId, card.getName()) == null) {
				BigDecimal oldPrice = new BigDecimal(Double.toString(card.getPrice()));
				BigDecimal discount = new BigDecimal(Double.toString(0.1));
				vo.setDistCountPrice(oldPrice.multiply(discount).doubleValue());
				vo.setHasNotBuy(true);
			}
			volist.add(vo);
		});
		return volist;
	}

	public void addClubCard(MemberClubCard clubCard) {
		clubCardDao.addClubCard(clubCard);
	}

	public void deleteClubCards(String[] clubCardIds) {
		clubCardDao.deleteClubCardByIds(clubCardIds);
	}

	public void updateClubCard(MemberClubCard clubCard) {
		clubCardDao.updateClubCard(clubCard);
	}

	public MemberClubCard findClubCardById(String clubCardId) {
		return clubCardDao.getClubCardById(clubCardId);
	}

}
