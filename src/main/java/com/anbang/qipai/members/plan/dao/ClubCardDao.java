package com.anbang.qipai.members.plan.dao;

import java.util.List;

import com.anbang.qipai.members.plan.bean.MemberClubCard;

public interface ClubCardDao {

	List<MemberClubCard> findAllClubCard();

	MemberClubCard getClubCardById(String clubCardId);

	void addClubCard(MemberClubCard clubCard);

	void deleteClubCardByIds(String[] clubCardIds);

	void updateClubCard(MemberClubCard clubCard);

	MemberClubCard getClubCardByTime(long time);
}
