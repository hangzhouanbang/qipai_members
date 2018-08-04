package com.anbang.qipai.members.plan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.members.plan.bean.MemberClubCard;
import com.anbang.qipai.members.plan.dao.ClubCardDao;

@Service
public class ClubCardService {

	@Autowired
	private ClubCardDao clubCardDao;

	public List<MemberClubCard> showClubCard() {
		return clubCardDao.findAllClubCard();
	}

	public void addClubCard(MemberClubCard clubCard) {
		clubCardDao.addClubCard(clubCard);
	}

	public boolean deleteClubCards(String[] clubCardIds) {
		return clubCardDao.deleteClubCardByIds(clubCardIds);
	}

	public boolean updateClubCard(MemberClubCard clubCard) {
		return clubCardDao.updateClubCard(clubCard);
	}

}
