package com.anbang.qipai.members.plan.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.plan.dao.ClubCardDao;
import com.anbang.qipai.members.plan.domain.ClubCard;
import com.mongodb.WriteResult;

@Component
public class MongodbClubCardDao implements ClubCardDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<ClubCard> findAllClubCard() {
		return mongoTemplate.findAll(ClubCard.class);
	}

	@Override
	public ClubCard getClubCardById(String clubCardId) {
		Query query = new Query(Criteria.where("id").is(clubCardId));
		return mongoTemplate.findOne(query, ClubCard.class);
	}

	@Override
	public void addClubCard(ClubCard clubCard) {
		mongoTemplate.insert(clubCard);
	}

	@Override
	public boolean deleteClubCardByIds(String[] clubCardIds) {
		Object[] ids = clubCardIds;
		Query query = new Query(Criteria.where("id").in(ids));
		WriteResult writeResult = mongoTemplate.remove(query, ClubCard.class);
		return writeResult.getN() <= clubCardIds.length;
	}

	@Override
	public boolean updateClubCard(ClubCard clubCard) {
		Query query = new Query(Criteria.where("id").is(clubCard.getId()));
		Update update = new Update();
		update.set("name", clubCard.getName());
		update.set("price", clubCard.getPrice());
		update.set("gold", clubCard.getGold());
		update.set("score", clubCard.getScore());
		update.set("time", clubCard.getTime());
		WriteResult writeResult = mongoTemplate.updateFirst(query, update, ClubCard.class);
		return writeResult.getN() > 0;
	}

}
