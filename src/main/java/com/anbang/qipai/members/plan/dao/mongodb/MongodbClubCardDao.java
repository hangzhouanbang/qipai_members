package com.anbang.qipai.members.plan.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.plan.dao.ClubCardDao;
import com.anbang.qipai.members.plan.domain.ClubCard;

@Component
public class MongodbClubCardDao implements ClubCardDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<ClubCard> getAllClubCard() {
		return mongoTemplate.findAll(ClubCard.class);
	}

}
