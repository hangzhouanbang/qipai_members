package com.anbang.qipai.members.plan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.members.plan.dao.ClubCardDao;
import com.anbang.qipai.members.plan.domain.ClubCard;

@Service
public class ClubCardService {

	@Autowired
	private ClubCardDao clubCardDao;

	public List<ClubCard> showClubCard() {
		return clubCardDao.getAllClubCard();
	}

}
