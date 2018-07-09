package com.anbang.qipai.members.plan.dao;

import java.util.List;

import com.anbang.qipai.members.plan.domain.ClubCard;

public interface ClubCardDao {

	List<ClubCard> findAllClubCard();

	ClubCard getClubCardById(String clubCardId);

	void addClubCard(ClubCard clubCard);

	boolean deleteClubCardByIds(String[] clubCardIds);

	boolean updateClubCard(ClubCard clubCard);
}
