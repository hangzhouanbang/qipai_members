package com.anbang.qipai.members.plan.dao;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;

import com.anbang.qipai.members.plan.domain.ClubCard;

public interface ClubCardDao {

	List<ClubCard> getAllClubCard();

	ClubCard getClubCardById(Query query);
}
